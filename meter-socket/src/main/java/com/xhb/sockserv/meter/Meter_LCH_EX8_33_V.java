package com.xhb.sockserv.meter;

import com.xhb.core.entity.DataElectricity;
import com.xhb.core.entity.ElectricityType;
import com.xhb.core.entity.ReceiptCircuit;
import com.xhb.core.entity.ReceiptCollector;
import com.xhb.core.entity.ReceiptMeter;

/*
 * 力创EX8_33_V
 * @author jc 改编自CS中孙家伟写的协议 
 * @time 2016-04-11
 */
public class Meter_LCH_EX8_33_V extends AbstractDevice {

	private double u0;
	private double i0;
	private double Ubb = 1;
	private double Ibb = 1;
	private double kwhForward;
	private double kwhReverse;
	private double kvarhForward;
	private double kvarhReverse;
	private double kwh;
	private double voltageA;
	private double voltageB;
	private double voltageC;
	private double lineVoltageAB;
	private double lineVoltageBC;
	private double lineVoltageAC;
	private double currentA;
	private double currentB;
	private double currentC;
	private double kw;
	private double kvar;
	private double powerFactor;
	private double frequency;
	private double kwA;
	private double kwB;
	private double kwC;
	private double kvarA;
	private double kvarB;
	private double kvarC;
	private double powerFactorA;
	private double powerFactorB;
	private double powerFactorC;

	
	public Meter_LCH_EX8_33_V(ReceiptCollector receiptCollector, ReceiptMeter receiptMeter) {
		this.receiptCollector = receiptCollector;
		this.receiptMeter = receiptMeter;
		buildWritingFrames();
	}

	@Override
	public void buildWritingFrames() {
		makeFrame1();
		makeFrame2();
	}

	private void makeFrame2() {
		int[] data = new int[8];
		data[0] = Integer.parseInt(receiptMeter.getMeterNo());
		data[1] = 0x03;
		data[2] = 0x00;
		data[3] = 0x40;
		data[4] = 0x00;
		data[5] = 0x1A;
		int[] crc = CRC.calculateCRC(data, 6);
		data[6] = crc[0];
		data[7] = crc[1];
		byte[] frame = new byte[data.length];
		for (int i = 0; i < data.length; i++) {
			frame[i] = (byte) data[i];
		}
		writingFrames.add(frame);
	}

	private void makeFrame1() {
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
		if (meterNo != data[0]) {
			return false;
		}
		if (!CRC.isValid(data))
			return false;
		if (data[2] == 0x30) {
			analyzeFrame1(data);
		}
		if (data[2] == 0x34) {
			analyzeFrame2(data);
		}
		return true;
	}

	private void analyzeFrame1(int[] data) {
		u0 = (data[5] * Math.pow(2, 8) + data[6]);
		i0 = (data[7] * Math.pow(2, 8) + data[8]) / 10;
		Ubb = (data[11] * Math.pow(2, 8) + data[12]);
		Ibb = (data[13] * Math.pow(2, 8) + data[14]);
		kwhForward = ((data[27] * Math.pow(2, 24) + data[28] * Math.pow(2, 16) + data[29] * Math.pow(2, 8) + data[30])
				* u0 * i0) / 10800000;
		kwhReverse = ((data[31] * Math.pow(2, 24) + data[32] * Math.pow(2, 16) + data[33] * Math.pow(2, 8) + data[34])
				* u0 * i0) / 10800000;
		kvarhForward = ((data[35] * Math.pow(2, 24) + data[36] * Math.pow(2, 16) + data[37] * Math.pow(2, 8) + data[38])
				* u0 * i0) / 10800000;
		kvarhReverse = ((data[39] * Math.pow(2, 24) + data[40] * Math.pow(2, 16) + data[41] * Math.pow(2, 8) + data[42])
				* u0 * i0) / 10800000;
		kwh = ((data[43] * Math.pow(2, 24) + data[44] * Math.pow(2, 16) + data[45] * Math.pow(2, 8) + data[46]) * u0
				* i0) / 10800000;
	}

	private void analyzeFrame2(int[] data) {
		voltageA = ((data[3] * Math.pow(2, 8) + data[4]) * u0) / 10000;
		voltageB = ((data[5] * Math.pow(2, 8) + data[6]) * u0) / 10000;
		voltageC = ((data[7] * Math.pow(2, 8) + data[8]) * u0) / 10000;
		lineVoltageAB = ((data[9] * Math.pow(2, 8) + data[10]) * u0) / 10000;
		lineVoltageBC = ((data[11] * Math.pow(2, 8) + data[12]) * u0) / 10000;
		lineVoltageAC = ((data[13] * Math.pow(2, 8) + data[14]) * u0) / 10000;
		currentA = ((data[15] * Math.pow(2, 8) + data[16]) * i0) / 10000;
		currentB = ((data[17] * Math.pow(2, 8) + data[18]) * i0) / 10000;
		currentC = ((data[19] * Math.pow(2, 8) + data[20]) * i0) / 10000;
		kw = ((data[21] * Math.pow(2, 8) + data[22]) * u0 * i0 * 3) / 10000;
		kvar = ((data[23] * Math.pow(2, 8) + data[24]) * u0 * i0 * 3) / 10000;
		powerFactor = (data[27] * Math.pow(2, 8) + data[28]) / 10000;
		frequency = (data[29] * Math.pow(2, 8) + data[30]) / 100;
		kwA = ((data[31] * Math.pow(2, 8) + data[32]) * u0 * i0) / 10000;
		kwB = ((data[33] * Math.pow(2, 8) + data[34]) * u0 * i0) / 10000;
		kwC = ((data[35] * Math.pow(2, 8) + data[36]) * u0 * i0) / 10000;
		kvarA = ((data[37] * Math.pow(2, 8) + data[38]) * u0 * i0) / 10000;
		kvarB = ((data[39] * Math.pow(2, 8) + data[40]) * u0 * i0) / 10000;
		kvarC = ((data[41] * Math.pow(2, 8) + data[42]) * u0 * i0) / 10000;
		powerFactorA = (data[49] * Math.pow(2, 8) + data[50]) / 10000;
		powerFactorB = (data[51] * Math.pow(2, 8) + data[52]) / 10000;
		powerFactorC = (data[53] * Math.pow(2, 8) + data[54]) / 10000;
	}

	private void resultMutiplyRate() {
		kwhForward = kwhForward * Ubb * Ibb;
		kwhReverse = kwhReverse * Ubb * Ibb;
		kvarhForward = kvarhForward * Ubb * Ibb;
		kvarhReverse = kvarhReverse * Ubb * Ibb;
		kwh = kwh * Ubb * Ibb;
		voltageA *= Ubb;
		voltageB *= Ubb;
		voltageC *= Ubb;
		lineVoltageAB *= Ubb;
		lineVoltageBC *= Ubb;
		lineVoltageAC *= Ubb;
		currentA *= Ibb;
		currentB *= Ibb;
		currentC *= Ibb;
		kw = kw * Ubb * Ibb;
		kvar = kvar * Ubb * Ibb;
		kwA = kwA * Ubb * Ibb;
		kwB = kwB * Ubb * Ibb;
		kwC = kwC * Ubb * Ibb;
		kvarA = kvarA * Ubb * Ibb;
		kvarB = kvarB * Ubb * Ibb;
		kvarC = kvarC * Ubb * Ibb;
	}

	@Override
	public void handleResult() {
		ReceiptCircuit receiptCircuit = services.receiptCircuitService.findCircuitByDtuNoAndMeterNoAndLoopNo(receiptCollector.getCollectorNo(), receiptMeter.getMeterNo(), 1);;
		if(config.isRateFromDataBaseEnabled()){
			if (receiptCircuit == null) {
				return;
			}
			if(receiptCircuit.getVoltageRatio() != null){
				Ubb = receiptCircuit.getVoltageRatio();
			}
			if (receiptCircuit.getCurrentRatio() != null) {
				Ibb  = receiptCircuit.getCurrentRatio();
			}
		}
		resultMutiplyRate();
		DataElectricity dataElectricity = new DataElectricity();
		dataElectricity.setCurrentA(currentA);
		dataElectricity.setCurrentB(currentB);
		dataElectricity.setCurrentC(currentC);
		dataElectricity.setElectricityType(ElectricityType.AC_THREE);
		dataElectricity.setFrequency(frequency);
		dataElectricity.setKvar(kvar);
		dataElectricity.setKvarA(kvarA);
		dataElectricity.setKvarB(kvarB);
		dataElectricity.setKvarC(kvarC);
		dataElectricity.setKw(kw);
		dataElectricity.setKwA(kwA);
		dataElectricity.setKwB(kwB);
		dataElectricity.setKwC(kwC);
		dataElectricity.setVoltageA(voltageA);
		dataElectricity.setVoltageB(voltageB);
		dataElectricity.setVoltageC(voltageC);
		dataElectricity.setKwh(kwh);
		dataElectricity.setKwhForward(kwhForward);
		dataElectricity.setKwhReverse(kwhReverse);
		dataElectricity.setVoltageAB(lineVoltageAB);
		dataElectricity.setVoltageBC(lineVoltageBC);
		dataElectricity.setVoltageCA(lineVoltageAC);
		dataElectricity.setPowerFactor(powerFactor);
		dataElectricity.setPowerFactorA(powerFactorA);
		dataElectricity.setPowerFactorB(powerFactorB);
		dataElectricity.setPowerFactorC(powerFactorC);
		dataElectricity.setReceiptCircuit(receiptCircuit);
		services.dataElectricityService.save(dataElectricity);
	}

}
