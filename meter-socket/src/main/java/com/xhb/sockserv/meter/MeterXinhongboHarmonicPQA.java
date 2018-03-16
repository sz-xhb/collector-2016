package com.xhb.sockserv.meter;

import java.util.Date;
import java.util.List;

import com.xhb.core.entity.DataAmHarm;
import com.xhb.core.entity.DataElectricity;
import com.xhb.core.entity.ElectricityType;
import com.xhb.core.entity.ReceiptCircuit;
import com.xhb.core.entity.ReceiptCollector;
import com.xhb.core.entity.ReceiptMeter;


public class MeterXinhongboHarmonicPQA extends AbstractDevice {

	private double kwhForward;
	private double kwhReverse;
	private double kvarh1;
	private double kvarh2;
	private double kwh;

	private double frequency;
	private double voltageA;
	private double voltageB;
	private double voltageC;
	private double voltageAB;
	private double voltageBC;
	private double voltageCA;
	private double currentA;
	private double currentB;
	private double currentC;
	private double kwA;
	private double kwB;
	private double kwC;
	private double kw;
	private double kvarA;
	private double kvarB;
	private double kvarC;
	private double kvar;
	private double kvaA;
	private double kvaB;
	private double kvaC;
	private double kva;
	private double powerFactorA;
	private double powerFactorB;
	private double powerFactorC;
	private double powerFactor;
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

	public MeterXinhongboHarmonicPQA(ReceiptCollector receiptCollector, ReceiptMeter receiptMeter) {
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
		makeFrame5();
	}

	private void makeFrame1() {
		int[] data = new int[8];
		data[0] = Integer.parseInt(receiptMeter.getMeterNo());
		data[1] = 0x03;
		data[2] = 0x00;
		data[3] = 0x20;
		data[4] = 0x00;
		data[5] = 0x10;
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
		data[2] = 0x01;
		data[3] = 0x00;
		data[4] = 0x00;
		data[5] = 0x24;
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
		data[2] = 0x01;
		data[3] = 0x24;
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

	private void makeFrame4() {
		int[] data = new int[8];
		data[0] = Integer.parseInt(receiptMeter.getMeterNo());
		data[1] = 0x03;
		data[2] = 0x01;
		data[3] = 0x92;
		data[4] = 0x00;
		data[5] = 0x63;
		int[] crc = CRC.calculateCRC(data, 6);
		data[6] = crc[0];
		data[7] = crc[1];
		byte[] frame = new byte[data.length];
		for (int i = 0; i < data.length; i++) {
			frame[i] = (byte) data[i];
		}
		writingFrames.add(frame);
	}

	private void makeFrame5() {
		int[] data = new int[8];
		data[0] = Integer.parseInt(receiptMeter.getMeterNo());
		data[1] = 0x03;
		data[2] = 0x01;
		data[3] = 0x70;
		data[4] = 0x00;
		data[5] = 0x22;
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
		if (readingFrames.size() != 5) {
			return false;
		}

		int[] data = new int[frame.length - 9];
		for (int i = 0; i < data.length; i++) {
			data[i] = frame[i + 8] & 0xFF;
		}

		if (!CRC.isValid(data))
			return false;

		if (data[2] == 0x20) {
			analyzeFrame1(data);
		}
		if (data[2] == 0x48) {
			analyzeFrame2(data);
		}
		if (data[2] == 0x98) {
			analyzeFrame3(data);
		}
		if (data[2] == 0xc6) {
			analyzeFrame4(data);
		}
		if (data[2] == 0x44) {
			analyzeFrame5(data);
		}

		return true;
	}
	
	private int valueCalculate(int hi, int lo){
		 int value = 0;
		 
		 if(((byte)hi & 0x80) == 0x80)
			 value = (((byte)hi & 0x7F) * 256 + lo) * (-1);
		 else
			 value = hi * 256 + lo;
			 
		 return value;
		 
	}

	private void analyzeFrame1(int[] data) {
		kwhForward = (data[3] * 256 * 256 * 256 + data[4] * 256 * 256 + data[5] * 256 + data[6]) / 100.0;
		kwhReverse = (data[7] * 256 * 256 * 256 + data[8] * 256 * 256 + data[9] * 256 + data[10]) / 100.0;
		kvarh1 = (data[11] * 256 * 256 * 256 + data[12] * 256 * 256 + data[13] * 256 + data[14]) / 100.0;
		kvarh2 = (data[15] * 256 * 256 * 256 + data[16] * 256 * 256 + data[17] * 256 + data[18]) / 100.0;
		kwh = (data[19] * 256 * 256 * 256 + data[20] * 256 * 256 + data[21] * 256 + data[22]) / 100.0;
	}

	private void analyzeFrame2(int[] data) {
		frequency = (data[3] * 256 + data[4]) / 100.0;
		voltageA = (data[5] * 256 + data[6]) / 10.0;
		voltageB = (data[7] * 256 + data[8]) / 10.0;
		voltageC = (data[9] * 256 + data[10]) / 10.0;
		voltageAB = (data[13] * 256 + data[14]) / 10.0;
		voltageBC = (data[15] * 256 + data[16]) / 10.0;
		voltageCA = (data[17] * 256 + data[18]) / 10.0;
		currentA = valueCalculate(data[21], data[22])/ 100.0;
		currentB = valueCalculate(data[23], data[24])/ 100.0;
		currentC = valueCalculate(data[25], data[26])/ 100.0;
		kwA = valueCalculate(data[31], data[32])/ 1000.0;
		kwB = valueCalculate(data[33], data[34])/ 1000.0;
		kwC = valueCalculate(data[35], data[36])/ 1000.0;
		kw = valueCalculate(data[37], data[38])/ 1000.0;
		kvarA = valueCalculate(data[39], data[40])/ 1000.0;
		kvarB = valueCalculate(data[41], data[42])/ 1000.0;
		kvarC = valueCalculate(data[43], data[44])/ 1000.0;
		kvar = valueCalculate(data[45], data[46])/ 1000.0;
		kvaA = valueCalculate(data[47], data[48])/ 1000.0;
		kvaB = valueCalculate(data[49], data[50])/ 1000.0;
		kvaC = valueCalculate(data[51], data[52])/ 1000.0;
		kva = valueCalculate(data[53], data[54])/ 1000.0;
		powerFactorA = valueCalculate(data[55], data[56])/ 1000.0;
		powerFactorB = valueCalculate(data[57], data[58])/ 1000.0;
		powerFactorC = valueCalculate(data[59], data[60])/ 1000.0;
		powerFactor = valueCalculate(data[61], data[62])/ 1000.0;
	}

	private void analyzeFrame3(int[] data) {
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
	
	private void analyzeFrame5(int[] data) {
		for (int i = 0; i < ratioVoltageC.length; i++) {
			ratioVoltageC[i] = (data[i * 2 + 3] * 256 + data[i * 2 + 4]) / 100.0;
		}
		oddDistortionVoltageC = (data[63] * 256 + data[64]) / 100.0;
		evenDistortionVoltageC = (data[65] * 256 + data[66]) / 100.0;
	}

	private void analyzeFrame4(int[] data) {
		for (int i = 0; i < ratioCurrentA.length; i++) {
			ratioCurrentA[i] = (data[i * 2 + 3] * 256 + data[i * 2 + 4]) / 100.0;
		}
		oddDistortionCurrentA = (data[63] * 256 + data[64]) / 100.0;
		evenDistortionCurrentA = (data[65] * 256 + data[66]) / 100.0;
		for (int i = 0; i < ratioCurrentB.length; i++) {
			ratioCurrentB[i] = (data[i * 2 + 69] * 256 + data[i * 2 + 70]) / 100.0;
		}
		oddDistortionCurrentB = (data[129] * 256 + data[130]) / 100.0;
		evenDistortionCurrentB = (data[131] * 256 + data[132]) / 100.0;
		for (int i = 0; i < ratioCurrentC.length; i++) {
			ratioCurrentC[i] = (data[i * 2 + 135] * 256 + data[i * 2 + 136]) / 100.0;
		}
		oddDistortionCurrentC = (data[195] * 256 + data[196]) / 100.0;
		evenDistortionCurrentC = (data[197] * 256 + data[198]) / 100.0;
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
		voltageAB *= Ubb;
		voltageCA *= Ubb;
		voltageBC *= Ubb;
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
			dataElectricity.setVoltageAB(voltageAB);
			dataElectricity.setVoltageBC(voltageBC);
			dataElectricity.setVoltageCA(voltageCA);
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
			dataElectricity.setKwhForward(kwhForward);
			dataElectricity.setKwhReverse(kwhReverse);
			dataElectricity.setKvarh1(kvarh1);
			dataElectricity.setKvarh2(kvarh2);
			dataElectricity.setKwh(kwh);
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
			dah.setHarmUa2(ratioVoltageA[0]);
			dah.setHarmUa3(ratioVoltageA[1]);
			dah.setHarmUa4(ratioVoltageA[2]);
			dah.setHarmUa5(ratioVoltageA[3]);
			dah.setHarmUa6(ratioVoltageA[4]);
			dah.setHarmUa7(ratioVoltageA[5]);
			dah.setHarmUa8(ratioVoltageA[6]);
			dah.setHarmUa9(ratioVoltageA[7]);
			dah.setHarmUa10(ratioVoltageA[8]);
			dah.setHarmUa11(ratioVoltageA[9]);
			dah.setHarmUa12(ratioVoltageA[10]);
			dah.setHarmUa13(ratioVoltageA[11]);
			dah.setHarmUa14(ratioVoltageA[12]);
			dah.setHarmUa15(ratioVoltageA[13]);
			dah.setHarmUa16(ratioVoltageA[14]);
			dah.setHarmUa17(ratioVoltageA[15]);
			dah.setHarmUa18(ratioVoltageA[16]);
			dah.setHarmUa19(ratioVoltageA[17]);
			dah.setHarmUa20(ratioVoltageA[18]);
			dah.setHarmUa21(ratioVoltageA[19]);
			dah.setHarmUa22(ratioVoltageA[20]);
			dah.setHarmUa23(ratioVoltageA[21]);
			dah.setHarmUa24(ratioVoltageA[22]);
			dah.setHarmUa25(ratioVoltageA[23]);
			dah.setHarmUa26(ratioVoltageA[24]);
			dah.setHarmUa27(ratioVoltageA[25]);
			dah.setHarmUa28(ratioVoltageA[26]);
			dah.setHarmUa29(ratioVoltageA[27]);
			dah.setHarmUa30(ratioVoltageA[28]);
			dah.setHarmUa31(ratioVoltageA[29]);
			dah.setHarmUb2(ratioVoltageB[0]);
			dah.setHarmUb3(ratioVoltageB[1]);
			dah.setHarmUb4(ratioVoltageB[2]);
			dah.setHarmUb5(ratioVoltageB[3]);
			dah.setHarmUb6(ratioVoltageB[4]);
			dah.setHarmUb7(ratioVoltageB[5]);
			dah.setHarmUb8(ratioVoltageB[6]);
			dah.setHarmUb9(ratioVoltageB[7]);
			dah.setHarmUb10(ratioVoltageB[8]);
			dah.setHarmUb11(ratioVoltageB[9]);
			dah.setHarmUb12(ratioVoltageB[10]);
			dah.setHarmUb13(ratioVoltageB[11]);
			dah.setHarmUb14(ratioVoltageB[12]);
			dah.setHarmUb15(ratioVoltageB[13]);
			dah.setHarmUb16(ratioVoltageB[14]);
			dah.setHarmUb17(ratioVoltageB[15]);
			dah.setHarmUb18(ratioVoltageB[16]);
			dah.setHarmUb19(ratioVoltageB[17]);
			dah.setHarmUb20(ratioVoltageB[18]);
			dah.setHarmUb21(ratioVoltageB[19]);
			dah.setHarmUb22(ratioVoltageB[20]);
			dah.setHarmUb23(ratioVoltageB[21]);
			dah.setHarmUb24(ratioVoltageB[22]);
			dah.setHarmUb25(ratioVoltageB[23]);
			dah.setHarmUb26(ratioVoltageB[24]);
			dah.setHarmUb27(ratioVoltageB[25]);
			dah.setHarmUb28(ratioVoltageB[26]);
			dah.setHarmUb29(ratioVoltageB[27]);
			dah.setHarmUb30(ratioVoltageB[28]);
			dah.setHarmUb31(ratioVoltageB[29]);
			dah.setHarmUc2(ratioVoltageC[0]);
			dah.setHarmUc3(ratioVoltageC[1]);
			dah.setHarmUc4(ratioVoltageC[2]);
			dah.setHarmUc5(ratioVoltageC[3]);
			dah.setHarmUc6(ratioVoltageC[4]);
			dah.setHarmUc7(ratioVoltageC[5]);
			dah.setHarmUc8(ratioVoltageC[6]);
			dah.setHarmUc9(ratioVoltageC[7]);
			dah.setHarmUc10(ratioVoltageC[8]);
			dah.setHarmUc11(ratioVoltageC[9]);
			dah.setHarmUc12(ratioVoltageC[10]);
			dah.setHarmUc13(ratioVoltageC[11]);
			dah.setHarmUc14(ratioVoltageC[12]);
			dah.setHarmUc15(ratioVoltageC[13]);
			dah.setHarmUc16(ratioVoltageC[14]);
			dah.setHarmUc17(ratioVoltageC[15]);
			dah.setHarmUc18(ratioVoltageC[16]);
			dah.setHarmUc19(ratioVoltageC[17]);
			dah.setHarmUc20(ratioVoltageC[18]);
			dah.setHarmUc21(ratioVoltageC[19]);
			dah.setHarmUc22(ratioVoltageC[20]);
			dah.setHarmUc23(ratioVoltageC[21]);
			dah.setHarmUc24(ratioVoltageC[22]);
			dah.setHarmUc25(ratioVoltageC[23]);
			dah.setHarmUc26(ratioVoltageC[24]);
			dah.setHarmUc27(ratioVoltageC[25]);
			dah.setHarmUc28(ratioVoltageC[26]);
			dah.setHarmUc29(ratioVoltageC[27]);
			dah.setHarmUc30(ratioVoltageC[28]);
			dah.setHarmUc31(ratioVoltageC[29]);
			dah.setHarmIa2(ratioCurrentA[0]);
			dah.setHarmIa3(ratioCurrentA[1]);
			dah.setHarmIa4(ratioCurrentA[2]);
			dah.setHarmIa5(ratioCurrentA[3]);
			dah.setHarmIa6(ratioCurrentA[4]);
			dah.setHarmIa7(ratioCurrentA[5]);
			dah.setHarmIa8(ratioCurrentA[6]);
			dah.setHarmIa9(ratioCurrentA[7]);
			dah.setHarmIa10(ratioCurrentA[8]);
			dah.setHarmIa11(ratioCurrentA[9]);
			dah.setHarmIa12(ratioCurrentA[10]);
			dah.setHarmIa13(ratioCurrentA[11]);
			dah.setHarmIa14(ratioCurrentA[12]);
			dah.setHarmIa15(ratioCurrentA[13]);
			dah.setHarmIa16(ratioCurrentA[14]);
			dah.setHarmIa17(ratioCurrentA[15]);
			dah.setHarmIa18(ratioCurrentA[16]);
			dah.setHarmIa19(ratioCurrentA[17]);
			dah.setHarmIa20(ratioCurrentA[18]);
			dah.setHarmIa21(ratioCurrentA[19]);
			dah.setHarmIa22(ratioCurrentA[20]);
			dah.setHarmIa23(ratioCurrentA[21]);
			dah.setHarmIa24(ratioCurrentA[22]);
			dah.setHarmIa25(ratioCurrentA[23]);
			dah.setHarmIa26(ratioCurrentA[24]);
			dah.setHarmIa27(ratioCurrentA[25]);
			dah.setHarmIa28(ratioCurrentA[26]);
			dah.setHarmIa29(ratioCurrentA[27]);
			dah.setHarmIa30(ratioCurrentA[28]);
			dah.setHarmIa31(ratioCurrentA[29]);
			dah.setHarmIb2(ratioCurrentB[0]);
			dah.setHarmIb3(ratioCurrentB[1]);
			dah.setHarmIb4(ratioCurrentB[2]);
			dah.setHarmIb5(ratioCurrentB[3]);
			dah.setHarmIb6(ratioCurrentB[4]);
			dah.setHarmIb7(ratioCurrentB[5]);
			dah.setHarmIb8(ratioCurrentB[6]);
			dah.setHarmIb9(ratioCurrentB[7]);
			dah.setHarmIb10(ratioCurrentB[8]);
			dah.setHarmIb11(ratioCurrentB[9]);
			dah.setHarmIb12(ratioCurrentB[10]);
			dah.setHarmIb13(ratioCurrentB[11]);
			dah.setHarmIb14(ratioCurrentB[12]);
			dah.setHarmIb15(ratioCurrentB[13]);
			dah.setHarmIb16(ratioCurrentB[14]);
			dah.setHarmIb17(ratioCurrentB[15]);
			dah.setHarmIb18(ratioCurrentB[16]);
			dah.setHarmIb19(ratioCurrentB[17]);
			dah.setHarmIb20(ratioCurrentB[18]);
			dah.setHarmIb21(ratioCurrentB[19]);
			dah.setHarmIb22(ratioCurrentB[20]);
			dah.setHarmIb23(ratioCurrentB[21]);
			dah.setHarmIb24(ratioCurrentB[22]);
			dah.setHarmIb25(ratioCurrentB[23]);
			dah.setHarmIb26(ratioCurrentB[24]);
			dah.setHarmIb27(ratioCurrentB[25]);
			dah.setHarmIb28(ratioCurrentB[26]);
			dah.setHarmIb29(ratioCurrentB[27]);
			dah.setHarmIb30(ratioCurrentB[28]);
			dah.setHarmIb31(ratioCurrentB[29]);
			dah.setHarmIc2(ratioCurrentC[0]);
			dah.setHarmIc3(ratioCurrentC[1]);
			dah.setHarmIc4(ratioCurrentC[2]);
			dah.setHarmIc5(ratioCurrentC[3]);
			dah.setHarmIc6(ratioCurrentC[4]);
			dah.setHarmIc7(ratioCurrentC[5]);
			dah.setHarmIc8(ratioCurrentC[6]);
			dah.setHarmIc9(ratioCurrentC[7]);
			dah.setHarmIc10(ratioCurrentC[8]);
			dah.setHarmIc11(ratioCurrentC[9]);
			dah.setHarmIc12(ratioCurrentC[10]);
			dah.setHarmIc13(ratioCurrentC[11]);
			dah.setHarmIc14(ratioCurrentC[12]);
			dah.setHarmIc15(ratioCurrentC[13]);
			dah.setHarmIc16(ratioCurrentC[14]);
			dah.setHarmIc17(ratioCurrentC[15]);
			dah.setHarmIc18(ratioCurrentC[16]);
			dah.setHarmIc19(ratioCurrentC[17]);
			dah.setHarmIc20(ratioCurrentC[18]);
			dah.setHarmIc21(ratioCurrentC[19]);
			dah.setHarmIc22(ratioCurrentC[20]);
			dah.setHarmIc23(ratioCurrentC[21]);
			dah.setHarmIc24(ratioCurrentC[22]);
			dah.setHarmIc25(ratioCurrentC[23]);
			dah.setHarmIc26(ratioCurrentC[24]);
			dah.setHarmIc27(ratioCurrentC[25]);
			dah.setHarmIc28(ratioCurrentC[26]);
			dah.setHarmIc29(ratioCurrentC[27]);
			dah.setHarmIc30(ratioCurrentC[28]);
			dah.setHarmIc31(ratioCurrentC[29]);
			services.dataHarmonicService.save(dah);
		}
	}

}
