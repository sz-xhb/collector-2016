package com.xhb.sockserv.meter;

import java.util.Date;

import com.xhb.core.entity.DataElectricity;
import com.xhb.core.entity.ElectricityType;
import com.xhb.core.entity.ReceiptCircuit;
import com.xhb.core.entity.ReceiptCollector;
import com.xhb.core.entity.ReceiptMeter;
import com.xhb.sockserv.util.FrameUtils;

/*
 * 常州济中使用的斯菲尔200电表
 * @author jc 改编自CS中的协议 
 * @time 2016-04-12
 */
public class Meter_CHZJZ_SFER_200 extends AbstractDevice {

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
	public Meter_CHZJZ_SFER_200(ReceiptCollector receiptCollector, ReceiptMeter receiptMeter){
		this.receiptCollector = receiptCollector;
		this.receiptMeter = receiptMeter;
		buildWritingFrames();
	}
	@Override
	public void buildWritingFrames() {
		makeFrameOne();
		makeFrameTwo();
	}

	private void makeFrameOne() {
		int[] data = new int[8];
		data[0] = Integer.parseInt(receiptMeter.getMeterNo());
		data[1] = 0x03;
		data[2] = 0x00;
		data[3] = 0x06;
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
	private void makeFrameTwo() {
		int[] data = new int[8];
		data[0] = Integer.parseInt(receiptMeter.getMeterNo());
		data[1] = 0x03;
		data[2] = 0x00;
		data[3] = 0x20;
		data[4] = 0x00;
		data[5] = 0x16;
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
		if (!CRC.isValid(data))
			return false;
		if(data[2] == 0x34){
			voltageA = FrameUtils.floatIeeeConvert(data, 3);
			voltageB = FrameUtils.floatIeeeConvert(data, 7);
			voltageC = FrameUtils.floatIeeeConvert(data, 11);
			voltAB = FrameUtils.floatIeeeConvert(data, 15);
			voltBC = FrameUtils.floatIeeeConvert(data, 19);
			voltCA = FrameUtils.floatIeeeConvert(data, 23);
			currentA = FrameUtils.floatIeeeConvert(data, 27);
			currentB = FrameUtils.floatIeeeConvert(data, 31);
			currentC = FrameUtils.floatIeeeConvert(data, 35);
			kwA = FrameUtils.floatIeeeConvert(data, 39);
			kwB = FrameUtils.floatIeeeConvert(data, 43);
			kwC = FrameUtils.floatIeeeConvert(data, 47);
			kw = FrameUtils.floatIeeeConvert(data, 51);
		}
		else if(data[2] == 0x2C){
			kvarA = FrameUtils.floatIeeeConvert(data, 3);
			kvarB = FrameUtils.floatIeeeConvert(data, 7);
			kvarC = FrameUtils.floatIeeeConvert(data, 11);
			kvar = FrameUtils.floatIeeeConvert(data, 15);
			//视在功率 = FrameUtils.floatIeeeConvert(data, 19);
			powerFactor = FrameUtils.floatIeeeConvert(data, 23);
			frequency = FrameUtils.floatIeeeConvert(data, 27);
			kwhForward = FrameUtils.floatIeeeConvert(data, 31);
			kwhReverse = FrameUtils.floatIeeeConvert(data, 35);
			kvarh1 = FrameUtils.floatIeeeConvert(data, 39);
			kvarh2 = FrameUtils.floatIeeeConvert(data, 43);
			kwh = kwhForward + kwhReverse;
		}
		return true;
	}

	@Override
	public void handleResult() {
		ReceiptCircuit receiptCircuit = services.receiptCircuitService.findCircuitByDtuNoAndMeterNoAndLoopNo(receiptCollector.getCollectorNo(), receiptMeter.getMeterNo(), 1);
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
		dataElectricity.setPowerFactorA(powerFactor);
		dataElectricity.setKwh(kwh);
		dataElectricity.setKwhForward(kwhForward);
		dataElectricity.setKwhReverse(kwhReverse);
		dataElectricity.setVoltageAB(voltAB);
		dataElectricity.setVoltageBC(voltBC);
		dataElectricity.setVoltageCA(voltCA);
		dataElectricity.setReceiptCircuit(receiptCircuit);
		dataElectricity.setReadTime(new Date());
		dataElectricity.setVoltageA(voltageA);
		dataElectricity.setVoltageB(voltageB);
		dataElectricity.setVoltageC(voltageC);
		dataElectricity.setKvarh1(kvarh1);
		dataElectricity.setKvarh2(kvarh2);
		services.dataElectricityService.save(dataElectricity);
	}

}
