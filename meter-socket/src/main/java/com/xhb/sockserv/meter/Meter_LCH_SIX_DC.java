package com.xhb.sockserv.meter;

import java.util.Date;
import java.util.List;

import com.xhb.core.entity.DataElectricity;
import com.xhb.core.entity.ElectricityType;
import com.xhb.core.entity.ReceiptCircuit;
import com.xhb.core.entity.ReceiptCollector;
import com.xhb.core.entity.ReceiptMeter;

/*
 * 力创六路直流表
 * @author jc 改编自CS中的协议 
 * @time 2016-04-12
 */
public class Meter_LCH_SIX_DC extends AbstractDevice {

	private double u0 = 48;
	private double i0 = 50;
	private double Ubb = 1;
	private double Ibb = 1;
	
	private double[] voltage = new double[6];
	private double[] current = new double[6];
	private double[] pow = new double[6];
	private double[] kwh_forward = new double[6];
	private double[] kwh_reverse = new double[6];
	private double[] kwh_total = new double[6];
	
	public Meter_LCH_SIX_DC(ReceiptCollector receiptCollector, ReceiptMeter receiptMeter) {
		this.receiptCollector = receiptCollector;
		this.receiptMeter = receiptMeter;
		buildWritingFrames();
	}
	@Override
	public void buildWritingFrames() {
		makeFrameOne();
		makeFrameTwo();
	}

	private void makeFrameTwo() {
		int[] data = new int[8];
		data[0] = Integer.parseInt(receiptMeter.getMeterNo());
		data[1] = 0x03;
		data[2] = 0x00;
		data[3] = 0x19;
		data[4] = 0x00;
		data[5] = 0x27;
		int[] crc = CRC.calculateCRC(data, 6);
		data[6] = crc[0];
		data[7] = crc[1];
		byte[] frame = new byte[data.length];
		for (int i = 0; i < data.length; i++) {
			frame[i] = (byte) data[i];
		}
		writingFrames.add(frame);
	}

	private void makeFrameOne() {
		int[] data = new int[8];
		data[0] = Integer.parseInt(receiptMeter.getMeterNo());
		data[1] = 0x03;
		data[2] = 0x00;
		data[3] = 0x00;
		data[4] = 0x00;
		data[5] = 0x18;
		int[] crc = CRC.calculateCRC(data, 6);
		data[6] = crc[0];
		data[7] = crc[1];
		byte[] frame = new byte[data.length];
		for (int i = 0; i < data.length; i++) {
			frame[i] = (byte) data[i];
		}
		writingFrames.add(frame);
	}


	@Override
	public boolean analyzeFrame(byte[] frame) {
		if (readingFrames.size() != 2) {
			return false;
		}
		int[] data = new int[frame.length - 9];
		for (int i = 0; i < data.length; i++) {
			data[i] = frame[i + 8] & 0xFF;
		}
		int meterNo = Integer.parseInt(receiptMeter.getMeterNo()); 
		if(meterNo != data[0]){
			return false;
		}
		if (!CRC.isValid(data))
			return false;
		if (data[2] == 0x30) {
			analyzeData(data);
		}else if (data[2] == 0x4E) {
			analyzeDataRest(data);
		}
		return true;
	}

	private void analyzeDataRest(int[] data) {
		for (int i = 0; i < 5; i++) {
			voltage[i + 1] = (data[3 + i * 16] * 256 + data[4 + i * 16]) * u0 / 10000.0;
			current[i + 1] = (data[5 + i * 16] * 256 + data[6 + i * 16]) * i0 / 10000.0;
			pow[i + 1] = (data[7 + i * 16] * 256 + data[8 + i * 16]) * u0 * i0 / 10000.0 / 1000.0;
			kwh_forward[i + 1] = ((data[9 + i * 16] * 256 * 256 * 256 + data[10 + i * 16] * 256 * 256 
					+ data[11 + i * 16] * 256 + data[12 + i * 16]) * u0 * i0) / 10800000;
			kwh_reverse[i + 1] = ((data[13 + i * 16] * 256 * 256 * 256 + data[14 + i * 16] * 256 * 256 
					+ data[15 + i * 16] * 256 + data[16 + i * 16]) * u0 * i0) / 10800000;
			kwh_total[i + 1] = kwh_forward[i + 1] + kwh_reverse[i + 1];
		}
	}

	private void analyzeData(int[] data) {
		u0 = data[19] * 256 + data[20];
		i0 = data[21] * 256 + data[22];
		Ubb = data[23] * 256 + data[24];
		Ibb = data[25] * 256 + data[26];
		voltage[0] = (data[37] * 256 + data[38]) * u0 / 10000.0;
		current[0] = (data[39] * 256 + data[40]) * i0 / 10000.0;
		pow[0] = (data[41] * 256 + data[42]) * u0 * i0 / 10000.0 / 1000.0;
		kwh_forward[0] = ((data[43] * 256 * 256 * 256 + data[44] * 256 * 256 
				+ data[45] * 256 + data[46]) * u0 * i0) / 10800000;
		kwh_reverse[0] = ((data[47] * 256 * 256 * 256 + data[48] * 256 * 256 
				+ data[49] * 256 + data[50]) * u0 * i0) / 10800000;
		kwh_total[0] = kwh_forward[0] + kwh_reverse[0];
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
						Ubb = receiptCircuit.getVoltageRatio();
					}
					if (receiptCircuit.getCurrentRatio() != null) {
						Ibb = receiptCircuit.getCurrentRatio();
					}
				}
				Integer loopNo = Integer.parseInt(receiptCircuit.getCircuitNo());
				resultMutiplyRate();
				DataElectricity dataElectricity = new DataElectricity();
				dataElectricity.setReceiptCircuit(receiptCircuit);
				dataElectricity.setReadTime(new Date());
				dataElectricity.setElectricityType(ElectricityType.DC);
				dataElectricity.setVoltage(voltage[loopNo-1]);
				dataElectricity.setCurrent(current[loopNo-1]);
				dataElectricity.setKw(pow[loopNo-1]);
				dataElectricity.setKwh(kwh_total[loopNo-1]);
				dataElectricity.setKwhForward(kwh_forward[loopNo-1]);
				dataElectricity.setKwhReverse(kwh_reverse[loopNo-1]);
				services.dataElectricityService.save(dataElectricity);
			}
		} catch (Exception ex) {
			logger.info("save Meter_LCH_SIX_DC data error! circuit of dtuNo:" + receiptCollector.getCollectorNo()
					+ " and meterNo:" + receiptMeter.getMeterNo(), ex);
		}
	}

	private void resultMutiplyRate() {
		for (int i = 0; i < 6; i++) {
			voltage[i] *= Ubb;
			current[i] *= Ibb;
			pow[i] *= Ubb * Ibb;
			kwh_forward[i] *= Ubb * Ibb; 
			kwh_reverse[i] *= Ubb * Ibb; 
			kwh_total[i] *= Ubb * Ibb;
		}
	}
}
