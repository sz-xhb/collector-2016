package com.xhb.sockserv.meter;

import com.xhb.core.entity.DataElectricity;
import com.xhb.core.entity.ReceiptCircuit;
import com.xhb.core.entity.ReceiptCollector;
import com.xhb.core.entity.ReceiptMeter;

/*
 * 迅鹏SPA-96BDE直流电能表
 * @author jc 改编自CS中孙家伟写个协议 
 * @time 2016-04-11
 */
public class Meter_XP_96BDE extends AbstractDevice{

	private double kwhForward;
	private double kwhReverse;
	private double voltage;
	private double current;
	private double power;
	private Integer Ubb = 1;
	private Integer Ibb = 1;
	
	public Meter_XP_96BDE(ReceiptCollector receiptCollector, ReceiptMeter receiptMeter){
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
		data[2] = 0x08;
		data[3] = 0x00;
		data[4] = 0x00;
		data[5] = 0x07;
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
		int meterNo = Integer.parseInt(receiptMeter.getMeterNo()); 
		if(meterNo != data[0]){
			return false;
		}
		if (!CRC.isValid(data))
			return false;
		analyzeFrame(data);

		return true;
	}

	private void analyzeFrame(int[] data) {
		kwhForward=(data[5] * Math.pow(2, 24) + data[6] * Math.pow(2, 16) + data[3] * Math.pow(2, 8) + data[4]) / 100;
		kwhReverse=(data[9] * Math.pow(2, 24) + data[10] * Math.pow(2, 16) + data[7] * Math.pow(2, 8) + data[8]) / 100;
		voltage=(data[11] * Math.pow(2, 8) + data[12]) / 10;
		current=(data[13] * Math.pow(2, 8) + data[14]);
		power=(data[15] * Math.pow(2, 8) + data[16]) / 10;
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
		dataElectricity.setCurrent(current);
		dataElectricity.setKw(power);
		dataElectricity.setVoltage(voltage);
		dataElectricity.setKwhForward(kwhForward);
		dataElectricity.setKwhReverse(kwhReverse);
		dataElectricity.setReceiptCircuit(receiptCircuit);
		services.dataElectricityService.save(dataElectricity);
		
	}
	
	//将各个电参量乘以电压和电流变比
	private void resultMutiplyRate() {
		voltage *= Ubb;
		current *= Ibb;
		power = power * Ubb * Ibb;
		kwhForward = kwhForward * Ubb * Ibb;
		kwhReverse = kwhReverse * Ubb * Ibb;
	}

}
