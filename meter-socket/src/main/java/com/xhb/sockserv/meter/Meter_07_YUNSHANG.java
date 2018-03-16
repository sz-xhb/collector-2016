package com.xhb.sockserv.meter;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import com.xhb.core.entity.DataElectricity;
import com.xhb.core.entity.ElectricityType;
import com.xhb.core.entity.ReceiptCircuit;
import com.xhb.core.entity.ReceiptCollector;
import com.xhb.core.entity.ReceiptMeter;
import com.xhb.sockserv.util.FrameUtils;

/**
 * 
 * @author xuyahui
 * @Date 2018-03-05
 * @description 支持云尚电力2007国网电表协议
 */
public class Meter_07_YUNSHANG extends AbstractDevice{
	
	private int Ubb = 1;
	private int Ibb = 1;
	private double voltAB;
	private double voltBC;
	private double voltCA;
	private double voltA;
	private double voltB;
	private double voltC;
	private double currentA;
	private double currentB;
	private double currentC;
	private double kw; // 总有功功率
	private double kwA;
	private double kwB;
	private double kwC;
	private double kvar; // 总无功功率
	private double kvarA;
	private double kvarB;
	private double kvarC;
	private double powerFactor; // 总功率因数
	private double powerFactorA; // A相功率因数
	private double powerFactorB; // B相功率因数
	private double powerFactorC; // C相功率因数
	private double kwh;
	private double kwhForward; // 正向有功总电能
	private double kwhReverse; // 反向有功总电能
	private double frequency;
	
	// 以下参数项是在原来新宏博07表基础上添加的
	// xuyahui add,2018-01-12
	private double kvarh1; // 组合无功1总电能
	private double kvarh2; // 组合无功2总电能
	private double kwhForwardRate1; // 正向有功费率1电能（尖）
	private double kwhForwardRate2; // 正向有功费率2电能（峰）
	private double kwhForwardRate3; // 正向有功费率3电能（平）
	private double kwhForwardRate4; // 正向有功费率4电能（谷）
	private double kwhReverseRate1; // 反向有功费率1电能（尖）
	private double kwhReverseRate2; // 反向有功费率2电能（峰）
	private double kwhReverseRate3; // 反向有功费率3电能（平）
	private double kwhReverseRate4; // 反向有功费率4电能（谷）

	private int[] address = new int[6];

	public Meter_07_YUNSHANG(ReceiptCollector receiptCollector, ReceiptMeter receiptMeter) {
		this.receiptCollector = receiptCollector;
		this.receiptMeter = receiptMeter;
		for (int i = 0; i < address.length; i++) {
			address[i] = Integer.parseInt(receiptMeter.getMeterNo().substring(2 * i, 2 * i + 2), 16);
		}
		buildWritingFrames();
	}

	@Override
	public void buildWritingFrames() {
		writingFrames.add(makeFrame(new int[] { 4, 0, 3, 6 })); // 电流变比
		writingFrames.add(makeFrame(new int[] { 4, 0, 3, 7 })); // 电压变比
		writingFrames.add(makeLineVoltFrame());
		writingFrames.add(makeFrame(new int[] { 2, 1, 0xff, 0 })); // 电压数据块（A、B、C相电压）-桢
		writingFrames.add(makeFrame(new int[] { 2, 2, 0xff, 0 })); // 电流数据块（A、B、C相电流）-桢
		writingFrames.add(makeFrame(new int[] { 2, 3, 0xff, 0 })); // 有功功率数据块-桢
		writingFrames.add(makeFrame(new int[] { 2, 4, 0xff, 0 })); // 无功功率数据块-桢
		writingFrames.add(makeFrame(new int[] { 2, 6, 0xff, 0 })); // 功率因数数据块-桢
		writingFrames.add(makeFrame(new int[] { 0, 0, 0, 0 })); // 组合有功总电能-桢
		writingFrames.add(makeFrame(new int[] { 0, 1, 0, 0 })); // 正向有功总电能-桢
		writingFrames.add(makeFrame(new int[] { 0, 2, 0, 0 })); // 反向有功总电能-桢
		writingFrames.add(makeFrame(new int[] { 0, 3, 0, 0 })); // 组合无功1总电能-桢
		writingFrames.add(makeFrame(new int[] { 0, 4, 0, 0 })); // 组合无功2总电能-桢
		writingFrames.add(makeFrame(new int[] { 2, 0x80, 0, 2 })); // 电网频率-桢

		writingFrames.add(makeFrame(new int[] { 0, 1, 1, 0 })); // 正向有功费率1电能（尖）-桢
		writingFrames.add(makeFrame(new int[] { 0, 1, 2, 0 })); // 正向有功费率2电能（峰）-桢
		writingFrames.add(makeFrame(new int[] { 0, 1, 3, 0 })); // 正向有功费率3电能（平）-桢
		writingFrames.add(makeFrame(new int[] { 0, 1, 4, 0 })); // 正向有功费率4电能（谷）-桢
		writingFrames.add(makeFrame(new int[] { 0, 2, 1, 0 })); // 反向有功费率1电能（尖）-桢
		writingFrames.add(makeFrame(new int[] { 0, 2, 2, 0 })); // 反向有功费率2电能（峰）-桢
		writingFrames.add(makeFrame(new int[] { 0, 2, 3, 0 })); // 反向有功费率3电能（平）-桢
		writingFrames.add(makeFrame(new int[] { 0, 2, 4, 0 })); // 反向有功费率4电能（谷）-桢

	}

	private byte[] makeLineVoltFrame() {
		byte[] senddata = null;
		int[] send = new int[16];
		send[0] = 0x68;
		send[1] = address[5];
		send[2] = address[4];
		send[3] = address[3];
		send[4] = address[2];
		send[5] = address[1];
		send[6] = address[0];
		send[7] = 0x68;
		send[8] = 0x1F;
		send[9] = 0x04;
		send[10] = 0x32;
		send[11] = 0x33;
		send[12] = 0x33;
		send[13] = 0x35;
		send[14] = calculateCS(send, 14);
		send[15] = 0x16;
		senddata = new byte[16];
		for (int i = 0; i < 16; i++) {
			senddata[i] = (byte) send[i];
		}
		return senddata;
	}

	private byte[] makeFrame(int[] ident) {
		byte[] senddata = null;

		if (ident.length != 4)
			return senddata;

		int[] send = new int[16];

		send[0] = 0x68;
		send[1] = address[5];
		send[2] = address[4];
		send[3] = address[3];
		send[4] = address[2];
		send[5] = address[1];
		send[6] = address[0];
		send[7] = 0x68;
		send[8] = 0x11; // 请求读电能表数据
		send[9] = 0x04;
		send[10] = ident[3] + 0x33;
		send[11] = ident[2] + 0x33;
		send[12] = ident[1] + 0x33;
		send[13] = ident[0] + 0x33;
		send[14] = calculateCS(send, 14);
		send[15] = 0x16;
		senddata = new byte[16];
		for (int i = 0; i < 16; i++) {
			senddata[i] = (byte) send[i];
		}
		return senddata;
	}

	private boolean isValid(int[] data) {
		if (data.length < 12)
			return false;

		if (data[0] != 0x68 || data[7] != 0x68 || data[data.length - 1] != 0x16)
			return false;

		int identLength = data[9];
		if ((identLength + 12) != data.length)
			return false;

		int cs = calculateCS(data, data.length - 2);
		if (cs == data[data.length - 2])
			return true;
		else
			return false;
	}

	private int calculateCS(int[] data, int length) {
		int sum = 0;

		for (int i = 0; i < length; i++) {
			sum += data[i];
		}

		sum = sum % 256;

		return sum;
	}

	@SuppressWarnings("unused")
	private String parseData(int[] validData) {
		StringBuilder builder = new StringBuilder();
		for (int i = 7; i > 3; i--) {
			if (validData[i] < 0x10) {
				builder.append('0');
			}
			builder.append(Integer.toHexString(validData[i]));
			if (i == 5) {
				builder.append('.');
			}
		}
		return builder.toString();
	}

	private int hexToTenData(int data) {
		return Integer.parseInt(Integer.toHexString(data));
	}

	@Override
	public boolean analyzeFrame(byte[] frame) {
		// 调试代码
		// System.out.println(FrameUtils.toString(frame));
		
		if (readingFrames.size() != 22) {
			return false;
		}
		int[] data = new int[frame.length - 9];
		for (int i = 0; i < data.length; i++) {
			data[i] = frame[i + 8] & 0xFF;
		}

		int num = 0;
		while (num < data.length && data[num] != 0x68) {
			num++;
		}

		int[] data2 = new int[data.length - num];
		for (int i = 0; i < data2.length; i++) {
			data2[i] = data[i + num];
		}
		if (!isValid(data2)) {
			return false;
		}
		int dataLen = data2[9];
		int[] validData = new int[dataLen];
		for (int i = 0; i < dataLen; i++) {
			if (data2[i + 10] < 0x33)
				validData[i] = data2[i + 10] + 0x01 + 0xFF - 0x33;
			else
				validData[i] = data2[i + 10] - 0x33;
		}
		if(validData.length < 4){
			return true;
		}
		// 电流变比
		if (validData[3] == 0x04 && validData[2] == 0x00 && validData[1] == 0x03 && validData[0] == 0x06) {
			int[] arr = new int[] { validData[4], validData[5], validData[6] };
			Ibb = FrameUtils.toBCDCode07(arr);
		}
		// 电压变比
		else if (validData[3] == 0x04 && validData[2] == 0x00 && validData[1] == 0x03 && validData[0] == 0x07) {
			int[] arr = new int[] { validData[4], validData[5], validData[6] };
			Ubb = FrameUtils.toBCDCode07(arr);
		}
		// 线电压
		else if (validData[3] == 0x02 && validData[2] == 0x00 && validData[1] == 0x00 && validData[0] == 0xff) {
			voltAB = (hexToTenData(validData[4]) + hexToTenData(validData[5]) * 100) / 10.0;
			voltBC = (hexToTenData(validData[6]) + hexToTenData(validData[7]) * 100) / 10.0;
			voltCA = (hexToTenData(validData[8]) + hexToTenData(validData[9]) * 100) / 10.0;
		}
		// 相电压
		else if (validData[3] == 0x02 && validData[2] == 0x01 && validData[1] == 0xff && validData[0] == 0x00) {
			voltA = (hexToTenData(validData[4]) + hexToTenData(validData[5]) * 100) / 10.0;
			voltB = (hexToTenData(validData[6]) + hexToTenData(validData[7]) * 100) / 10.0;
			voltC = (hexToTenData(validData[8]) + hexToTenData(validData[9]) * 100) / 10.0;
		}
		// 电流
		else if (validData[3] == 0x02 && validData[2] == 0x02 && validData[1] == 0xff && validData[0] == 0x00) {
			currentA = doGetCurrent(new int[] { validData[4], validData[5], validData[6] });
			currentB = doGetCurrent(new int[] { validData[7], validData[8], validData[9] });
			currentC = doGetCurrent(new int[] { validData[10], validData[11], validData[12] });
		}
		// 功率
		else if (validData[3] == 0x02 && validData[2] == 0x03 && validData[1] == 0xff && validData[0] == 0x00) {
			kw = doGetPow(new int[] { validData[4], validData[5], validData[6] });
			kwA = doGetPow(new int[] { validData[7], validData[8], validData[9] });
			kwB = doGetPow(new int[] { validData[10], validData[11], validData[12] });
			kwC = doGetPow(new int[] { validData[13], validData[14], validData[15] });
		}
		// 无功功率
		else if (validData[3] == 0x02 && validData[2] == 0x04 && validData[1] == 0xff && validData[0] == 0x00) {
			kvar = doGetPow(new int[] { validData[4], validData[5], validData[6] });
			kvarA = doGetPow(new int[] { validData[7], validData[8], validData[9] });
			kvarB = doGetPow(new int[] { validData[10], validData[11], validData[12] });
			kvarC = doGetPow(new int[] { validData[13], validData[14], validData[15] });
		}
		// 功率因数
		else if (validData[3] == 0x02 && validData[2] == 0x06 && validData[1] == 0xff && validData[0] == 0x00) {
			powerFactor = doGetPowerFactor(new int[] { validData[4], validData[5] });
			powerFactor = (hexToTenData(validData[4]) + hexToTenData(validData[5]) * 100) / 1000.0;
			powerFactorA = (hexToTenData(validData[6]) + hexToTenData(validData[7]) * 100) / 1000.0;
			powerFactorB = (hexToTenData(validData[8]) + hexToTenData(validData[9]) * 100) / 1000.0;
			powerFactorC = (hexToTenData(validData[10]) + hexToTenData(validData[11]) * 100) / 1000.0;
		}
		// 频率
		else if (validData[3] == 0x02 && validData[2] == 0x80 && validData[1] == 0x00 && validData[0] == 0x02) {
			frequency = (hexToTenData(validData[4]) + hexToTenData(validData[5]) * 100) / 100.0;
		}
		// 组合有功总电能
		else if (validData[3] == 0x00 && validData[2] == 0x00 && validData[1] == 0x00 && validData[0] == 0x00) {
			kwh = doGetEnergy(new int[] { validData[4], validData[5], validData[6], validData[7] });
		}
		// 正向有功总电能
		else if (validData[3] == 0x00 && validData[2] == 0x01 && validData[1] == 0x00 && validData[0] == 0x00) {
			kwhForward = doGetEnergy(new int[] { validData[4], validData[5], validData[6], validData[7] });
		}
		// 反向有功总电能
		else if (validData[3] == 0x00 && validData[2] == 0x02 && validData[1] == 0x00 && validData[0] == 0x00) {
			kwhReverse = doGetEnergy(new int[] { validData[4], validData[5], validData[6], validData[7] });
		}
		// 组合无功1总电能
		else if (validData[3] == 0x00 && validData[2] == 0x03 && validData[1] == 0x00 && validData[0] == 0x00) {
			kvarh1 = doGetEnergy(new int[] { validData[4], validData[5], validData[6], validData[7] });
		}
		// 组合无功2总电能
		else if (validData[3] == 0x00 && validData[2] == 0x04 && validData[1] == 0x00 && validData[0] == 0x00) {
			kvarh2 = doGetEnergy(new int[] { validData[4], validData[5], validData[6], validData[7] });
		}
		// 正向有功费率1电能
		else if (validData[3] == 0x00 && validData[2] == 0x01 && validData[1] == 0x01 && validData[0] == 0x00) {
			kwhForwardRate1 = doGetEnergy(new int[] { validData[4], validData[5], validData[6], validData[7] });
		}
		// 正向有功费率2电能
		else if (validData[3] == 0x00 && validData[2] == 0x01 && validData[1] == 0x02 && validData[0] == 0x00) {
			kwhForwardRate2 = doGetEnergy(new int[] { validData[4], validData[5], validData[6], validData[7] });
		}
		// 正向有功费率3电能
		else if (validData[3] == 0x00 && validData[2] == 0x01 && validData[1] == 0x03 && validData[0] == 0x00) {
			kwhForwardRate3 = doGetEnergy(new int[] { validData[4], validData[5], validData[6], validData[7] });
		}
		// 正向有功费率4电能
		else if (validData[3] == 0x00 && validData[2] == 0x01 && validData[1] == 0x04 && validData[0] == 0x00) {
			kwhForwardRate4 = doGetEnergy(new int[] { validData[4], validData[5], validData[6], validData[7] });
		}
		// 反向有功费率1电能
		else if (validData[3] == 0x00 && validData[2] == 0x02 && validData[1] == 0x01 && validData[0] == 0x00) {
			kwhReverseRate1 = doGetEnergy(new int[] { validData[4], validData[5], validData[6], validData[7] });
		}
		// 反向有功费率2电能
		else if (validData[3] == 0x00 && validData[2] == 0x02 && validData[1] == 0x02 && validData[0] == 0x00) {
			kwhReverseRate2 = doGetEnergy(new int[] { validData[4], validData[5], validData[6], validData[7] });
		}
		// 反向有功费率3电能
		else if (validData[3] == 0x00 && validData[2] == 0x02 && validData[1] == 0x03 && validData[0] == 0x00) {
			kwhReverseRate3 = doGetEnergy(new int[] { validData[4], validData[5], validData[6], validData[7] });
		}
		// 反向有功费率4电能
		else if (validData[3] == 0x00 && validData[2] == 0x02 && validData[1] == 0x04 && validData[0] == 0x00) {
			kwhReverseRate4 = doGetEnergy(new int[] { validData[4], validData[5], validData[6], validData[7] });
		}
		return true;
	}

	private double doGetEnergy(int[] arr) {
		int pf = FrameUtils.toBCDCode07(arr);
//		if (pf >= 800000) {
//			pf = 800000 - pf;
//		}
		return (pf / 100.0);
	}

	private double doGetPowerFactor(int[] arr) {
		int pf = FrameUtils.toBCDCode07(arr);
		if (pf >= 800000) {
			pf = 800000 - pf;
		}
		return (pf / 1000.0);
	}

	private double doGetCurrent(int[] arr) {
		int current = FrameUtils.toBCDCode07(arr);
		if (current >= 800000) {
			current = 800000 - current;
		}
		return (current / 1000.0);
	}

	private double doGetPow(int[] arr) {
		int power = FrameUtils.toBCDCode07(arr);
		if (power >= 800000) {
			power = 800000 - power;
		}
		return (power / 10000.0);
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
		kwh = kwh * Ubb * Ibb;
		kwhForward = kwhForward * Ubb * Ibb;
		kwhReverse = kwhReverse * Ubb * Ibb;
		kvarh1 = kvarh1 * Ubb * Ibb;
		kvarh2 = kvarh2 * Ubb * Ibb;
		kwhForwardRate1 = kwhForwardRate1 * Ubb * Ibb;
		kwhForwardRate2 = kwhForwardRate2 * Ubb * Ibb;
		kwhForwardRate3 = kwhForwardRate3 * Ubb * Ibb;
		kwhForwardRate4 = kwhForwardRate4 * Ubb * Ibb;
		kwhReverseRate1 = kwhReverseRate1 * Ubb * Ibb;
		kwhReverseRate2 = kwhReverseRate2 * Ubb * Ibb;
		kwhReverseRate3 = kwhReverseRate3 * Ubb * Ibb;
		kwhReverseRate4 = kwhReverseRate4 * Ubb * Ibb;
  		voltAB *= Ubb;
		voltBC *= Ubb;
		voltCA *= Ubb;
		DecimalFormat df = new DecimalFormat("######0.00");
		kwh = Double.parseDouble(df.format(kwh));
		kwhForward = Double.parseDouble(df.format(kwhForward));
		kwhReverse = Double.parseDouble(df.format(kwhReverse));
		kvarh1 = Double.parseDouble(df.format(kvarh1));
		kvarh2 = Double.parseDouble(df.format(kvarh2));
		kwhForwardRate1 = Double.parseDouble(df.format(kwhForwardRate1));
		kwhForwardRate2 = Double.parseDouble(df.format(kwhForwardRate2));
		kwhForwardRate3 = Double.parseDouble(df.format(kwhForwardRate3));
		kwhForwardRate4 = Double.parseDouble(df.format(kwhForwardRate4));
		kwhReverseRate1 = Double.parseDouble(df.format(kwhReverseRate1));
		kwhReverseRate2 = Double.parseDouble(df.format(kwhReverseRate2));
		kwhReverseRate3 = Double.parseDouble(df.format(kwhReverseRate3));
		kwhReverseRate4 = Double.parseDouble(df.format(kwhReverseRate4));
		// voltA = Double.parseDouble(df.format(voltA));
		// voltB = Double.parseDouble(df.format(voltB));
		// voltC = Double.parseDouble(df.format(voltC));
		DecimalFormat df1 = new DecimalFormat("######0.000");
		currentA = Double.parseDouble(df1.format(currentA));
		currentB = Double.parseDouble(df1.format(currentB));
		currentC = Double.parseDouble(df1.format(currentC));
		DecimalFormat df2 = new DecimalFormat("######0.0000");
		kw = Double.parseDouble(df2.format(kw));
		kwA = Double.parseDouble(df2.format(kwA));
		kwB = Double.parseDouble(df2.format(kwB));
		kwC = Double.parseDouble(df2.format(kwC));
		kvar = Double.parseDouble(df2.format(kvar));
		kvarA = Double.parseDouble(df2.format(kvarA));
		kvarB = Double.parseDouble(df2.format(kvarB));
		kvarC = Double.parseDouble(df2.format(kvarC));
	}

	@Override
	public void handleResult() {
		Date now = new Date();
		List<ReceiptCircuit> receiptCircuitList = services.receiptCircuitService.queryByMeterId(receiptMeter.getId());
		for (ReceiptCircuit receiptCircuit : receiptCircuitList) {
			if (config.isRateFromDataBaseEnabled()) {
				if (receiptCircuit.getVoltageRatio() != null && receiptCircuit.getVoltageRatio() != 0) {
					Ubb = receiptCircuit.getVoltageRatio();
				}
				if (receiptCircuit.getCurrentRatio() != null && receiptCircuit.getCurrentRatio() != 0) {
					Ibb = receiptCircuit.getCurrentRatio();
				}
			}
			resultMutiplyRate();
			DataElectricity dataElectricity = new DataElectricity();
			dataElectricity.setReceiptCircuit(receiptCircuit);
			dataElectricity.setReadTime(now);
			dataElectricity.setElectricityType(ElectricityType.AC_THREE);
			dataElectricity.setCurrentA(currentA);
			dataElectricity.setCurrentB(currentB);
			dataElectricity.setCurrentC(currentC);
			dataElectricity.setVoltageA(voltA);
			dataElectricity.setVoltageB(voltB);
			dataElectricity.setVoltageC(voltC);
			dataElectricity.setPowerFactor(powerFactor);
			dataElectricity.setPowerFactorA(powerFactorA);
			dataElectricity.setPowerFactorB(powerFactorB);
			dataElectricity.setPowerFactorC(powerFactorC);
			dataElectricity.setKw(kw);
			dataElectricity.setKwA(kwA);
			dataElectricity.setKwB(kwB);
			dataElectricity.setKwC(kwC);
			dataElectricity.setKvar(kvar);
			dataElectricity.setKvarA(kvarA);
			dataElectricity.setKvarB(kvarB);
			dataElectricity.setKvarC(kvarC);
			dataElectricity.setFrequency(frequency);
			dataElectricity.setKwh(kwh);
			dataElectricity.setKwhForward(kwhForward);
			dataElectricity.setKwhReverse(kwhReverse);
			dataElectricity.setKvarh1(kvarh1);
			dataElectricity.setKvarh2(kvarh2);
			dataElectricity.setKwhForwardRate1(kwhForwardRate1);
			dataElectricity.setKwhForwardRate2(kwhForwardRate2);
			dataElectricity.setKwhForwardRate3(kwhForwardRate3);
			dataElectricity.setKwhForwardRate4(kwhForwardRate4);
			dataElectricity.setKwhReverseRate1(kwhReverseRate1);
			dataElectricity.setKwhReverseRate2(kwhReverseRate2);
			dataElectricity.setKwhReverseRate3(kwhReverseRate3);
			dataElectricity.setKwhReverseRate4(kwhReverseRate4);
			dataElectricity.setVoltageAB(voltAB);
			dataElectricity.setVoltageBC(voltBC);
			dataElectricity.setVoltageCA(voltCA);
			services.dataElectricityService.save(dataElectricity);
		}
	}
	
}
