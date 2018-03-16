package com.xhb.sockserv.meter;

import java.text.SimpleDateFormat;
import java.util.List;

import com.xhb.core.entity.DataElectricity;
import com.xhb.core.entity.ElectricityType;
import com.xhb.core.entity.ReceiptCircuit;
import com.xhb.core.entity.ReceiptCollector;
import com.xhb.core.entity.ReceiptMeter;


/*
 * XHB-DZG1252D 力创
 */
public class MeterDC extends AbstractDevice {

	private int address;
	private int ratioV = 1; // 电压变比
	private int ratioC = 1; // 电流变比

	private double voltage = 0;
	private double current = 0;
	private double power = 0;
	private double energy = 0;
	private String time = "";

	public MeterDC(ReceiptCollector receiptCollector, ReceiptMeter receiptMeter) {
		this.receiptCollector = receiptCollector;
		this.receiptMeter = receiptMeter;
		address = Integer.parseInt(receiptMeter.getMeterNo());
		buildWritingFrames();
	}

	@Override
	public void buildWritingFrames() {
		writingFrames.add(makeFrame(address));
	}

	private byte[] makeFrame(int address) {

		int[] senddata = new int[8];
		byte[] senddata2 = new byte[8];
		int[] crc = new int[2];
		senddata[0] = address;
		senddata[1] = 0x03;
		senddata[2] = 0x00;
		senddata[3] = 0x02;
		senddata[4] = 0x00;
		senddata[5] = 0x11;

		crc = CRC.calculateCRC(senddata, 6);
		senddata[6] = crc[0];
		senddata[7] = crc[1];

		for (int i = 0; i < 8; i++)
			senddata2[i] = (byte) senddata[i];

		return senddata2;
	}

	@Override
	public boolean analyzeFrame(byte[] frame) {
		int[] data = new int[frame.length - 9];
		for (int i = 0; i < data.length; i++) {
			data[i] = frame[i + 8] & 0xFF;
		}
		// crc校验是否正确
		if (!CRC.isValid(data))
			return false;
		if (data[2] != 0x22)
			return false;
		double U0 = data[3] * 256 + data[4];
		double I0 = (data[5] * 256 + data[6]) / 10;
		ratioV = data[9] * 256 + data[10];
		ratioC = data[11] * 256 + data[12];
		// 电表当前时间：精确到分
		time = "20";
		for (int i = 17; i < 22; i++) {
			if (data[i] < 0x10)
				time += "0" + Integer.toHexString(data[i]);
			else
				time += Integer.toHexString(data[i]);
		}
		energy = (data[23] * 256 * 256 * 256 + data[24] * 256 * 256 + data[25] * 256 + data[26]) * U0 * I0 / 10800000;
		voltage = (data[31] * 256 + data[32]) * U0 / 10000;
		current = (data[33] * 256 + data[34]) * I0 / 10000;
		power = (data[35] * 256 + data[36]) * U0 * I0 / (10000 * 1000);
		return true;
	}

	@Override
	public void handleResult() {
		try {
			List<ReceiptCircuit> receiptCircuitList = services.receiptCircuitService
					.queryByMeterId(receiptMeter.getId());
			for (ReceiptCircuit receiptCircuit : receiptCircuitList) {
				if (config.isRateFromDataBaseEnabled()) {
					if (receiptCircuit == null) {
						return;
					}
					if (receiptCircuit.getVoltageRatio() != null) {
						ratioV = receiptCircuit.getVoltageRatio();
					}
					if (receiptCircuit.getCurrentRatio() != null) {
						ratioC = receiptCircuit.getCurrentRatio();
					}
				}
				resultMutiplyRate();
				DataElectricity dataElectricity = new DataElectricity();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
				dataElectricity.setReceiptCircuit(receiptCircuit);
				dataElectricity.setReadTime(sdf.parse(time));
				dataElectricity.setElectricityType(ElectricityType.DC);
				dataElectricity.setVoltage(voltage);
				dataElectricity.setCurrent(current);
				dataElectricity.setKw(power);
				dataElectricity.setKwh(energy);
				services.dataElectricityService.save(dataElectricity);
			}
		} catch (Exception ex) {
			logger.info("save MeterDC data error! circuit of dtuNo:" + receiptCollector.getCollectorNo()
					+ " and meterNo:" + receiptMeter.getMeterNo(), ex);
		}
	}

	
	private void resultMutiplyRate() {
		voltage *= ratioV;
		current *= ratioC;
		power *= ratioV * current;
		energy *= ratioV * current;
	}

}
