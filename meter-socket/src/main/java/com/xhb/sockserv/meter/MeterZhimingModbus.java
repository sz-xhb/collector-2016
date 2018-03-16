package com.xhb.sockserv.meter;

import java.util.Date;
import java.util.List;

import com.xhb.core.entity.DataElectricity;
import com.xhb.core.entity.ElectricityType;
import com.xhb.core.entity.ReceiptCircuit;
import com.xhb.core.entity.ReceiptCollector;
import com.xhb.core.entity.ReceiptMeter;


public class MeterZhimingModbus extends AbstractDevice {

	private double voltageA;
	private double voltageB;
	private double voltageC;
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
	private double powerFactorA;
	private double powerFactorB;
	private double powerFactorC;
	private double powerFactor;
	private double kvaA;
	private double kvaB;
	private double kvaC;
	private double kva;
	private double frequency;
	private double kwh;
	private double kwhForward;
	private double kwhReverse;
	private double kvarh1;
	private double kvarh2;

	private Integer Ubb = 1;//电压变比
	private Integer Ibb = 1;//电流变比
	
	public MeterZhimingModbus(ReceiptCollector receiptCollector, ReceiptMeter receiptMeter) {
		this.receiptCollector = receiptCollector;
		this.receiptMeter = receiptMeter;
		buildWritingFrames();
	}

	@Override
	public void buildWritingFrames() {
		int[] data = new int[8];
		data[0] = Integer.parseInt(receiptMeter.getMeterNo());
		data[1] = 0x03;
		data[2] = 0x00;
		data[3] = 0x25;
		data[4] = 0x00;
		data[5] = 0x2A;
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

		voltageA = (data[3] * 256 + data[4]) / 10.0;
		voltageB = (data[5] * 256 + data[6]) / 10.0;
		voltageC = (data[7] * 256 + data[8]) / 10.0;
		currentA = (data[15] * 256 + data[16]) / 1000.0;
		currentB = (data[17] * 256 + data[18]) / 1000.0;
		currentC = (data[19] * 256 + data[20]) / 1000.0;
		kwA = (data[21] * 256 + data[22]) / 1000.0;
		kwB = (data[23] * 256 + data[24]) / 1000.0;
		kwC = (data[25] * 256 + data[26]) / 1000.0;
		kw = (data[27] * 256 + data[28]) / 1000.0;
		kvarA = (data[29] * 256 + data[30]) / 1000.0;
		kvarB = (data[31] * 256 + data[32]) / 1000.0;
		kvarC = (data[33] * 256 + data[34]) / 1000.0;
		kvar = (data[35] * 256 + data[36]) / 1000.0;
		powerFactorA = (data[37] * 256 + data[38]) / 1000.0;
		powerFactorB = (data[39] * 256 + data[40]) / 1000.0;
		powerFactorC = (data[41] * 256 + data[42]) / 1000.0;
		powerFactor = (data[43] * 256 + data[44]) / 1000.0;
		kvaA = (data[45] * 256 + data[46]) / 1000.0;
		kvaB = (data[47] * 256 + data[48]) / 1000.0;
		kvaC = (data[49] * 256 + data[50]) / 1000.0;
		kva = (data[51] * 256 + data[52]) / 1000.0;
		frequency = (data[53] * 256 + data[54]) / 100.0;
		kwh = (data[55] * 256 * 256 * 256 + data[56] * 256 * 256 + data[57] * 256 + data[58]) / 1000.0;
		kwhForward = (data[55] * 256 * 256 * 256 + data[56] * 256 * 256 + data[57] * 256 + data[58]) / 1000.0;
		kwhReverse = (data[59] * 256 * 256 * 256 + data[60] * 256 * 256 + data[61] * 256 + data[62]) / 1000.0;
		kvarh1 = (data[63] * 256 * 256 * 256 + data[64] * 256 * 256 + data[65] * 256 + data[66]) / 1000.0;
		kvarh2 = (data[67] * 256 * 256 * 256 + data[68] * 256 * 256 + data[69] * 256 + data[70]) / 1000.0;

		return true;
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
			dataElectricity.setKvaA(kvaA);
			dataElectricity.setKvaB(kvaB);
			dataElectricity.setKvaC(kvaC);
			dataElectricity.setKva(kva);
			dataElectricity.setFrequency(frequency);
			dataElectricity.setKwh(kwh);
			dataElectricity.setKwhForward(kwhForward);
			dataElectricity.setKwhReverse(kwhReverse);
			dataElectricity.setKvarh1(kvarh1);
			dataElectricity.setKvarh2(kvarh2);
			services.dataElectricityService.save(dataElectricity);
		}
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

}
