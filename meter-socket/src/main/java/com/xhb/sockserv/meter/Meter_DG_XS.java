package com.xhb.sockserv.meter;

import java.util.Date;

import com.xhb.core.entity.DataElectricity;
import com.xhb.core.entity.ElectricityType;
import com.xhb.core.entity.ReceiptCircuit;
import com.xhb.core.entity.ReceiptCollector;
import com.xhb.core.entity.ReceiptMeter;


/*
 * 东歌电气 X/S系列
 * @author jc 
 * @time 2016-04-11
 */
public class Meter_DG_XS extends AbstractDevice {

	private double current_A = 0;
	private double current_B = 0;
	private double current_C = 0;
	private double voltage_A = 0;
	private double voltage_B = 0;
	private double voltage_C = 0;
	private double active_power_A = 0;
	private double active_power_B = 0;
	private double active_power_C = 0;
	private double active_power = 0;
	private double reactive_power_A = 0;
	private double reactive_power_B = 0;
	private double reactive_power_C = 0;
	private double reactive_power = 0;
	private double frequency = 0;
	private double active_energy = 0;
	private double _active_energy = 0;
	private double reactive_energy = 0;
	private double _reactive_energy = 0;
	private double Ibb = 0;
	private double Ubb = 0;
	private double power_factor = 0;
	
	public Meter_DG_XS(ReceiptCollector receiptCollector, ReceiptMeter receiptMeter) {
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
		data[3] = 0x00;
		data[4] = 0x00;
		data[5] = 0x38;
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
		int meterNo = Integer.parseInt(receiptMeter.getMeterNo()); 
		if(meterNo != data[0]){
			return false;
		}
		if (0x70 == data[2]) {
			getParam(data);
		} 
		return true;
	}

	private void getParam(int[] data) {
		Ibb = (data[95] * 256 + data[96]);
		Ubb = (data[93] * 256 + data[94]);
		current_A = ((data[3] * 256 + data[4]) ) / 1000.0;
		current_B = ((data[5] * 256 + data[6]) ) / 1000.0;
		current_C = ((data[7] * 256 + data[8]) ) / 1000.0;
//		current = ((data[9] * 256 + data[10]) * current_ratio) / 1000.0;
		voltage_A = ((data[11] * 256 + data[12])) / 10.0 ;
		voltage_B = ((data[13] * 256 + data[14])) / 10.0;
		voltage_C = ((data[15] * 256 + data[16])) / 10.0;
		active_power_A = ((data[19] * 256 + data[20])) / 10.0 / 1000.0;
		active_power_B = ((data[21] * 256 + data[22])) / 10.0 / 1000.0;
		active_power_C = ((data[23] * 256 + data[24])) / 10.0 / 1000.0;
		active_power = ((data[25] * 256 + data[26]) ) / 10.0 / 1000.0;
		reactive_power_A = ((data[27] * 256 + data[28])) / 10.0 / 1000.0;
		reactive_power_B = ((data[29] * 256 + data[30])) / 10.0 / 1000.0;
		reactive_power_C = ((data[31] * 256 + data[32])) / 10.0 / 1000.0;
		reactive_power = ((data[33] * 256 + data[34])) / 10.0 / 1000.0;
		active_energy = (data[62] * 256 * 256 * 256 * 256 + data[65] * 256 * 256* 256 + data[66] * 256 * 256 + data[63] * 256 + data[64]) / 10.0 ;
		_active_energy = ((data[69] * 256 * 256* 256 + data[70] * 256 * 256 + data[67] * 256 + data[68])) / 10.0 ;
		reactive_energy = (data[61] * 256 * 256 * 256 * 256 + data[73] * 256 * 256* 256 + data[74] * 256 * 256 + data[71] * 256 + data[72]) / 10.0 ;
		_reactive_energy = ((data[77] * 256 * 256* 256 + data[78] * 256 * 256 + data[75] * 256 + data[76])) / 10.0 ;
		power_factor = (data[35] * 256 + data[36]) / 10000.0;
		frequency = (data[45] * 256 + data[46]) /100.0;
	}

	private void resultMutiplyRate(){
		voltage_A *= Ubb;
		voltage_B *= Ubb;
		voltage_C *= Ubb;
		current_A *= Ibb;
		current_B *= Ibb; 
		current_C *= Ibb;
		active_power = active_power * Ubb * Ibb;
		active_power_A = active_power_A * Ubb * Ibb;
		active_power_B = active_power_B * Ubb * Ibb;
		active_power_C = active_power_C * Ubb * Ibb;
		reactive_power = reactive_power * Ubb * Ibb;
		reactive_power_A = reactive_power_A * Ubb * Ibb;
		reactive_power_B = reactive_power_B * Ubb * Ibb;
		reactive_power_C = reactive_power_C * Ubb * Ibb;
		active_energy = active_energy * Ubb * Ibb;
		reactive_energy = reactive_energy * Ubb * Ibb;
		_active_energy = _active_energy * Ubb * Ibb;
		_reactive_energy = _reactive_energy * Ubb * Ibb;
	}
	@Override
	public void handleResult() {
		ReceiptCircuit receiptCircuit = services.receiptCircuitService.findCircuitByDtuNoAndMeterNoAndLoopNo(receiptCollector.getCollectorNo(), receiptMeter.getMeterNo(), 1);
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
		dataElectricity.setCurrentA(current_A);
		dataElectricity.setCurrentB(current_B);
		dataElectricity.setCurrentC(current_C);
		dataElectricity.setElectricityType(ElectricityType.AC_THREE);
		dataElectricity.setFrequency(frequency);
		dataElectricity.setKvar(reactive_power);
		dataElectricity.setKvarA(reactive_power_A);
		dataElectricity.setKvarB(reactive_power_B);
		dataElectricity.setKvarC(reactive_power_C);
		dataElectricity.setKw(active_power);
		dataElectricity.setKwA(active_power_A);
		dataElectricity.setKwB(active_power_B);
		dataElectricity.setKwC(active_power_C);
		dataElectricity.setVoltageA(voltage_A);
		dataElectricity.setVoltageB(voltage_B);
		dataElectricity.setVoltageC(voltage_C);
		dataElectricity.setKwh(active_energy);
		dataElectricity.setKwhForward(active_energy);
		dataElectricity.setKwhReverse(_active_energy);
		dataElectricity.setPowerFactor(power_factor);
		dataElectricity.setKvarh1(reactive_energy);
		dataElectricity.setKvarh2(_reactive_energy);
		dataElectricity.setReceiptCircuit(receiptCircuit);
		dataElectricity.setReadTime(new Date());
		services.dataElectricityService.save(dataElectricity);
	}

}
