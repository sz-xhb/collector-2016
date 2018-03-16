package com.xhb.sockserv.meter;

import java.math.BigDecimal;
import java.util.Date;

import com.xhb.core.entity.DataLingbu;
import com.xhb.core.entity.ReceiptCircuit;
import com.xhb.core.entity.ReceiptCollector;
import com.xhb.core.entity.ReceiptMeter;


public class ElectricFilter extends AbstractDevice {

	private double busvoltageA;
	private double busvoltageB;
	private double busvoltageC;
	private double apfvoltage;
	private double apfcurrentA;
	private double apfcurrentB;
	private double apfcurrentC;
	private double apfcurrentN;
	private double buscurrentA;
	private double buscurrentB;
	private double buscurrentC;
	private double distortionCurrent;
	private double apparentPower;
	private double activePower;
	private double reactivePower;
	private double powerFactor;
	private int dcProtect;
	private int overVoltageProtect;
	private int underVoltageProtect;
	private int tempProtect;
	private int currentProtect;
	private int resetSwitch;
	private int startSwitch;
	private int closecontrolSwitch;

	public ElectricFilter(ReceiptCollector receiptCollector, ReceiptMeter receiptMeter) {
		this.receiptCollector = receiptCollector;
		this.receiptMeter = receiptMeter;
		buildWritingFrames();
	}

	@Override
	public void buildWritingFrames() {
		makeFrame();
	}

	private void makeFrame() {
		int[] data = new int[8];
		data[0] = Integer.parseInt(receiptMeter.getMeterNo());
		data[1] = 0x03;
		data[2] = 0x00;
		data[3] = 0x00;
		data[4] = 0x00;
		data[5] = 0x30;
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

		if (!CRC.isValid(data))
			return false;

		if (data.length != 101)
			return false;
		
		if(data[0] == Integer.parseInt(receiptMeter.getMeterNo())){
			analyzeFrame(data);
		}else{
			return false;
		}
		return true;
	}

	private void analyzeFrame(int[] data) {
		busvoltageA = floatIeeeConvert(data, 3);
		busvoltageB = floatIeeeConvert(data, 7);
		busvoltageC = floatIeeeConvert(data, 11);
		apfvoltage = floatIeeeConvert(data, 15);
		apfcurrentA = floatIeeeConvert(data, 19);
		apfcurrentB = floatIeeeConvert(data, 23);
		apfcurrentC = floatIeeeConvert(data, 27);
		apfcurrentN = floatIeeeConvert(data, 31);
		buscurrentA = floatIeeeConvert(data, 35);
		buscurrentB = floatIeeeConvert(data, 39);
		buscurrentC = floatIeeeConvert(data, 43);
		distortionCurrent = floatIeeeConvert(data, 47);
		apparentPower = floatIeeeConvert(data, 51);
		activePower = floatIeeeConvert(data, 55);
		reactivePower = floatIeeeConvert(data, 59);
		powerFactor = floatIeeeConvert(data, 63);
		dcProtect = (int) floatIeeeConvert(data, 67);
		overVoltageProtect = (int) floatIeeeConvert(data, 71);
		underVoltageProtect = (int) floatIeeeConvert(data, 75);
		tempProtect = (int) floatIeeeConvert(data, 79);
		currentProtect = (int) floatIeeeConvert(data, 83);
		resetSwitch = (int) floatIeeeConvert(data, 87);
		startSwitch = (int) floatIeeeConvert(data, 91);
		closecontrolSwitch = (int) floatIeeeConvert(data, 95);
	}

	private double floatIeeeConvert(int[] data, int index) {
		int s = 1;

		if ((data[index] & 0x80) == 0x80) {
			s = -1;
		}

		int e = ((data[index] & 0x7f) << 1) + ((data[index + 1] & 0x80) >> 7);
		double m = 0;

		if (e == 0) {
			e = 1 - e;
			m = ((data[index + 1] & 0x7f) * 256 * 256 + data[index + 2] * 256 + data[index + 3]) * 1.0 / (2 << 22);
		} else {
			e = e - 127;
			m = (((data[index + 1] & 0x7f) + 0x80) * 256 * 256 + data[index + 2] * 256 + data[index + 3]) * 1.0 / (2 << 22);
		}

		double n = s * Math.pow(2, e) * m;
		BigDecimal b = new BigDecimal(n);
		n = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

		return n;
	}

	@Override
	public void handleResult() {
		Date now = new Date();
		ReceiptCircuit receiptCircuit = services.receiptCircuitService
				.findCircuitByDtuNoAndMeterNoAndLoopNo(receiptCollector.getCollectorNo(), receiptMeter.getMeterNo(), 1);
		DataLingbu dataLingbu = new DataLingbu();
		dataLingbu.setReceiptCircuit(receiptCircuit);
		dataLingbu.setReadTime(now);
		dataLingbu.setSuppVoltageA(busvoltageA);
		dataLingbu.setSuppVoltageB(busvoltageB);
		dataLingbu.setSuppVoltageC(busvoltageC);
		dataLingbu.setApfVoltageDc(apfvoltage);
		dataLingbu.setApfCurrentA(apfcurrentA);
		dataLingbu.setApfCurrentB(apfcurrentB);
		dataLingbu.setApfCurrentC(apfcurrentC);
		dataLingbu.setApfCurrentN(apfcurrentN);
		dataLingbu.setLoadCurrentA(buscurrentA);
		dataLingbu.setLoadCurrentB(buscurrentB);
		dataLingbu.setLoadCurrentC(buscurrentC);
		dataLingbu.setDistortionCurrent(distortionCurrent);
		dataLingbu.setKva(apparentPower);
		dataLingbu.setKw(activePower);
		dataLingbu.setKvar(reactivePower);
		dataLingbu.setPowerFactor(powerFactor);
		dataLingbu.setDcVoltageProtect(dcProtect);
		dataLingbu.setOverVoltageProtect(overVoltageProtect);
		dataLingbu.setUnderVoltageProtect(underVoltageProtect);
		dataLingbu.setTemperatureProtect(tempProtect);
		dataLingbu.setCurrentProtect(currentProtect);
		dataLingbu.setResetSwitch(resetSwitch);
		dataLingbu.setStartSwitch(startSwitch);
		dataLingbu.setControlSwitch(closecontrolSwitch);
		services.dataLingbuService.save(dataLingbu);
	}
}
