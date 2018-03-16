package com.xhb.sockserv.meter;

import java.util.Date;
import java.util.List;

import com.xhb.core.entity.DataElectricity;
import com.xhb.core.entity.ElectricityType;
import com.xhb.core.entity.ReceiptCircuit;
import com.xhb.core.entity.ReceiptCollector;
import com.xhb.core.entity.ReceiptMeter;

public class Meter_OneWayA extends AbstractDevice {

	protected String time = null;
	protected double frequency;
	protected double voltage;
	protected double current;
	protected double kw ;
	protected double kwh;
	protected double kwh_forward;
	protected double kwh_reverse;
	protected double powerFactor;
	private Integer Ubb = 1;
	private Integer Ibb = 1;
	
	protected double kvar;
	protected double kva;
	protected double kvarh1;
	protected double kvarh2;
	
	
	
	public Meter_OneWayA(ReceiptCollector receiptCollector, ReceiptMeter receiptMeter){
		this.receiptCollector = receiptCollector;
		this.receiptMeter = receiptMeter;
		buildWritingFrames();
	}
	@Override
	public void buildWritingFrames() {
		writingFrames.add(makeFrameRate());
		writingFrames.add(makeFrame());
	}

	private byte[] makeFrame() {
		int[] sendData = new int[8];
		sendData[0] = Integer.parseInt(receiptMeter.getMeterNo());
		sendData[1] = 0x03;
		sendData[2] = 0x10;
		sendData[3] = 0x00;
		sendData[4] = 0x00;
		sendData[5] = 0x26;
		int crc[] = CRC.calculateCRC(sendData, 6);
		sendData[6] = crc[0];
		sendData[7] = crc[1];
		byte [] frame = new byte[8];
		for(int i=0; i<8; i++){
			frame[i]= (byte) sendData[i] ;
		}
		return frame;
	}
	private byte[] makeFrameRate(){
		int[] sendData = new int[8];
		sendData[0] = Integer.parseInt(receiptMeter.getMeterNo());
		sendData[1] = 0x03;
		sendData[2] = 0x00;
		sendData[3] = 0x06;
		sendData[4] = 0x00;
		sendData[5] = 0x02;
		int crc[] = CRC.calculateCRC(sendData, 6);
		sendData[6] = crc[0];
		sendData[7] = crc[1];
		byte [] frame = new byte[8];
		for(int i=0; i<8; i++){
			frame[i]= (byte) sendData[i] ;
		}
		return frame;
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
		if (data[2] == 0x4C) {
			analyzeDataInfo(data);
		}else if (data[2] == 0x04) {
			analyzeRateInfo(data);
		}
		return true;
	}

	private void analyzeRateInfo(int[] data) {
		Ubb = data[3] * 256 + data[4];
		Ibb = data[5] * 256 + data[6];
	}
	private void analyzeDataInfo(int[] data) {
		time = "20";
		for (int i = 5; i < 10; i++) {
			if (data[i] < 0x10)
				time += "0" + Integer.toHexString(data[i]);
			else
				time += Integer.toHexString(data[i]);
		}
		frequency = (data[11]*256 + data[12]) / 100.0;
		voltage = (data[13]*256 + data[14]) / 10.0;
		current = (data[19]*256 + data[20]) / 100.0;
		kw = (data[27]*256 + data[28]) / 100.0;
		kvar = (data[35] * 256 + data[36]) / 100.0;
		kva = (data[43] * 256 + data[44]) / 100.0;
		powerFactor = (data[51]*256 + data[52]) / 100.0;
		kwh = (data[59] *256*256*256 + data[60]*256*256 + data[61]*256 + data[62]) / 100.0;
		kwh_forward = (data[63] *256*256*256 + data[64]*256*256 + data[65]*256 + data[66]) / 100.0;
		kwh_reverse = (data[67] *256*256*256 + data[68]*256*256 + data[69]*256 + data[70]) / 100.0;
		kvarh1 = (data[71] *256*256*256 + data[72]*256*256 + data[73]*256 + data[74]) / 100.0;
		kvarh1 = (data[75] *256*256*256 + data[76]*256*256 + data[77]*256 + data[78]) / 100.0;
		
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
			dataElectricity.setElectricityType(ElectricityType.AC_SINGLE);
			dataElectricity.setFrequency(frequency);
			dataElectricity.setVoltage(voltage);
			dataElectricity.setCurrent(current);
			dataElectricity.setKw(kw);
			dataElectricity.setKwh(kwh);
			dataElectricity.setKwhForward(kwh_forward);
			dataElectricity.setKwhReverse(kwh_reverse);
			dataElectricity.setPowerFactor(powerFactor);
			
			dataElectricity.setKva(kva);
			dataElectricity.setKvar(kvar);
			dataElectricity.setKvarh1(kvarh1);
			dataElectricity.setKvarh2(kvarh2);
			services.dataElectricityService.save(dataElectricity);
		}
	}
	private void resultMutiplyRate() {
		voltage *= Ubb;
		current *= Ibb;
		kw *= Ubb * Ibb;
		kwh *= Ubb * Ibb;
		kwh_forward *= Ubb * Ibb;
		kwh_reverse *= Ubb * Ibb;
	}
}
