package com.xhb.sockserv.meter;

import java.util.Date;
import java.util.List;

import com.xhb.core.entity.DataElectricity;
import com.xhb.core.entity.ElectricityType;
import com.xhb.core.entity.ReceiptCircuit;
import com.xhb.core.entity.ReceiptCollector;
import com.xhb.core.entity.ReceiptMeter;

/*
 * 苏教科使用的江阴中瑞电表ZRY4Z-9HY
 * @author jc 改编自CS中的协议 
 * @time 2016-04-12
 */
public class Meter_CHZJZ_ZRY4Z_9HY extends AbstractDevice {

	private double frequency;
	private double voltageA;
	private double voltageB;
	private double voltageC;
	private double currentA;
	private double currentB;
	private double currentC;
	private double kw;
	private double kwA;
	private double kwB;
	private double kwC;
	private double kvar;
	private double kvarA;
	private double kvarB;
	private double kvarC;
	private double powerFactor;
	private double kwh;
	private double kwhForward;
	private double kwhReverse;
	private double kvarh1;
	private double kvarh2;
	
	private double voltAB;
	private double voltBC;
	private double voltCA;
	private int Ibb = 1;
	private int Ubb = 1;
	
	
	public Meter_CHZJZ_ZRY4Z_9HY(ReceiptCollector receiptCollector, ReceiptMeter receiptMeter){
		this.receiptCollector = receiptCollector;
		this.receiptMeter = receiptMeter;
		buildWritingFrames();
	}
	@Override
	public void buildWritingFrames() {
		makeFrame();
		makeFrameRate();
	}

	private void makeFrame() {
		int[] data = new int[8];
		data[0] = Integer.parseInt(receiptMeter.getMeterNo());
		data[1] = 0x03;
		data[2] = 0x00;
		data[3] = 0x3E;
		data[4] = 0x00;
		data[5] = 0x21;

		int[] crc = CRC.calculateCRC(data, 6);
		data[6] = crc[0];
		data[7] = crc[1];

		byte[] frame = new byte[data.length];
		for (int i = 0; i < data.length; i++) {
			frame[i] = (byte) data[i];
		}
		writingFrames.add(frame);
	}
	private void makeFrameRate(){
		int[] data = new int[8];
		data[0] = Integer.parseInt(receiptMeter.getMeterNo());
		data[1] = 0x03;
		data[2] = 0x01;
		data[3] = 0xCF;
		data[4] = 0x00;
		data[5] = 0x02;

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
		if(readingFrames.size() != 2){
			return false;
		}
		int[] data = new int[frame.length - 9];
		for (int i = 0; i < data.length; i++) {
			data[i] = frame[i + 8] & 0xFF;
		}
		if (!CRC.isValid(data))
			return false;
		if(data[2] == 0x42 ){
			voltageA = (data[3] * 256 + data[4]) / 10.0;
			voltageB = (data[5] * 256 + data[6]) / 10.0;
			voltageC = (data[7] * 256 + data[8]) / 10.0;
			voltAB = (data[9] * 256 + data[10]) / 10.0;
			voltBC = (data[11] * 256 + data[12]) / 10.0;
			voltCA = (data[13] * 256 + data[14]) / 10.0;
			currentA = (data[15] * 256 + data[16]) / 1000.0;
			currentB = (data[17] * 256 + data[18]) / 1000.0;
			currentC = (data[19] * 256 + data[20]) / 1000.0;
			kw = (data[21] * 256 + data[22]) / 1000.0;
			kwA = (data[23] * 256 + data[24]) / 1000.0;
			kwB = (data[25] * 256 + data[26]) / 1000.0;
			kwC = (data[27] * 256 + data[28]) / 1000.0;
			kvar = (data[29] * 256 + data[30]) / 1000.0;
			kvarA = (data[31] * 256 + data[32]) / 1000.0;
			kvarB = (data[33] * 256 + data[34]) / 1000.0;
			kvarC = (data[35] * 256 + data[36]) / 1000.0;
			//视在kw = (data[37] * 256 + data[38]) / 1000.0;
			//视在kwA = (data[39] * 256 + data[40]) / 1000.0;
			//视在kwB = (data[41] * 256 + data[42]) / 1000.0;
			//视在kwC = (data[43] * 256 + data[44]) / 1000.0;
			powerFactor = (data[45] * 256 + data[46]) / 1000.0;
			frequency = (data[47] * 256 + data[48]) / 100.0;
			kwhForward = (data[49] * 256 * 256 * 256 + data[50] * 256 * 256 + data[51] * 256 + data[52]) / 1000.0;
			kwhReverse = (data[53] * 256 * 256 * 256 + data[54] * 256 * 256 + data[55] * 256 + data[56]) / 1000.0;
			kvarh1 = (data[57] * 256 * 256 * 256 + data[58] * 256 * 256 + data[59] * 256 + data[60]) / 1000.0;
			kvarh2 = (data[61] * 256 * 256 * 256 + data[62] * 256 * 256 + data[63] * 256 + data[64]) / 1000.0;
			kwh = (data[65] * 256 * 256 * 256 + data[66] * 256 * 256 + data[67] * 256 + data[68]) / 1000.0;
		}else if(data[2] == 0x04){
			Ubb = data[3] * 256 + data[4];
			Ibb = data[5] * 256 + data[6];
		}
		return true;
	}

	@Override
 	public void handleResult() {
		Date now = new Date();
		List<ReceiptCircuit> receiptCircuitList = services.receiptCircuitService.queryByMeterId(receiptMeter.getId());
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
			resultMutiplyRate();
			DataElectricity dataElectricity = new DataElectricity();
			dataElectricity.setReceiptCircuit(receiptCircuit);
			dataElectricity.setReadTime(now);
			dataElectricity.setElectricityType(ElectricityType.AC_THREE);
			dataElectricity.setFrequency(frequency);
			dataElectricity.setVoltageA(voltageA);
			dataElectricity.setVoltageB(voltageB);
			dataElectricity.setVoltageC(voltageC);
			dataElectricity.setVoltageAB(voltAB);
			dataElectricity.setVoltageBC(voltBC);
			dataElectricity.setVoltageCA(voltCA);
			dataElectricity.setCurrentA(currentA);
			dataElectricity.setCurrentB(currentB);
			dataElectricity.setCurrentC(currentC);
			dataElectricity.setKw(kw);
			dataElectricity.setKwA(kwA);
			dataElectricity.setKwB(kwB);
			dataElectricity.setKwC(kwC);
			dataElectricity.setKvar(kvar);
			dataElectricity.setKvarA(kvarA);
			dataElectricity.setKvarB(kvarB);
			dataElectricity.setKvarC(kvarC);
			dataElectricity.setKwh(kwh);
			dataElectricity.setKwhForward(kwhForward);
			dataElectricity.setKwhReverse(kwhReverse);
			dataElectricity.setKvarh1(kvarh1);
			dataElectricity.setKvarh2(kvarh2);
			dataElectricity.setPowerFactor(powerFactor);
			services.dataElectricityService.save(dataElectricity);
		}
	}

	private void resultMutiplyRate() {
		kwh *= Ubb * Ibb;
		kwhForward *= Ubb * Ibb; 
		kwhReverse *= Ubb * Ibb;
		kvarh1 *= Ubb * Ibb;
		kvarh2 *= Ubb * Ibb;
		voltageA *= Ubb;
		voltageB *= Ubb;
		voltageC *= Ubb;
		currentA *= Ibb;
		currentB *= Ibb;
		currentC *= Ibb;
		kw *= Ubb * Ibb;
		kwA *= Ubb * Ibb;
		kwB *= Ubb * Ibb;
		kwC *= Ubb * Ibb;
		kvar *= Ubb * Ibb;
		kvarA *= Ubb * Ibb;
		kvarB *= Ubb * Ibb;
		kvarC *= Ubb * Ibb;
		voltAB *= Ubb;
		voltBC *= Ubb;
		voltCA *= Ubb;
	}

}
