package com.xhb.sockserv.meter;

import java.util.Date;
import java.util.List;

import com.xhb.core.entity.DataElectricity;
import com.xhb.core.entity.ElectricityType;
import com.xhb.core.entity.ReceiptCircuit;
import com.xhb.core.entity.ReceiptCollector;
import com.xhb.core.entity.ReceiptMeter;

public class Meter_MODEL_DC extends AbstractDevice {

	private int address;
	private int Ubb = 1;
	private double[] IBB = new double[2];
	private double voltage;
	private double[] current = new double[2];
	private double[] pow = new double[2];
	private double[] kwh_total = new double[2];

	public Meter_MODEL_DC(ReceiptCollector receiptCollector, ReceiptMeter receiptMeter) {
		this.receiptCollector = receiptCollector;
		this.receiptMeter = receiptMeter;
		address = Integer.parseInt(receiptMeter.getMeterNo());
		buildWritingFrames();
	}

	@Override
	public void buildWritingFrames() {
		writingFrames.add(makeFrame(address));
		writingFrames.add(makeFrame1(address));
	}

	private byte[] makeFrame(int address) {
		int[] senddata = new int[8];
		byte[] senddata2 = new byte[8];
		int[] crc = new int[2];
		senddata[0] = address;
		senddata[1] = 0x03;
		senddata[2] = 0x00;
		senddata[3] = 0x06;
		senddata[4] = 0x00;
		senddata[5] = 0x03;
		crc = CRC.calculateCRC(senddata, 6);
		senddata[6] = crc[0];
		senddata[7] = crc[1];
		for (int i = 0; i < 8; i++)
			senddata2[i] = (byte) senddata[i];
		return senddata2;
	}
	private byte[] makeFrame1(int address) {
		int[] senddata = new int[8];
		byte[] senddata2 = new byte[8];
		int[] crc = new int[2];
		senddata[0] = address;
		senddata[1] = 0x03;
		senddata[2] = 0x10;
		senddata[3] = 0x00;
		senddata[4] = 0x00;
		senddata[5] = 0x0E;
		crc = CRC.calculateCRC(senddata, 6);
		senddata[6] = crc[0];
		senddata[7] = crc[1];
		for (int i = 0; i < 8; i++)
			senddata2[i] = (byte) senddata[i];
		return senddata2;
	}
	@Override
	public boolean analyzeFrame(byte[] frame) {
		if(readingFrames.size() != 2) {
		   return false;
		}
		int[] data = new int[frame.length - 9];
		for (int i = 0; i < data.length; i++) {
			data[i] = frame[i + 8] & 0xFF;
		}
		// crc校验是否正确
		if (!CRC.isValid(data))
			return false;
		if (data[2] == 0x06){
			analyzeFrame0(data);
		}
		if (data[2] == 0x1C){
			analyzeFrame1(data);
		}
		return true;
	}
	private void analyzeFrame0(int[] data) {
		Ubb = data[3] * 256 + data[4];
		for(int i=0;i<2;i++){
			IBB[i] = data[5+i*2] * 256 + data[6+i*2];			
		}
	}

	private void analyzeFrame1(int[] data) {
		voltage = (data[11] * 256 + data[12])/ 100.0;
		for (int i = 0; i < 2; i++) {			
			current[i] = ((data[13 + i * 2] * 256 + data[14 + i * 2]))/ 100.0;
			pow[i] = ((data[17 + i * 2] * 256 + data[18+ i * 2]))/ 100.0;
			kwh_total[i] = (((data[21 + i * 4] * 256 * 256 * 256 + data[22 + i * 4] * 256 * 256 
					+ data[23 + i * 4] * 256 + data[24 + i * 4]) ))/ 100.0;
		}
	}

	@Override
	public void handleResult() {
		Date now = new Date();
		List<ReceiptCircuit> receiptCircuitList = services.receiptCircuitService.queryByMeterId(receiptMeter.getId());
	for (ReceiptCircuit receiptCircuit : receiptCircuitList) {
		if (receiptCircuit.getVoltageRatio() != null && receiptCircuit.getVoltageRatio() != 0) {
			Ubb = receiptCircuit.getVoltageRatio();
		}
		if (receiptCircuit.getCurrentRatio() != null && receiptCircuit.getCurrentRatio() != 0) {
			IBB[Integer.parseInt(receiptCircuit.getCircuitNo()) - 1] = receiptCircuit.getCurrentRatio();
		}
		resultMutiplyRate();
		DataElectricity dataElectricity = new DataElectricity();
		dataElectricity.setReceiptCircuit(receiptCircuit);
		dataElectricity.setReadTime(now);
		dataElectricity.setElectricityType(ElectricityType.DC);
		dataElectricity.setVoltage(voltage);
		dataElectricity.setCurrent(current[Integer.parseInt(receiptCircuit.getCircuitNo()) - 1]);
		dataElectricity.setKw(pow[Integer.parseInt(receiptCircuit.getCircuitNo()) - 1]);
		dataElectricity.setKwh(kwh_total[Integer.parseInt(receiptCircuit.getCircuitNo()) - 1]);
		services.dataElectricityService.save(dataElectricity);	
		}
	}

	private void resultMutiplyRate() {
		for (int i = 0; i < 2; i++) {
			voltage *= Ubb;
			current[i] *= IBB[i];
			pow[i] *= Ubb * IBB[i];
			kwh_total[i] *= Ubb * IBB[i];
		}
	}
}
