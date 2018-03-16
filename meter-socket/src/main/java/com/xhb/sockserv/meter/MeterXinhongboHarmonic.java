package com.xhb.sockserv.meter;

import java.util.Date;
import java.util.List;

import com.xhb.core.entity.DataAmHarm;
import com.xhb.core.entity.DataElectricity;
import com.xhb.core.entity.ElectricityType;
import com.xhb.core.entity.ReceiptCircuit;
import com.xhb.core.entity.ReceiptCollector;
import com.xhb.core.entity.ReceiptMeter;



public class MeterXinhongboHarmonic extends AbstractDevice {

	private int index;

//	private String time;
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
	private double kva;
	private double kvaA;
	private double kvaB;
	private double kvaC;
	private double powerFactor;
	private double powerFactorA;
	private double powerFactorB;
	private double powerFactorC;
	private double kwh;
	private double kwhForward;
	private double kwhReverse;
	private double kvarh1;
	private double kvarh2;
	
	private Integer Ubb = 1;
	private Integer Ibb = 1;

	private double distortionVoltageA;
	private double distortionVoltageB;
	private double distortionVoltageC;
	private double distortionVoltage;
	private double distortionCurrentA;
	private double distortionCurrentB;
	private double distortionCurrentC;
	private double distortionCurrent;

	private double[] ratioVoltageA = new double[30];
	private double oddDistortionVoltageA;
	private double evenDistortionVoltageA;

	private double[] ratioVoltageB = new double[30];
	private double oddDistortionVoltageB;
	private double evenDistortionVoltageB;

	private double[] ratioVoltageC = new double[30];
	private double oddDistortionVoltageC;
	private double evenDistortionVoltageC;

	private double[] ratioCurrentA = new double[30];
	private double oddDistortionCurrentA;
	private double evenDistortionCurrentA;

	private double[] ratioCurrentB = new double[30];
	private double oddDistortionCurrentB;
	private double evenDistortionCurrentB;

	private double[] ratioCurrentC = new double[30];
	private double oddDistortionCurrentC;
	private double evenDistortionCurrentC;

	public MeterXinhongboHarmonic(ReceiptCollector receiptCollector, ReceiptMeter receiptMeter) {
		this.receiptCollector = receiptCollector;
		this.receiptMeter = receiptMeter;
		buildWritingFrames();
	}

	@Override
	public void buildWritingFrames() {
		makeFrame1();
		makeFrame2();
		makeFrame3();
		makeFrame4();
		index = 0;
	}

	private void makeFrame1() {
		int[] data = new int[8];
		data[0] = Integer.parseInt(receiptMeter.getMeterNo());
		data[1] = 0x03;
		data[2] = 0x10;
		data[3] = 0x00;
		data[4] = 0x00;
		data[5] = 0x26;
		int[] crc = CRC.calculateCRC(data, 6);
		data[6] = crc[0];
		data[7] = crc[1];
		byte[] frame = new byte[data.length];
		for (int i = 0; i < data.length; i++) {
			frame[i] = (byte) data[i];
		}
		writingFrames.add(frame);
	}

	private void makeFrame2() {
		int[] data = new int[8];
		data[0] = Integer.parseInt(receiptMeter.getMeterNo());
		data[1] = 0x03;
		data[2] = 0x60;
		data[3] = 0x00;
		data[4] = 0x00;
		data[5] = 0x4C;
		int[] crc = CRC.calculateCRC(data, 6);
		data[6] = crc[0];
		data[7] = crc[1];
		byte[] frame = new byte[data.length];
		for (int i = 0; i < data.length; i++) {
			frame[i] = (byte) data[i];
		}
		writingFrames.add(frame);
	}

	private void makeFrame3() {
		int[] data = new int[8];
		data[0] = Integer.parseInt(receiptMeter.getMeterNo());
		data[1] = 0x03;
		data[2] = 0x60;
		data[3] = 0x4C;
		data[4] = 0x00;
		data[5] = 0x43;
		int[] crc = CRC.calculateCRC(data, 6);
		data[6] = crc[0];
		data[7] = crc[1];
		byte[] frame = new byte[data.length];
		for (int i = 0; i < data.length; i++) {
			frame[i] = (byte) data[i];
		}
		writingFrames.add(frame);
	}

	private void makeFrame4() {
		int[] data = new int[8];
		data[0] = Integer.parseInt(receiptMeter.getMeterNo());
		data[1] = 0x03;
		data[2] = 0x60;
		data[3] = 0x8F;
		data[4] = 0x00;
		data[5] = 0x42;
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
		if (readingFrames.size() != 4) {
			return false;
		}

		int[] data = new int[frame.length - 9];
		for (int i = 0; i < data.length; i++) {
			data[i] = frame[i + 8] & 0xFF;
		}

		if (!CRC.isValid(data))
			return false;

		if (index == 0) {
			analyzeFrame1(data);
		}
		if (index == 1) {
			analyzeFrame2(data);
		}
		if (index == 2) {
			analyzeFrame3(data);
		}
		if (index == 3) {
			analyzeFrame4(data);
		}
		index++;

		return true;
	}

	private void analyzeFrame1(int[] data) {
//		time = "20";
//		for (int i = 5; i < 10; i++) {
//			if (data[i] < 0x10)
//				time += "0" + Integer.toHexString(data[i]);
//			else
//				time += Integer.toHexString(data[i]);
//		}
		frequency = (data[11] * 256 + data[12]) / 100.0;
		voltageA = (data[13] * 256 + data[14]) / 10.0;
		voltageB = (data[15] * 256 + data[16]) / 10.0;
		voltageC = (data[17] * 256 + data[18]) / 10.0;
		currentA = (data[19] * 256 + data[20]) / 100.0;
		currentB = (data[21] * 256 + data[22]) / 100.0;
		currentC = (data[23] * 256 + data[24]) / 100.0;
		kw = (data[25] * 256 + data[26]) / 100.0;
		kwA = (data[27] * 256 + data[28]) / 100.0;
		kwB = (data[29] * 256 + data[30]) / 100.0;
		kwC = (data[31] * 256 + data[32]) / 100.0;
		kvar = (data[33] * 256 + data[34]) / 100.0;
		kvarA = (data[35] * 256 + data[36]) / 100.0;
		kvarB = (data[37] * 256 + data[38]) / 100.0;
		kvarC = (data[39] * 256 + data[40]) / 100.0;
		kva = (data[41] * 256 + data[42]) / 100.0;
		kvaA = (data[43] * 256 + data[44]) / 100.0;
		kvaB = (data[45] * 256 + data[46]) / 100.0;
		kvaC = (data[47] * 256 + data[48]) / 100.0;
		powerFactor = (data[49] * 256 + data[50]) / 100.0;
		powerFactorA = (data[51] * 256 + data[52]) / 100.0;
		powerFactorB = (data[53] * 256 + data[54]) / 100.0;
		powerFactorC = (data[55] * 256 + data[56]) / 100.0;
		kwh = (data[59] * 256 * 256 * 256 + data[60] * 256 * 256 + data[61] * 256 + data[62]) / 100.0;
		kwhForward = (data[63] * 256 * 256 * 256 + data[64] * 256 * 256 + data[65] * 256 + data[66]) / 100.0;
		kwhReverse = (data[67] * 256 * 256 * 256 + data[68] * 256 * 256 + data[69] * 256 + data[70]) / 100.0;
		kvarh1 = (data[71] * 256 * 256 * 256 + data[72] * 256 * 256 + data[73] * 256 + data[74]) / 100.0;
		kvarh2 = (data[75] * 256 * 256 * 256 + data[76] * 256 * 256 + data[77] * 256 + data[78]) / 100.0;
	}

	private void analyzeFrame2(int[] data) {
		distortionVoltageA = (data[3] * 256 + data[4]) / 100.0;
		distortionVoltageB = (data[5] * 256 + data[6]) / 100.0;
		distortionVoltageC = (data[7] * 256 + data[8]) / 100.0;
		distortionVoltage = (data[9] * 256 + data[10]) / 100.0;
		distortionCurrentA = (data[11] * 256 + data[12]) / 100.0;
		distortionCurrentB = (data[13] * 256 + data[14]) / 100.0;
		distortionCurrentC = (data[15] * 256 + data[16]) / 100.0;
		distortionCurrent = (data[17] * 256 + data[18]) / 100.0;
		for (int i = 0; i < ratioVoltageA.length; i++) {
			ratioVoltageA[i] = (data[i * 2 + 19] * 256 + data[i * 2 + 20]) / 100.0;
		}
		oddDistortionVoltageA = (data[79] * 256 + data[80]) / 100.0;
		evenDistortionVoltageA = (data[81] * 256 + data[82]) / 100.0;
		for (int i = 0; i < ratioVoltageB.length; i++) {
			ratioVoltageB[i] = (data[i * 2 + 87] * 256 + data[i * 2 + 88]) / 100.0;
		}
		oddDistortionVoltageB = (data[147] * 256 + data[148]) / 100.0;
		evenDistortionVoltageB = (data[149] * 256 + data[150]) / 100.0;
	}

	private void analyzeFrame3(int[] data) {
		for (int i = 0; i < ratioVoltageC.length; i++) {
			ratioVoltageC[i] = (data[i * 2 + 3] * 256 + data[i * 2 + 4]) / 100.0;
		}
		oddDistortionVoltageC = (data[63] * 256 + data[64]) / 100.0;
		evenDistortionVoltageC = (data[65] * 256 + data[66]) / 100.0;
		for (int i = 0; i < ratioCurrentA.length; i++) {
			ratioCurrentA[i] = (data[i * 2 + 71] * 256 + data[i * 2 + 72]) / 100.0;
		}
		oddDistortionCurrentA = (data[131] * 256 + data[132]) / 100.0;
		evenDistortionCurrentA = (data[133] * 256 + data[134]) / 100.0;
	}

	private void analyzeFrame4(int[] data) {
		for (int i = 0; i < ratioCurrentB.length; i++) {
			ratioCurrentB[i] = (data[i * 2 + 3] * 256 + data[i * 2 + 4]) / 100.0;
		}
		oddDistortionCurrentB = (data[63] * 256 + data[64]) / 100.0;
		evenDistortionCurrentB = (data[65] * 256 + data[66]) / 100.0;
		for (int i = 0; i < ratioCurrentC.length; i++) {
			ratioCurrentC[i] = (data[i * 2 + 69] * 256 + data[i * 2 + 70]) / 100.0;
		}
		oddDistortionCurrentC = (data[129] * 256 + data[130]) / 100.0;
		evenDistortionCurrentC = (data[131] * 256 + data[132]) / 100.0;
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
			dataElectricity.setKva(kva);
			dataElectricity.setKvaA(kvaA);
			dataElectricity.setKvaB(kvaB);
			dataElectricity.setKvaC(kvaC);
			dataElectricity.setPowerFactor(powerFactor);
			dataElectricity.setPowerFactorA(powerFactorA);
			dataElectricity.setPowerFactorB(powerFactorB);
			dataElectricity.setPowerFactorC(powerFactorC);
			dataElectricity.setKwh(kwh);
			dataElectricity.setKwhForward(kwhForward);
			dataElectricity.setKwhReverse(kwhReverse);
			dataElectricity.setKvarh1(kvarh1);
			dataElectricity.setKvarh2(kvarh2);
			services.dataElectricityService.save(dataElectricity);		
			DataAmHarm dah = new DataAmHarm();
			dah.setDataElectricity(dataElectricity);
			dah.setDistorIa(distortionCurrentA);
			dah.setDistorIb(distortionCurrentB);
			dah.setDistorIc(distortionCurrentC);
			dah.setDistorUa(distortionVoltageA);
			dah.setDistorUb(distortionVoltageB);
			dah.setDistorUc(distortionVoltageC);
			dah.setEvenHarmIa(evenDistortionCurrentA);
			dah.setEvenHarmIb(evenDistortionCurrentB);
			dah.setEvenHarmIc(evenDistortionCurrentC);
			dah.setEvenHarmUa(evenDistortionVoltageA);
			dah.setEvenHarmUb(evenDistortionVoltageB);
			dah.setEvenHarmUc(evenDistortionVoltageC);
			dah.setOddHarmIa(oddDistortionCurrentA);
			dah.setOddHarmIb(oddDistortionCurrentB);
			dah.setOddHarmIc(oddDistortionCurrentC);
			dah.setOddHarmUa(oddDistortionVoltageA);
			dah.setOddHarmUb(oddDistortionVoltageB);
			dah.setOddHarmUc(oddDistortionVoltageC);
//			dah.setInsertTime(now);
//			dah.setReceiptCircuit(receiptCircuit);
			dah.setTotalCurrentDistor(distortionCurrent);
			dah.setTotalVoltDistor(distortionVoltage);
			services.dataAmHarmService.save(dah);
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
		kva *= Ubb * Ibb;
		kvaA *= Ubb * Ibb;
		kvaB *= Ubb * Ibb;
		kvaC *= Ubb * Ibb;
	
	}

}
