package com.xhb.sockserv.meter;

import java.util.Date;
import java.util.List;

import com.xhb.core.entity.DataElectricity;
import com.xhb.core.entity.ElectricityType;
import com.xhb.core.entity.ReceiptCircuit;
import com.xhb.core.entity.ReceiptCollector;
import com.xhb.core.entity.ReceiptMeter;

public class Meter_EIGHT_DC extends AbstractDevice {

	private int address;
	private int Ubb = 1;
	private double[] IBB = new double[8];
	private double[] voltage = new double[8];
	private double[] current = new double[8];
	private double[] pow = new double[8];
	private double[] kwh_total = new double[8];

	public Meter_EIGHT_DC(ReceiptCollector receiptCollector, ReceiptMeter receiptMeter) {
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
		senddata[3] = 0x09;
		senddata[4] = 0x00;
		senddata[5] = 0x09;
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
		senddata[3] = 0x02;
		senddata[4] = 0x00;
		senddata[5] = 0x2B;
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
		if (data[2] == 0x12){
			analyzeFrame0(data);
			}
		if (data[2] == 0x56){
			analyzeFrame1(data);
			}	
		return true;
	}
	private void analyzeFrame0(int[] data) {
		Ubb = data[3] * 256 + data[4];
		for(int i=0;i<8;i++){
			IBB[i] = data[5+i*2] * 256 + data[6+i*2];			
		}
	}

	private void analyzeFrame1(int[] data) {	
		for (int i = 0; i < 8; i++) {			
			voltage[i] = ((data[9 + i * 10] * 256 + data[10 + i * 10]))/ 100.0;
			current[i] = ((data[11 + i * 10] * 256 + data[12 + i * 10]))/ 100.0;
			pow[i] = ((data[13 + i * 10] * 256 + data[14+ i * 10]))/100.00;
			kwh_total[i] = (((data[15 + i * 10] * 256 * 256 * 256 + data[16 + i * 10] * 256 * 256 
					+ data[17 + i * 10] * 256 + data[18 + i * 10])))/100.00;
		}
	}

	@Override
	public void handleResult() {
		Date now = new Date();
		List<ReceiptCircuit> receiptCircuitList = services.receiptCircuitService
				.queryByMeterId(receiptMeter.getId());
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
		dataElectricity.setVoltage(voltage[Integer.parseInt(receiptCircuit.getCircuitNo()) - 1]);
		dataElectricity.setCurrent(current[Integer.parseInt(receiptCircuit.getCircuitNo()) - 1]);
		dataElectricity.setKw(pow[Integer.parseInt(receiptCircuit.getCircuitNo()) - 1]);
		dataElectricity.setKwh(kwh_total[Integer.parseInt(receiptCircuit.getCircuitNo()) - 1]);
		services.dataElectricityService.save(dataElectricity);
		}
	}

	private void resultMutiplyRate() {
		for (int i = 0; i < 8; i++) {
			voltage[i] *= Ubb;
			current[i] *= IBB[i];
			pow[i] *= Ubb * IBB[i];
			kwh_total[i] *= Ubb * IBB[i];
		}
	}
}
