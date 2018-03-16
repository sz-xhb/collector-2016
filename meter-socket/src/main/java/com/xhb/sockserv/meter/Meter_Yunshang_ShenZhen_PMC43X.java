package com.xhb.sockserv.meter;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import com.xhb.core.entity.DataElectricity;
import com.xhb.core.entity.ElectricityType;
import com.xhb.core.entity.ReceiptCircuit;
import com.xhb.core.entity.ReceiptCollector;
import com.xhb.core.entity.ReceiptMeter;


public class Meter_Yunshang_ShenZhen_PMC43X extends AbstractDevice {

	private double Ubb = 1;
	private double Ibb = 1;
	private double voltA;
	private double voltB;
	private double voltC;
	private double voltAB;
	private double voltBC;
	private double voltCA;
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
	private double kvaA;
	private double kvaB;
	private double kvaC;
	private double kva;
	private double powerFactorA;
	private double powerFactorB;
	private double powerFactorC;
	private double powerFactor;
	private double frequency;
	private double kwh;
	private double kwhForward;
	private double kwhReverse;
	private double kvarh1;
	private double kvarh2;
	
	public Meter_Yunshang_ShenZhen_PMC43X(ReceiptCollector receiptCollector,ReceiptMeter receiptMeter) {
		this.receiptCollector = receiptCollector;
		this.receiptMeter = receiptMeter;
		buildWritingFrames();
	}
	
	
	@Override
	public void buildWritingFrames() {
		//makeRateFrame();
		makeBasicParamFrame();
		makeEnergyFrame();
	}

	//读电表电能的组帧
	private void makeEnergyFrame() {
		int[] data = new int[8];
		data[0] = Integer.parseInt(receiptMeter.getMeterNo());
		data[1] = 0x03;
		data[2] = 0x9C;
		data[3] = 0xA4;
		data[4] = 0x00;
		data[5] = 0x0E;
		int[] crc = CRC.calculateCRC(data, 6);
		data[6] = crc[0];
		data[7] = crc[1];
		byte[] frame = new byte[data.length];
		for (int i = 0; i < data.length; i++) {
			frame[i] = (byte) data[i];
		}
		writingFrames.add(frame);
	}


	//读电表基本参数的组帧
	private void makeBasicParamFrame() {
		int[] data = new int[8];
		data[0] = Integer.parseInt(receiptMeter.getMeterNo());
		data[1] = 0x03;
		data[2] = 0x9C;
		data[3] = 0x40;
		data[4] = 0x00;
		data[5] = 0x39;
		int[] crc = CRC.calculateCRC(data, 6);
		data[6] = crc[0];
		data[7] = crc[1];
		byte[] frame = new byte[data.length];
		for (int i = 0; i < data.length; i++) {
			frame[i] = (byte) data[i];
		}
		writingFrames.add(frame);
	}


	//读变比组帧
	@SuppressWarnings("unused")
	private void makeRateFrame() {
		int[] data = new int[8];
		data[0] = Integer.parseInt(receiptMeter.getMeterNo());
		data[1] = 0x03;
		data[2] = 0xA0;
		data[3] = 0x31;
		data[4] = 0x00;
		data[5] = 0x04;
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
		if (!CRC.isValid(data)){
			return false;
		}
		if (data[2] == 0x08) {
			analyzeRateData(data);
		}else if (data[2] == 0x72) {
			analyzeBasicParam(data);
		}else if (data[2] == 0x1C){
			analyzeEnergyData(data);
		}
		return true;
	}

	//解析电能数据
	private void analyzeEnergyData(int[] data) {
		kwhForward = (data[3] * 256 * 256 * 256 + data[4] * 256 * 256 + data[5] * 256 + data[6]) / 10.0;
		kwhReverse = (data[7] * 256 * 256 * 256 + data[8] * 256 * 256 + data[9] * 256 + data[10]) / 10.0;
		kwh = kwhForward + kwhReverse;
		kvarh1 = (data[15] * 256 * 256 * 256 + data[16] * 256 * 256 + data[17] * 256 + data[18]) / 10.0;
		kvarh2 = (data[19] * 256 * 256 * 256 + data[20] * 256 * 256 + data[21] * 256 + data[22]) / 10.0;
	}


	//解析基本参数
	private void analyzeBasicParam(int[] data) {
		voltA = (data[3] * 256 * 256 * 256 + data[4] * 256 * 256 + data[5] * 256 + data[6]) / 100.0;
		voltB = (data[7] * 256 * 256 * 256 + data[8] * 256 * 256 + data[9] * 256 + data[10]) / 100.0;
		voltC = (data[11] * 256 * 256 * 256 + data[12] * 256 * 256 + data[13] * 256 + data[14]) / 100.0;
		voltAB = (data[19] * 256 * 256 * 256 + data[20] * 256 * 256 + data[21] * 256 + data[22]) / 100.0;
		voltBC = (data[23] * 256 * 256 * 256 + data[24] * 256 * 256 + data[25] * 256 + data[26]) / 100.0;
		voltCA = (data[27] * 256 * 256 * 256 + data[28] * 256 * 256 + data[29] * 256 + data[30]) / 100.0;
		currentA = (data[35] * 256 * 256 * 256 + data[36] * 256 * 256 + data[37] * 256 + data[38]) / 1000.0;
		currentB = (data[39] * 256 * 256 * 256 + data[40] * 256 * 256 + data[41] * 256 + data[42]) / 1000.0;
		currentC = (data[43] * 256 * 256 * 256 + data[44] * 256 * 256 + data[45] * 256 + data[46]) / 1000.0;
		kwA = (data[51] * 256 * 256 * 256 + data[52] * 256 * 256 + data[53] * 256 + data[54]) / 1000.0;
		kwB = (data[55] * 256 * 256 * 256 + data[56] * 256 * 256 + data[57] * 256 + data[58]) / 1000.0;
		kwC = (data[59] * 256 * 256 * 256 + data[60] * 256 * 256 + data[61] * 256 + data[62]) / 1000.0;
		kw = (data[63] * 256 * 256 * 256 + data[64] * 256 * 256 + data[65] * 256 + data[66]) / 1000.0;
		kvarA = (data[67] * 256 * 256 * 256 + data[68] * 256 * 256 + data[69] * 256 + data[70]) / 1000.0;
		kvarB = (data[71] * 256 * 256 * 256 + data[72] * 256 * 256 + data[73] * 256 + data[74]) / 1000.0;
		kvarC = (data[75] * 256 * 256 * 256 + data[76] * 256 * 256 + data[77] * 256 + data[78]) / 1000.0;
		kvar = (data[79] * 256 * 256 * 256 + data[80] * 256 * 256 + data[81] * 256 + data[82]) / 1000.0;
		kvaA = (data[83] * 256 * 256 * 256 + data[84] * 256 * 256 + data[85] * 256 + data[86]) / 1000.0;
		kvaB = (data[87] * 256 * 256 * 256 + data[88] * 256 * 256 + data[89] * 256 + data[90]) / 1000.0;
		kvaC = (data[91] * 256 * 256 * 256 + data[92] * 256 * 256 + data[93] * 256 + data[94]) / 1000.0;
		kva = (data[95] * 256 * 256 * 256 + data[96] * 256 * 256 + data[97] * 256 + data[98]) / 1000.0;
		powerFactorA = (data[99] * 256 + data[100]) / 1000.0;
		powerFactorB = (data[101] * 256 + data[102]) / 1000.0;
		powerFactorC = (data[103] * 256 + data[104]) / 1000.0;
		powerFactor = (data[105] * 256 + data[106]) / 1000.0;
		frequency = (data[107] * 256 + data[108]) / 100.0;
	}


	//解析变比的数据
	private void analyzeRateData(int[] data) {
		Ubb = data[9] * 256 + data[10] + Double.parseDouble("0." + (data[3] * 256 + data[4]));
		Ibb = data[5] * 256 + data[6];
	}

	
	private void resultMutiplyRate() {
		voltA *= Ubb;
		voltB *= Ubb;
		voltC *= Ubb;
		currentA *= Ibb;
		currentB *= Ibb;
		currentC *= Ibb;
		kw = kw * Ubb * Ibb;
		kwA = kwA * Ubb * Ibb;
		kwB = kwB * Ubb * Ibb;
		kwC = kwC * Ubb * Ibb;
		kvar = kvar * Ubb * Ibb;
		kvarA = kvarA * Ubb * Ibb;
		kvarB = kvarB * Ubb * Ibb;
		kvarC = kvarC * Ubb * Ibb;
		kva = kva * Ubb * Ibb;
		kvaA = kvaA * Ubb * Ibb;
		kvaB = kvaB * Ubb * Ibb;
		kvaC = kvaC * Ubb * Ibb;
		kwh = kwh * Ubb * Ibb;
		kwhForward = kwhForward * Ubb * Ibb;
		kwhReverse = kwhReverse * Ubb * Ibb;
		voltAB *= Ubb;
		voltBC *= Ubb;
		voltCA *= Ubb;
		kvarh1 = kvarh1 * Ubb * Ibb;
		kvarh2 = kvarh2 * Ubb * Ibb;
	}

	@Override
	public void handleResult() {
		Date now = new Date();
		List<ReceiptCircuit> receiptCircuitList = services.receiptCircuitService.queryByMeterId(receiptMeter.getId());
		if(receiptCircuitList == null || receiptCircuitList.size() == 0){
			logger.info("dtuNo: " + receiptCollector.getCollectorNo() + " meterNo:" + receiptMeter.getMeterNo() + " not find circuit!");
			return;
		}
		resultMutiplyRate();
		DecimalFormat dfOne = new DecimalFormat("#.#");
		DecimalFormat dfTwo = new DecimalFormat("#.##");
		DecimalFormat dfThree = new DecimalFormat("#.###");
		DataElectricity dataElectricity = new DataElectricity();
		dataElectricity.setReceiptCircuit(receiptCircuitList.get(0));
		dataElectricity.setReadTime(now);
		dataElectricity.setElectricityType(ElectricityType.AC_THREE);
		dataElectricity.setFrequency(Double.parseDouble(dfTwo.format(frequency)));
		dataElectricity.setVoltageA(Double.parseDouble(dfOne.format(voltA)));
		dataElectricity.setVoltageB(Double.parseDouble(dfOne.format(voltB)));
		dataElectricity.setVoltageC(Double.parseDouble(dfOne.format(voltC)));
		dataElectricity.setVoltageAB(Double.parseDouble(dfOne.format(voltAB)));
		dataElectricity.setVoltageBC(Double.parseDouble(dfOne.format(voltBC)));
		dataElectricity.setVoltageCA(Double.parseDouble(dfOne.format(voltCA)));
		dataElectricity.setCurrentA(Double.parseDouble(dfThree.format(currentA)));
		dataElectricity.setCurrentB(Double.parseDouble(dfThree.format(currentB)));
		dataElectricity.setCurrentC(Double.parseDouble(dfThree.format(currentC)));
		dataElectricity.setKva(Double.parseDouble(dfOne.format(kva)));
		dataElectricity.setKvaA(Double.parseDouble(dfOne.format(kvaA)));
		dataElectricity.setKvaB(Double.parseDouble(dfOne.format(kvaB)));
		dataElectricity.setKvaC(Double.parseDouble(dfOne.format(kvaC)));
		dataElectricity.setKw(Double.parseDouble(dfOne.format(kw)));
		dataElectricity.setKwA(Double.parseDouble(dfOne.format(kwA)));
		dataElectricity.setKwB(Double.parseDouble(dfOne.format(kwB)));
		dataElectricity.setKwC(Double.parseDouble(dfOne.format(kwC)));
		dataElectricity.setKvar(Double.parseDouble(dfOne.format(kvar)));
		dataElectricity.setKvarA(Double.parseDouble(dfOne.format(kvarA)));
		dataElectricity.setKvarB(Double.parseDouble(dfOne.format(kvarB)));
		dataElectricity.setKvarC(Double.parseDouble(dfOne.format(kvarC)));
		dataElectricity.setKwh(Double.parseDouble(dfThree.format(kwh)));
		dataElectricity.setKwhForward(Double.parseDouble(dfThree.format(kwhForward)));
		dataElectricity.setKwhReverse(Double.parseDouble(dfThree.format(kwhReverse)));
		dataElectricity.setKvarh1(Double.parseDouble(dfThree.format(kvarh1)));
		dataElectricity.setKvarh2(Double.parseDouble(dfThree.format(kvarh2)));
		dataElectricity.setPowerFactor(Double.parseDouble(dfThree.format(powerFactor)));
		dataElectricity.setPowerFactorA(Double.parseDouble(dfThree.format(powerFactorA)));
		dataElectricity.setPowerFactorB(Double.parseDouble(dfThree.format(powerFactorB)));
		dataElectricity.setPowerFactorC(Double.parseDouble(dfThree.format(powerFactorC)));
		services.dataElectricityService.save(dataElectricity);
	}

}
