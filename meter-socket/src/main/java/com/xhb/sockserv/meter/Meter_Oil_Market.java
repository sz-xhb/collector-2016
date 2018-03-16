package com.xhb.sockserv.meter;

import java.text.SimpleDateFormat;
import java.util.List;

import com.xhb.core.entity.DataElecOil;
import com.xhb.core.entity.DataElectricity;
import com.xhb.core.entity.ElectricityType;
import com.xhb.core.entity.ReceiptCircuit;
import com.xhb.core.entity.ReceiptCollector;
import com.xhb.core.entity.ReceiptMeter;


/*
 * 记录油机市电的切换状态和使用的电能
 * @author jc
 * @time 2016-04-13
 */
public class Meter_Oil_Market extends AbstractDevice {

	private String time;
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
	private double kva;
	private double kvaA;
	private double kvaB;
	private double kvaC;
	private double powerFactor;
	private double powerFactorA;
	private double powerFactorB;
	private double powerFactorC;
	private double kwh;
	private double kwhForward;
	private double kwhReverse;
	private double kvarh1;
	private double kvarh2;
	private double oilCommonKwh;
	private double oilCommonKwhForward;
	private double oilCommonKwhReverse;
	private double oilCommonkvarh1;
	private double oilCommonkvarh2;
	private int voltageRatio = 1;
	private int currentRatio = 1;
	private double voltageAB;
	private double voltageBC;
	private double voltageAC;
	private String oilMarketStatus;

	private String[] changeTime = new String[5];
	private String[] afterChangeStatu = new String[5];
	private Double[] makertEnergy = new Double[5];
	private Double[] oilEnergy = new Double[5];

	public Meter_Oil_Market(ReceiptCollector receiptCollector, ReceiptMeter receiptMeter) {
		this.receiptCollector = receiptCollector;
		this.receiptMeter = receiptMeter;
		buildWritingFrames();
	}

	@Override
	public void buildWritingFrames() {
		makeFrame1();
		makeFrame2();
		makeFrame3();
	}

	private void makeFrame1() {
		int[] data = new int[8];
		data[0] = Integer.parseInt(receiptMeter.getMeterNo());
		data[1] = 0x03;
		data[2] = 0x00;
		data[3] = 0x06;
		data[4] = 0x00;
		data[5] = 0x02;
		int[] crc = CRC.calculateCRC(data, 6);
		data[6] = crc[0];
		data[7] = crc[1];
		byte[] frame = new byte[data.length];
		for (int i = 0; i < data.length; i++) {
			frame[i] = (byte) data[i];
		}
		writingFrames.add(frame);
	}

	private void makeFrame2() {
		int[] data = new int[8];
		data[0] = Integer.parseInt(receiptMeter.getMeterNo());
		data[1] = 0x03;
		data[2] = 0x10;
		data[3] = 0x00;
		data[4] = 0x00;
		data[5] = 0x34;
		int[] crc = CRC.calculateCRC(data, 6);
		data[6] = crc[0];
		data[7] = crc[1];
		byte[] frame = new byte[data.length];
		for (int i = 0; i < data.length; i++) {
			frame[i] = (byte) data[i];
		}
		writingFrames.add(frame);
	}

	private void makeFrame3() {
		int[] data = new int[8];
		data[0] = Integer.parseInt(receiptMeter.getMeterNo());
		data[1] = 0x03;
		data[2] = 0x50;
		data[3] = 0x03;
		data[4] = 0x00;
		data[5] = 0x28;
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
		if (readingFrames.size() != 3) {
			return false;
		}
		int[] data = new int[frame.length - 9];
		for (int i = 0; i < data.length; i++) {
			data[i] = frame[i + 8] & 0xFF;
		}
		if (!CRC.isValid(data)) {
			return false;
		}
		if (data[2] == 0x04) {
			analyzeRateData(data);
		} else if (data[2] == 0x68) {
			analyzeEnergyData(data);
		} else if (data[2] == 0x50) {
			analyzeEventRecord(data);
		}
		return true;
	}

	private void analyzeEventRecord(int[] data) {
		for (int i = 0; i < 5; i++) {
			String temp = "20";
			for (int j = 3; j < 8; j++) {
				if (data[j + i * 16] < 0x10)
					temp += "0" + Integer.toHexString(data[j + i * 16]);
				else
					temp += Integer.toHexString(data[j + i * 16]);
			}
			changeTime[i] = temp;
			afterChangeStatu[i] = (data[9 + i * 16] * 256 + data[10 + i * 16]) == 1 ? "o" : "m";
			makertEnergy[i] = (data[11 + i * 16] * 256 * 256 * 256 + data[12 + i * 16] * 256 * 256
					+ data[13 + i * 16] * 256 + data[14 + i * 16]) * voltageRatio * currentRatio / 100.0;
			oilEnergy[i] = (data[15 + i * 16] * 256 * 256 * 256 + data[16 + i * 16] * 256 * 256
					+ data[17 + i * 16] * 256 + data[18 + i * 16]) * voltageRatio * currentRatio / 100.0;
		}
	}

	private void analyzeEnergyData(int[] data) {
		time = "20";
		for (int i = 5; i < 10; i++) {
			if (data[i] < 0x10)
				time += "0" + Integer.toHexString(data[i]);
			else
				time += Integer.toHexString(data[i]);
		}
		frequency = (data[11] * 256 + data[12]) / 100.0;
		voltageA = (data[13] * 256 + data[14]) * voltageRatio / 10.0;
		voltageB = (data[15] * 256 + data[16]) * voltageRatio / 10.0;
		voltageC = (data[17] * 256 + data[18]) * voltageRatio / 10.0;
		currentA = (data[19] * 256 + data[20]) * currentRatio / 100.0;
		currentB = (data[21] * 256 + data[22]) * currentRatio / 100.0;
		currentC = (data[23] * 256 + data[24]) * currentRatio / 100.0;
		kw = (data[25] * 256 + data[26]) * voltageRatio * currentRatio / 100.0;
		kwA = (data[27] * 256 + data[28]) * voltageRatio * currentRatio / 100.0;
		kwB = (data[29] * 256 + data[30]) * voltageRatio * currentRatio / 100.0;
		kwC = (data[31] * 256 + data[32]) * voltageRatio * currentRatio / 100.0;
		kvar = (data[33] * 256 + data[34]) * voltageRatio * currentRatio / 100.0;
		kvarA = (data[35] * 256 + data[36]) * voltageRatio * currentRatio / 100.0;
		kvarB = (data[37] * 256 + data[38]) * voltageRatio * currentRatio / 100.0;
		kvarC = (data[39] * 256 + data[40]) * voltageRatio * currentRatio / 100.0;
		kva = (data[41] * 256 + data[42]) * voltageRatio * currentRatio / 100.0;
		kvaA = (data[43] * 256 + data[44]) * voltageRatio * currentRatio / 100.0;
		kvaB = (data[45] * 256 + data[46]) * voltageRatio * currentRatio / 100.0;
		kvaC = (data[47] * 256 + data[48]) * voltageRatio * currentRatio / 100.0;
		powerFactor = (data[49] * 256 + data[50]) / 100.0;
		powerFactorA = (data[51] * 256 + data[52]) / 100.0;
		powerFactorB = (data[53] * 256 + data[54]) / 100.0;
		powerFactorC = (data[55] * 256 + data[56]) / 100.0;
		kwh = (data[59] * 256 * 256 * 256L + data[60] * 256 * 256 + data[61] * 256 + data[62]) * voltageRatio
				* currentRatio / 100.0;
		kwhForward = (data[63] * 256 * 256 * 256L + data[64] * 256 * 256 + data[65] * 256 + data[66]) * voltageRatio
				* currentRatio / 100.0;
		kwhReverse = (data[67] * 256 * 256 * 256L + data[68] * 256 * 256 + data[69] * 256 + data[70]) * voltageRatio
				* currentRatio / 100.0;
		kvarh1 = (data[71] * 256 * 256 * 256L + data[72] * 256 * 256 + data[73] * 256 + data[74]) * voltageRatio
				* currentRatio / 100.0;
		kvarh2 = (data[75] * 256 * 256 * 256L + data[76] * 256 * 256 + data[77] * 256 + data[78]) * voltageRatio
				* currentRatio / 100.0;
		voltageAB = (data[79] * 256 + data[80]) * voltageRatio / 10.0;
		voltageBC = (data[81] * 256 + data[82]) * voltageRatio / 10.0;
		voltageAC = (data[83] * 256 + data[84]) * voltageRatio / 10.0;
		oilCommonKwh = (data[85] * 256 * 256 * 256L + data[86] * 256 * 256 + data[87] * 256 + data[88]) * voltageRatio
				* currentRatio / 100.0;
		oilCommonKwhForward = (data[89] * 256 * 256 * 256L + data[90] * 256 * 256 + data[91] * 256 + data[92])
				* voltageRatio * currentRatio / 100.0;
		oilCommonKwhReverse = (data[93] * 256 * 256 * 256L + data[94] * 256 * 256 + data[95] * 256 + data[96])
				* voltageRatio * currentRatio / 100.0;
		oilCommonkvarh1 = (data[97] * 256 * 256 * 256L + data[98] * 256 * 256 + data[99] * 256 + data[100])
				* voltageRatio * currentRatio / 100.0;
		oilCommonkvarh1 = (data[101] * 256 * 256 * 256L + data[102] * 256 * 256 + data[103] * 256 + data[104])
				* voltageRatio * currentRatio / 100.0;
		oilMarketStatus = (data[105] * 256 + data[106]) == 1 ? "o" : "m";
	}

	private void analyzeRateData(int[] data) {
		voltageRatio = data[3] * 256 + data[4];
		currentRatio = data[5] * 256 + data[6];
	}

	@Override
	public void handleResult() {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
			List<ReceiptCircuit> receiptCircuits = services.receiptCircuitService.queryByMeterId(receiptMeter.getId());
			DataElectricity dataElectricity = new DataElectricity();
			dataElectricity.setReceiptCircuit(receiptCircuits.get(0));
			dataElectricity.setReadTime(sdf.parse(time));
			dataElectricity.setElectricityType(ElectricityType.OIL_MARKET);
			dataElectricity.setFrequency(frequency);
			dataElectricity.setVoltageA(voltageA);
			dataElectricity.setVoltageB(voltageB);
			dataElectricity.setVoltageC(voltageC);
			dataElectricity.setVoltageAB(voltageAB);
			dataElectricity.setVoltageBC(voltageBC);
			dataElectricity.setVoltageCA(voltageAC);
			dataElectricity.setCurrentA(currentA);
			dataElectricity.setCurrentB(currentB);
			dataElectricity.setCurrentC(currentC);
			dataElectricity.setKva(kva);
			dataElectricity.setKvaA(kvaA);
			dataElectricity.setKvaB(kvaB);
			dataElectricity.setKvaC(kvaC);
			dataElectricity.setKw(kw);
			dataElectricity.setKwA(kwA);
			dataElectricity.setKwB(kwB);
			dataElectricity.setKwC(kwC);
			dataElectricity.setKvar(kvar);
			dataElectricity.setKvarA(kvarA);
			dataElectricity.setKvarB(kvarB);
			dataElectricity.setKvarC(kvarC);
			dataElectricity.setKwh(kwh);
			dataElectricity.setKwhForward(kwhForward);
			dataElectricity.setKwhReverse(kwhReverse);
			dataElectricity.setKvarh1(kvarh1);
			dataElectricity.setKvarh2(kvarh2);
			dataElectricity.setPowerFactor(powerFactor);
			dataElectricity.setPowerFactorA(powerFactorA);
			dataElectricity.setPowerFactorB(powerFactorB);
			dataElectricity.setPowerFactorC(powerFactorC);
			services.dataElectricityService.save(dataElectricity);

			DataElecOil deo = new DataElecOil();
			deo.setDataId(dataElectricity.getId());
			deo.setSwitchType(oilMarketStatus);
			deo.setKvarh1(oilCommonkvarh1);
			deo.setKvarh2(oilCommonkvarh2);
			deo.setKwhFor(oilCommonKwhForward);
			deo.setKwhRev(oilCommonKwhReverse);
			deo.setKwhTotal(oilCommonKwh);
			if (!changeTime[0].equals("200000000000")) {
				deo.setElecKwhTotal1(makertEnergy[0]);
				deo.setOilKwhTotal1(oilEnergy[0]);
				deo.setSwitchTime1(sdf.parse(changeTime[0]));
				deo.setSwitchType1(afterChangeStatu[0]);
			}
			if (!changeTime[1].equals("200000000000")) {
				deo.setElecKwhTotal2(makertEnergy[1]);
				deo.setOilKwhTotal2(oilEnergy[1]);
				deo.setSwitchTime2(sdf.parse(changeTime[1]));
				deo.setSwitchType2(afterChangeStatu[1]);
			}
			if (!changeTime[2].equals("200000000000")) {
				deo.setElecKwhTotal3(makertEnergy[2]);
				deo.setOilKwhTotal3(oilEnergy[2]);
				deo.setSwitchTime3(sdf.parse(changeTime[2]));
				deo.setSwitchType3(afterChangeStatu[2]);
			}
			if (!changeTime[3].equals("200000000000")) {
				deo.setElecKwhTotal4(makertEnergy[3]);
				deo.setOilKwhTotal4(oilEnergy[3]);
				deo.setSwitchTime4(sdf.parse(changeTime[3]));
				deo.setSwitchType4(afterChangeStatu[3]);
			}
			if (!changeTime[4].equals("200000000000")) {
				deo.setElecKwhTotal5(makertEnergy[4]);
				deo.setOilKwhTotal5(oilEnergy[4]);
				deo.setSwitchTime5(sdf.parse(changeTime[4]));
				deo.setSwitchType5(afterChangeStatu[4]);
			}
			services.dataElecOilService.save(deo);
		} catch (Exception ex) {
			logger.error("save Meter_Oil_Markert data Error! circuit of dtuNo:" + receiptCollector.getCollectorNo()
					+ "meterNo:" + receiptMeter.getMeterNo(), ex);
		}
	}

}
