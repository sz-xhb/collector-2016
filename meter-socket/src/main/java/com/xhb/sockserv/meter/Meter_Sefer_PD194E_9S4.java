package com.xhb.sockserv.meter;


import java.util.Date;
import com.xhb.core.entity.DataElectricity;
import com.xhb.core.entity.ElectricityType;
import com.xhb.core.entity.ReceiptCircuit;
import com.xhb.core.entity.ReceiptCollector;
import com.xhb.core.entity.ReceiptMeter;

public class Meter_Sefer_PD194E_9S4 extends AbstractDevice {

	private int Ubb = 1;
	private int Ibb = 1;
	private double voltA;
	private double voltB;
	private double voltC;
	private double voltAB;
	private double voltBC;
	private double voltCA;
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
	private double powerFactor;
	private double frequency;
	private double kwh;
	private double kwhForward;
	private double kwhReverse;
	private double kvarh1;
	private double kvarh2;
	private int  index = 0;
	public Meter_Sefer_PD194E_9S4(ReceiptCollector receiptCollector,ReceiptMeter receiptMeter) {
		this.receiptCollector = receiptCollector;
		this.receiptMeter = receiptMeter;
		buildWritingFrames();
	}
	@Override
	public void buildWritingFrames() {
		index = 0;
		makeVoltRate();
		makeCurrentRate();
		makeFrame();
	}

	private void makeCurrentRate() {
		int[] data = new int[8];
		data[0] = Integer.parseInt(receiptMeter.getMeterNo());
		data[1] = 0x03;
		data[2] = 0x00;
		data[3] = 0x6C;
		data[4] = 0x00;
		data[5] = 0x01;
		int[] crc = CRC.calculateCRC(data, 6);
		data[6] = crc[0];
		data[7] = crc[1];
		byte[] frame = new byte[data.length];
		for (int i = 0; i < data.length; i++) {
			frame[i] = (byte) data[i];
		}
		writingFrames.add(frame);
	}
	private void makeVoltRate() {
		int[] data = new int[8];
		data[0] = Integer.parseInt(receiptMeter.getMeterNo());
		data[1] = 0x03;
		data[2] = 0x00;
		data[3] = 0x6B;
		data[4] = 0x00;
		data[5] = 0x01;
		int[] crc = CRC.calculateCRC(data, 6);
		data[6] = crc[0];
		data[7] = crc[1];
		byte[] frame = new byte[data.length];
		for (int i = 0; i < data.length; i++) {
			frame[i] = (byte) data[i];
		}
		writingFrames.add(frame);
	}
	private void makeFrame() {
		int[] data = new int[8];
		data[0] = Integer.parseInt(receiptMeter.getMeterNo());
		data[1] = 0x03;
		data[2] = 0x00;
		data[3] = 0x3D;
		data[4] = 0x00;
		data[5] = 0x1F;
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
		int[] data = new int[frame.length - 9];
		for (int i = 0; i < data.length; i++) {
			data[i] = frame[i + 8] & 0xFF;
		}
		if (!CRC.isValid(data)){
			return false;
		}
		index++;
		if(index == 1){
			Ubb = data[3] * 256 + data[4];
		}else if (index == 2) {
			Ibb = data[3] * 256 + data[4];
		}else {
			voltA = ( data[3] * 256 + data[4] )  / 10.0;
			voltB = ( data[5] * 256 + data[6] )  / 10.0;
			voltC = ( data[7] * 256 + data[8] )  / 10.0;
			voltAB = ( data[9] * 256 + data[10] )  / 10.0;
			voltBC = ( data[11] * 256 + data[12] )  / 10.0;
			voltCA = ( data[13] * 256 + data[14] )  / 10.0;
			currentA = ( data[15] * 256 + data[16] )  / 1000.0;
			currentB = ( data[17] * 256 + data[18] )  / 1000.0;
			currentC = ( data[19] * 256 + data[20] )  / 1000.0;
			kwA = doGetComplement( data[21] * 256 + data[22]) / 1000.0;
			kwB = doGetComplement( data[23] * 256 + data[24]) / 1000.0;
			kwC = doGetComplement( data[25] * 256 + data[26]) / 1000.0;
			kw = doGetComplement( data[27] * 256 + data[28] ) / 1000.0;
			kvarA = doGetComplement( data[29] * 256 + data[30] ) / 1000.0;
			kvarB = doGetComplement( data[31] * 256 + data[32] ) / 1000.0;
			kvarC = doGetComplement( data[33] * 256 + data[34] ) / 1000.0;
			kvar = doGetComplement( data[35] * 256 + data[36] ) / 1000.0;
			kvaA = doGetComplement( data[37] * 256 + data[38] ) / 1000.0;
			kvaB = doGetComplement( data[39] * 256 + data[40] ) / 1000.0;
			kvaC = doGetComplement( data[41] * 256 + data[42] ) / 1000.0;
			kva = doGetComplement( data[43] * 256 + data[44] ) / 1000.0;
			powerFactor = doGetComplement( data[45] * 256 + data[46] ) / 1000.0;
			frequency = ( data[47] * 256 + data[48] ) / 100.0;
			kwhForward = (data[49] * 256 * 256 * 256 + data[50] * 256 * 256 + data[51] * 256 + data[52]) / 1000.0;
			kwhReverse = (data[53] * 256 * 256 * 256 + data[54] * 256 * 256 + data[55] * 256 + data[56]) / 1000.0;
			kwh = kwhForward + kwhReverse;
			kvarh1 = (data[57] * 256 * 256 * 256 + data[58] * 256 * 256 + data[59] * 256 + data[60]) / 1000.0;
			kvarh2 = (data[61] * 256 * 256 * 256 + data[62] * 256 * 256 + data[63] * 256 + data[64]) / 1000.0;
			index = 0;
		}
		return true;
	}

	private int doGetComplement(int data){
		if((data & 0x8000) == 0x8000){
			return (short)data;
		}else{
			return data;
		}
	}
	private void resultMutiplyRate() {
		kwhForward = kwhForward * Ubb * Ibb;
		kwhReverse = kwhReverse * Ubb * Ibb;
		kvarh1 = kvarh1 * Ubb * Ibb ;
		kvarh2  = kvarh2 * Ubb * Ibb ;
		kwh  = kwh * Ubb * Ibb ;
		voltA *= Ubb;
		voltB *= Ubb;
		voltC *= Ubb;
		voltAB *= Ubb;
		voltBC *= Ubb;
		voltCA *= Ubb;
		currentA *= Ibb;
		currentB *= Ibb;
		currentC *= Ibb;
		kwA = kwA * Ubb * Ibb;
		kwB = kwB * Ubb * Ibb;
		kwC  = kwC * Ubb * Ibb;
		kw = kw * Ubb * Ibb;
		kvarA = kvarA * Ubb * Ibb;
		kvarB = kvarB * Ubb * Ibb;
		kvarC = kvarC * Ubb * Ibb;
		kvar = kvar * Ubb * Ibb;
		kvaA = kvaA * Ubb * Ibb;
		kvaB = kvaB * Ubb * Ibb;
		kvaC = kvaC * Ubb * Ibb;
		kva = kva * Ubb * Ibb;
	}
	@Override
	public void handleResult() {
		ReceiptCircuit receiptCircuit = services.receiptCircuitService.findCircuitByDtuNoAndMeterNoAndLoopNo(receiptCollector.getCollectorNo(), receiptMeter.getMeterNo(), 1);
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
		Date now = new Date();
		DataElectricity de = new DataElectricity();
		de.setReceiptCircuit(receiptCircuit);
		de.setReadTime(now);
		de.setFrequency(frequency);
		de.setVoltageA(voltA);
		de.setVoltageB(voltB);
		de.setVoltageC(voltC);
		de.setVoltageAB(voltAB);
		de.setVoltageBC(voltBC);
		de.setVoltageCA(voltCA);
		de.setCurrentA(currentA);
		de.setCurrentB(currentB);
		de.setCurrentC(currentC);
		de.setKw(kw);
		de.setKwA(kwA);
		de.setKwB(kwB);
		de.setKwC(kwC);
		de.setKvar(kvar);
		de.setKvarA(kvarA);
		de.setKvarB(kvarB);
		de.setKvarC(kvarC);
		de.setKva(kva);
		de.setKvaA(kvaA);
		de.setKvaB(kvaB);
		de.setKvaC(kvaC);
		de.setPowerFactor(powerFactor);
		de.setKwh(kwh);
		de.setKwhForward(kwhForward);
		de.setKwhReverse(kwhReverse);
		de.setKvarh1(kvarh1);
		de.setKvarh2(kvarh2);
		de.setElectricityType(ElectricityType.AC_THREE);
		services.dataElectricityService.save(de);
	}

}
