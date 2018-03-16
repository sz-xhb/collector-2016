package com.xhb.sockserv.meter;

import java.util.Date;

import com.xhb.core.entity.DataElectricity;
import com.xhb.core.entity.ElectricityType;
import com.xhb.core.entity.ReceiptCircuit;
import com.xhb.core.entity.ReceiptCollector;
import com.xhb.core.entity.ReceiptMeter;

/*
 * 安科瑞DTSD1352电表
 * @author jc 改编自CS中的协议 
 * @time 2016-04-12
 */
public class Meter_AKR_DTSD1352 extends AbstractDevice {

	private double frequency = 0;
	private double voltage_A = 0;
	private double voltage_B = 0;
	private double voltage_C = 0;
	private double current_A = 0;
	private double current_B = 0;
	private double current_C = 0;
	private double total_active_power = 0;
	private double active_power_A = 0;
	private double active_power_B = 0;
	private double active_power_C = 0;
	private double total_reactive_power = 0;
	private double reactive_power_A = 0;
	private double reactive_power_B = 0;
	private double reactive_power_C = 0;
	private double factor = 0;
	private double factor_A = 0;
	private double factor_B = 0;
	private double factor_C = 0;
	private double combine_active_total_energy = 0;
	private double positive_active_energy = 0;
	private double reverse_active_energy = 0;
	
	public Meter_AKR_DTSD1352(ReceiptCollector receiptCollector, ReceiptMeter receiptMeter) {
		this.receiptCollector = receiptCollector;
		this.receiptMeter = receiptMeter;
		buildWritingFrames();
	}
	@Override
	public void buildWritingFrames() {
		makeFrameForVoltAndCurrentAndPow();
		makeFrameForEnergyAndTime();
	}

	private void makeFrameForEnergyAndTime() {
		int[] data = new int[8];
		int[] crc = new int[2];
		data[0] = Integer.parseInt(receiptMeter.getMeterNo());
		data[1] = 0x03;
		data[2] = 0x00;
		data[3] = 0x00;
		data[4] = 0x00;
		data[5] = 0x3F;
		crc = CRC.calculateCRC(data, 6);
		data[6] = crc[0];
		data[7] = crc[1];
		byte[] frame = new byte[data.length];
		for (int i = 0; i < data.length; i++) {
			frame[i] = (byte) data[i];
		}
		writingFrames.add(frame);
	}
	private void makeFrameForVoltAndCurrentAndPow() {
		int[] data = new int[8];
		int[] crc = new int[2];
		data[0] = Integer.parseInt(receiptMeter.getMeterNo());
		data[1] = 0x03;
		data[2] = 0x00;
		data[3] = 0x61;
		data[4] = 0x00;
		data[5] = 0x17;
		crc = CRC.calculateCRC(data, 6);
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
		// crc校验是否正确
		if (!CRC.isValid(data)) {
			return false;
		}
		// 0x4C为返回的报文的数据部分的长度，通常为请求的2倍
		if (0x7E == data[2]) {
			getEnergyAndTime(data);
		} 
		else if(0x2E == data[2]){
			getCurrentOrVoltageAndPower(data);
		}
		return true;
	}

	private void getEnergyAndTime(int[] data) {
		combine_active_total_energy = (data[3]*256*256*256 + data[4]*256*256 
				+ data[5]*256 + data[6]) / 100.0;
		positive_active_energy = (data[23]*256*256*256 + data[24]*256*256 
				+ data[25]*256 + data[26]) / 100.0;
		reverse_active_energy = (data[43]*256*256*256 + data[44]*256*256 
				+ data[45]*256 + data[46]) / 100.0;
	}
	
	private void getCurrentOrVoltageAndPower(int[] data) {
		voltage_A = (data[3] * 256 + data[4]) / 10.0;
		voltage_B = (data[5] * 256 + data[6]) / 10.0;
		voltage_C = (data[7] * 256 + data[8]) / 10.0;
		current_A = (data[9] * 256 + data[10]) / 100.0;
		current_B = (data[11] * 256 + data[12]) / 100.0;
		current_C = (data[13] * 256 + data[14]) / 100.0;
		active_power_A = (data[15] * 256 + data[16]) / 1000.0;
		active_power_B = (data[17] * 256 + data[18]) / 1000.0;
		active_power_C = (data[19] * 256 + data[20]) / 1000.0;
		total_active_power = (data[21] * 256 + data[22]) / 1000.0;
		reactive_power_A = (data[23] * 256 + data[24]) / 1000.0;
		reactive_power_B = (data[25] * 256 + data[26]) / 1000.0;
		reactive_power_C = (data[27] * 256 + data[28]) / 1000.0;
		total_reactive_power = (data[29] * 256 + data[30]) / 1000.0;
		factor_A = (data[39] * 256 + data[40]) / 100.0;
		factor_B = (data[41] * 256 + data[42]) / 100.0;
		factor_C = (data[43] * 256 + data[44]) / 100.0;
		factor = (data[45] * 256 + data[46]) / 100.0;
		frequency = (data[47] * 256 + data[48]) / 10.0;
	}
	@Override
	public void handleResult() {
		ReceiptCircuit receiptCircuit = services.receiptCircuitService.findCircuitByDtuNoAndMeterNoAndLoopNo(receiptCollector.getCollectorNo(), receiptMeter.getMeterNo(), 1);;
		DataElectricity dataElectricity = new DataElectricity();
		dataElectricity.setCurrentA(current_A);
		dataElectricity.setCurrentB(current_B);
		dataElectricity.setCurrentC(current_C);
		dataElectricity.setElectricityType(ElectricityType.AC_THREE);
		dataElectricity.setFrequency(frequency);
		dataElectricity.setKvar(total_reactive_power);
		dataElectricity.setKvarA(reactive_power_A);
		dataElectricity.setKvarB(reactive_power_B);
		dataElectricity.setKvarC(reactive_power_C);
		dataElectricity.setKw(total_active_power);
		dataElectricity.setKwA(active_power_A);
		dataElectricity.setKwB(active_power_B);
		dataElectricity.setKwC(active_power_C);
		dataElectricity.setVoltageA(voltage_A);
		dataElectricity.setVoltageB(voltage_B);
		dataElectricity.setVoltageC(voltage_C);
		dataElectricity.setKwh(combine_active_total_energy);
		dataElectricity.setKwhForward(positive_active_energy);
		dataElectricity.setKwhReverse(reverse_active_energy);
		dataElectricity.setPowerFactor(factor);
		dataElectricity.setPowerFactorA(factor_A);
		dataElectricity.setPowerFactorB(factor_B);
		dataElectricity.setPowerFactorC(factor_C);
		dataElectricity.setReceiptCircuit(receiptCircuit);
		dataElectricity.setReadTime(new Date());
		services.dataElectricityService.save(dataElectricity);
	}

}
