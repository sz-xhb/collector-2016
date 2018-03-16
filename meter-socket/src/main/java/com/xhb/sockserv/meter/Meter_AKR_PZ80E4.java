package com.xhb.sockserv.meter;

import java.util.Date;

import com.xhb.core.entity.DataElectricity;
import com.xhb.core.entity.ElectricityType;
import com.xhb.core.entity.ReceiptCircuit;
import com.xhb.core.entity.ReceiptCollector;
import com.xhb.core.entity.ReceiptMeter;


/*
 * 安科瑞PZ80E4
 * @author jc 改编自CS中的协议 
 * @time 2016-04-12
 */
public class Meter_AKR_PZ80E4 extends AbstractDevice {

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
	
	public Meter_AKR_PZ80E4(ReceiptCollector receiptCollector, ReceiptMeter receiptMeter) {
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
		int[] crc = new int[2];
		data[0] = Integer.parseInt(receiptMeter.getMeterNo());
		data[1] = 0x03;
		data[2] = 0x00;
		data[3] = 0x23;
		data[4] = 0x00;
		data[5] = 0x2B;
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
		int[] data = new int[frame.length - 9];
		for (int i = 0; i < data.length; i++) {
			data[i] = frame[i + 8] & 0xFF;
		}
		if (!CRC.isValid(data)) {
			return false;
		}
		if (0x56 == data[2]) {
			getParam(data);
		} 
		return true;
	}

	private void getParam(int[] data) {
		int dpt = data[3];
		int dct = data[4];
		int dpq = data[5];
		voltage_A = ((data[7] * 256 + data[8]) * Math.pow(10.0, (dpt-4)));
		voltage_B = ((data[9] * 256 + data[10]) * Math.pow(10.0, (dpt-4)));
		voltage_C = ((data[11] * 256 + data[12]) * Math.pow(10.0, (dpt-4)));
		current_A = ((data[19] * 256 + data[20]) * Math.pow(10.0, (dct-4)));
		current_B = ((data[21] * 256 + data[22]) * Math.pow(10.0, (dct-4)));
		current_C = ((data[23] * 256 + data[24]) * Math.pow(10.0, (dct-4)));
		active_power_A = ((data[25] * 256 + data[26]) * Math.pow(10.0, (dpq-4)));
		active_power_B = ((data[27] * 256 + data[28]) * Math.pow(10.0, (dpq-4)));
		active_power_C = ((data[29] * 256 + data[30]) * Math.pow(10.0, (dpq-4)));
		total_active_power = ((data[31] * 256 + data[32]) * Math.pow(10.0, (dpq-4)));
		reactive_power_A = ((data[33] * 256 + data[34]) * Math.pow(10.0, (dpq-4)));
		reactive_power_B = ((data[35] * 256 + data[36]) * Math.pow(10.0, (dpq-4)));
		reactive_power_C = ((data[37] * 256 + data[38]) * Math.pow(10.0, (dpq-4)));
		total_reactive_power = ((data[39] * 256 + data[40]) * Math.pow(10.0, (dpq-4)));
		factor_A = (data[41] * 256 + data[42]) / 1000.0;
		factor_B = (data[43] * 256 + data[44]) / 1000.0;
		factor_C = (data[45] * 256 + data[46]) / 1000.0;
		factor   = (data[47] * 256 + data[48]) / 1000.0;
		frequency = (data[57] * 256 + data[58]) / 100.0;
		positive_active_energy = (data[59] * 256 * 256 * 256 + data[60] * 256 * 256 
				+ data[61] * 256  + data[62]) / 1000.0;
		reverse_active_energy = (data[63] * 256 * 256 * 256 + data[64] * 256 * 256 
				+ data[65] * 256  + data[66]) / 1000.0;
		combine_active_total_energy = positive_active_energy + reverse_active_energy;
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
