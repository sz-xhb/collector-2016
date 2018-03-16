package com.xhb.sockserv.meter;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.xhb.core.entity.DataElectricity;
import com.xhb.core.entity.ElectricityType;
import com.xhb.core.entity.ReceiptCircuit;
import com.xhb.core.entity.ReceiptMeter;
import com.xhb.sockserv.util.FrameUtils;


public class MeterXinhongboDsm extends AbstractDevice {

	private Date readTime;

	private Double voltageA;
	private Double voltageB;
	private Double voltageC;
	private Double currentA;
	private Double currentB;
	private Double currentC;
	private Double kwA;
	private Double kwB;
	private Double kwC;
	private Double kw;
	private Double kvarA;
	private Double kvarB;
	private Double kvarC;
	private Double kvar;
	private Double powerFactorA;
	private Double powerFactorB;
	private Double powerFactorC;
	private Double powerFactor;
//	private Double kvaA;
//	private Double kvaB;
//	private Double kvaC;
//	private Double kva;
	private Double frequency;
	private Double kwh;
	private Double kwhForward;
	private Double kwhReverse;
	private Double kvarh1;
	private Double kvarh2;

	public MeterXinhongboDsm() {
		buildWritingFrames();
	}

	@Override
	public void buildWritingFrames() {
		//
	}

	@Override
	public boolean analyzeFrame(byte[] frame) {
		byte[] body = Arrays.copyOfRange(frame, 8, frame.length - 1);
		if (!isValid(body)) {
			return false;
		}

		String dtuNo = FrameUtils.getDtuNo(frame);
		receiptCollector = services.receiptCollectorService.getByDtuNo(dtuNo);
		if (receiptCollector == null) {
			return false;
		}
		
		if(receiptCollector.getCollectorType().toString().equals("PASSIVE")){
			return false;
		}

		byte[] meterNo = Arrays.copyOfRange(body, 7, 13);
		ArrayUtils.reverse(meterNo);
		List<ReceiptMeter> meters = services.ReceiptMeterService.findByCollectorId(receiptCollector.getId());
		for (ReceiptMeter meter : meters) {
			if (FrameUtils.toHexString(meterNo).equalsIgnoreCase(meter.getMeterNo())) {
				receiptMeter = meter;
				break;
			}
			if (String.valueOf(Long.parseLong(FrameUtils.toHexString(meterNo), 16)).equals(StringUtils.stripStart(meter.getMeterNo(), "0"))) {
				receiptMeter = meter;
				break;
			}
		}
		if (receiptMeter == null) {
			return false;
		}

		byte[] circuitNo = Arrays.copyOfRange(body, 13, 15);
		List<ReceiptCircuit> circuits =services.receiptCircuitService.queryByMeterId(receiptMeter.getId());
		for (ReceiptCircuit circuit : circuits) {
			if (String.valueOf(Long.parseLong(FrameUtils.toHexString(circuitNo), 16)).equals(StringUtils.stripStart(circuit.getCircuitNo(), "0"))) {
				receiptCircuit = circuit;
				break;
			}
		}
		if (receiptCircuit == null) {
			return false;
		}

		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			readTime = format.parse(FrameUtils.toHexString(Arrays.copyOfRange(body, 17, 24)));
		} catch (Exception e) {
			return false;
		}

		byte[] data = Arrays.copyOfRange(body, 24, frame.length - 3);
		while (data.length >= 10) {
			double value = Double.parseDouble(FrameUtils.toHexString(Arrays.copyOfRange(data, 4, 8))
					+ "." + FrameUtils.toHexString(Arrays.copyOfRange(data, 8, 10)));
			if (data[0] == (byte) 0x01) currentA = value;
			if (data[0] == (byte) 0x02) currentB = value;
			if (data[0] == (byte) 0x03) currentC = value;
			if (data[0] == (byte) 0x04) voltageA = value;
			if (data[0] == (byte) 0x05) voltageB = value;
			if (data[0] == (byte) 0x06) voltageC = value;
			if (data[0] == (byte) 0x07) kwA = value;
			if (data[0] == (byte) 0x08) kwB = value;
			if (data[0] == (byte) 0x09) kwC = value;
			if (data[0] == (byte) 0x10) powerFactorA = value;
			if (data[0] == (byte) 0x11) powerFactorB = value;
			if (data[0] == (byte) 0x12) powerFactorC = value;
			if (data[0] == (byte) 0x13) kvarA = value;
			if (data[0] == (byte) 0x14) kvarB = value;
			if (data[0] == (byte) 0x15) kvarC = value;
			if (data[0] == (byte) 0x17) kw = value;
			if (data[0] == (byte) 0x18) powerFactor = value;
			if (data[0] == (byte) 0x19) kvar = value;
			if (data[0] == (byte) 0x20) frequency = value;
			if (data[0] == (byte) 0x31) kwhForward = value;
			if (data[0] == (byte) 0x32) kvarh1 = value;
			if (data[0] == (byte) 0x33) kwhReverse = value;
			if (data[0] == (byte) 0x34) kvarh2 = value;
			data = Arrays.copyOfRange(data, 10, data.length);
		}
		if (kwhForward != null) {
			if (kwhReverse != null) {
				kwh = kwhForward + kwhReverse;
			} else {
				kwh = kwhForward;
			}
		} else {
			kwh = kwhReverse;
		}

		return true;
	}

	private boolean isValid(byte[] data) {
		int sum = 0;
		for (int i = 1; i < data.length - 3; i++) {
			sum = sum + (data[i] & 0xFF);
		}
		return sum == Integer.parseInt(FrameUtils.toHexString(
				new byte[] { data[data.length - 2], data[data.length - 3] }), 16);
	}

	@Override
	public void handleResult() {
		DataElectricity dataElectricity = new DataElectricity();
		dataElectricity.setReceiptCircuit(receiptCircuit);
		dataElectricity.setReadTime(readTime);
		dataElectricity.setElectricityType(ElectricityType.AC_THREE);
		dataElectricity.setVoltageA(voltageA);
		dataElectricity.setVoltageB(voltageB);
		dataElectricity.setVoltageC(voltageC);
		dataElectricity.setCurrentA(currentA);
		dataElectricity.setCurrentB(currentB);
		dataElectricity.setCurrentC(currentC);
		dataElectricity.setKwA(kwA);
		dataElectricity.setKwB(kwB);
		dataElectricity.setKwC(kwC);
		dataElectricity.setKw(kw);
		dataElectricity.setKvarA(kvarA);
		dataElectricity.setKvarB(kvarB);
		dataElectricity.setKvarC(kvarC);
		dataElectricity.setKvar(kvar);
		dataElectricity.setPowerFactorA(powerFactorA);
		dataElectricity.setPowerFactorB(powerFactorB);
		dataElectricity.setPowerFactorC(powerFactorC);
		dataElectricity.setPowerFactor(powerFactor);
//		dataElectricity.setKvaA(kvaA);
//		dataElectricity.setKvaB(kvaB);
//		dataElectricity.setKvaC(kvaC);
//		dataElectricity.setKva(kva);
		dataElectricity.setFrequency(frequency);
		dataElectricity.setKwh(kwh);
		dataElectricity.setKwhForward(kwhForward);
		dataElectricity.setKwhReverse(kwhReverse);
		dataElectricity.setKvarh1(kvarh1);
		dataElectricity.setKvarh2(kvarh2);
		services.dataElectricityService.save(dataElectricity);
	}

}
