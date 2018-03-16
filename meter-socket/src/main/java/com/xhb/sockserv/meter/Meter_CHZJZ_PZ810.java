package com.xhb.sockserv.meter;

import java.util.Date;
import java.util.List;

import com.xhb.core.entity.DataElectricity;
import com.xhb.core.entity.DataHarmonic;
import com.xhb.core.entity.ElectricityType;
import com.xhb.core.entity.ReceiptCircuit;
import com.xhb.core.entity.ReceiptCollector;
import com.xhb.core.entity.ReceiptMeter;

/*
 * 常州酒店使用的PZ810电表
 * @author jc 改编自CS中的协议 
 * @time 2016-04-12
 */
public class Meter_CHZJZ_PZ810 extends AbstractDevice {

	private double PT1;
	private double PT2;
	private double CT1;
	private double CT2;
	private double kwhForward;
	private double kwhReverse;
	private double kvarh1;
	private double kvarh2;
	private double kwh;
	private double frequency;
	private double voltageA;
	private double voltageB;
	private double voltageC;
	private double voltageAB;
	private double voltageBC;
	private double voltageCA;
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

	private double[] ratioVoltageA = new double[30];

	private double[] ratioVoltageB = new double[30];

	private double[] ratioVoltageC = new double[30];

	private double[] ratioCurrentA = new double[30];

	private double[] ratioCurrentB = new double[30];

	private double[] ratioCurrentC = new double[30];
	
	public Meter_CHZJZ_PZ810(ReceiptCollector receiptCollector, ReceiptMeter receiptMeter) {
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
		data[2] = 0x01;
		data[3] = 0x05;
		data[4] = 0x00;
		data[5] = 0x5B;
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
		data[2] = 0x30;
		data[3] = 0x08;
		data[4] = 0x00;
		data[5] = 0x62;
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
		data[2] = 0x30;
		data[3] = 0x6e;
		data[4] = 0x00;
		data[5] = 0x60;
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
		logger.info("test1");
		if (!CRC.isValid(data))
			return false;
		int meterNo = Integer.parseInt(receiptMeter.getMeterNo()); 
		if(meterNo != data[0]){
			return false;
		}
		logger.info("test2");
		if (data[2] == 0xB6) {
			analyzeFrame1(data);
		}
		if (data[2] == 0xC4) {
			analyzeFrame2(data);
		}
		if (data[2] == 0xC0) {
			analyzeFrame3(data);
		}
		return true;
	}

	private void analyzeFrame1(int[] data) {
		logger.info("test3");
		PT1 = data[3] * 256 * 256 * 256 + data[4] * 256 * 256 + data[5] * 256 + data[6];
		PT2 = data[7] * 256 + data[8];
		CT1 = data[9] * 256 + data[10];
		CT2 = data[11] * 256 + data[12];
		frequency = (data[89] * 256 + data[90]) / 100.0;
		voltageA = (data[91] * 256 + data[92]) * PT1 / (PT2 * 10.0);
		voltageB = (data[93] * 256 + data[94]) * PT1 / (PT2 * 10.0);
		voltageC = (data[95] * 256 + data[96]) * PT1 / (PT2 * 10.0);
		voltageAB = (data[99] * 256 + data[100]) * PT1 / (PT2 * 10.0);
		voltageBC = (data[101] * 256 + data[102]) * PT1 / (PT2 * 10.0);
		voltageCA = (data[103] * 256 + data[104]) * PT1 / (PT2 * 10.0);
		currentA = (data[107] * 256 + data[108]) * CT1 / (CT2 * 1000.0);
		currentB = (data[109] * 256 + data[110]) * CT1 / (CT2 * 1000.0);
		currentC = (data[111] * 256 + data[112]) * CT1 / (CT2 * 1000.0);
		
		byte[] buf = new byte[2];
		buf[0] = (byte)data[117];
		buf[1] = (byte)data[118];
		kwA = setKwValue(buf);
		buf[0] = (byte)data[119];
		buf[1] = (byte)data[120];
		kwB = setKwValue(buf);
		buf[0] = (byte)data[121];
		buf[1] = (byte)data[122];
		kwC = setKwValue(buf);
		buf[0] = (byte)data[123];
		buf[1] = (byte)data[124];
		kw = setKwValue(buf);
		
		buf[0] = (byte)data[125];
		buf[1] = (byte)data[126];
		kvarA = setKwValue(buf);
		buf[0] = (byte)data[127];
		buf[1] = (byte)data[128];
		kvarB = setKwValue(buf);
		buf[0] = (byte)data[129];
		buf[1] = (byte)data[130];
		kvarC = setKwValue(buf);
		buf[0] = (byte)data[131];
		buf[1] = (byte)data[132];
		kvar = setKwValue(buf);
		
		buf[0] = (byte)data[141];
		buf[1] = (byte)data[142];
		powerFactorA = setFactorValue(buf);
		buf[0] = (byte)data[143];
		buf[1] = (byte)data[144];
		powerFactorB = setFactorValue(buf);
		buf[0] = (byte)data[145];
		buf[1] = (byte)data[146];
		powerFactorC = setFactorValue(buf);
		buf[0] = (byte)data[147];
		buf[1] = (byte)data[148];
		powerFactor = setFactorValue(buf);
		
		kwhForward = (data[165] * 256 * 256 * 256 + data[166] * 256 * 256 + data[167] * 256 + data[168]) / 10.0;
		kwhReverse = (data[169] * 256 * 256 * 256 + data[170] * 256 * 256 + data[171] * 256 + data[172]) / 10.0;
		kvarh1 = (data[173] * 256 * 256 * 256 + data[174] * 256 * 256 + data[175] * 256 + data[176]) / 10.0;
		kvarh2 = (data[177] * 256 * 256 * 256 + data[178] * 256 * 256 + data[179] * 256 + data[180]) / 10.0;
		kwh = (data[181] * 256 * 256 * 256 + data[182] * 256 * 256 + data[183] * 256 + data[184]) / 10.0;
	}

	private double setFactorValue(byte[] buf){
		double factor = 0.0;
		if ((buf[0] & 0x80) == 0x80) {
			buf[0] = (byte)~buf[0];
			buf[1] = (byte)~buf[1];
			factor = (buf[0] * 256 + buf[1]) * (-1) / 1000.0;
		}
		else{
			factor = (buf[0] * 256 + buf[1]) / 1000.0;
		}
		return factor;
	}
	private double setKwValue(byte[] buf){
		double pow = 0.0;
		if ((buf[0] & 0x80) == 0x80) {
			buf[0] = (byte)~buf[0];
			buf[1] = (byte)~buf[1];
			pow = (buf[0] * 256 + buf[1]) * PT1 * CT1 * (-1) / (PT2 * CT2 * 1000.0);
		}
		else{
			pow = (buf[0] * 256 + buf[1]) * PT1 * CT1 / (PT2 * CT2 * 1000.0);
		}
		return pow;
	}
	
	private void analyzeFrame2(int[] data) {
		logger.info("test4");
		for(int i=0; i<30; i++){
			ratioVoltageA[i] = (data[i * 2 + 3] * 256 + data[i * 2 + 4]) / 10000.0 * 100;
			ratioVoltageB[i] = (data[i * 2 + 71] * 256 + data[i * 2 + 72]) / 10000.0 * 100;
			ratioVoltageC[i] = (data[i * 2 + 139] * 256 + data[i * 2 + 139]) / 10000.0 * 100;
		}
	}

	private void analyzeFrame3(int[] data) {
		logger.info("test5");
		for(int i=0; i<30; i++){
			ratioCurrentA[i] = (data[i * 2 + 3] * 256 + data[i * 2 + 4]) / 10000.0 * 100;
			ratioCurrentB[i] = (data[i * 2 + 69] * 256 + data[i * 2 + 69]) / 10000.0 * 100;
			ratioCurrentC[i] = (data[i * 2 + 135] * 256 + data[i * 2 + 135]) / 10000.0 * 100;
		}
	}

	@Override
	public void handleResult() {

		Date now = new Date();
		List<ReceiptCircuit> receiptCircuitList = services.receiptCircuitService.queryByMeterId(receiptMeter.getId());
		for (ReceiptCircuit receiptCircuit : receiptCircuitList) {
			DataElectricity dataElectricity = new DataElectricity();
			dataElectricity.setReceiptCircuit(receiptCircuit);
			dataElectricity.setReadTime(now);
			dataElectricity.setElectricityType(ElectricityType.AC_THREE);
			dataElectricity.setFrequency(frequency);
			dataElectricity.setVoltageA(voltageA);
			dataElectricity.setVoltageB(voltageB);
			dataElectricity.setVoltageC(voltageC);
			dataElectricity.setCurrentA(currentA);
			dataElectricity.setCurrentB(currentB);
			dataElectricity.setCurrentC(currentC);
			dataElectricity.setKw(kw);
			dataElectricity.setKwA(kwA);
			dataElectricity.setKwB(kwB);
			dataElectricity.setKwC(kwC);
			dataElectricity.setKvar(kvar);
			dataElectricity.setKvarA(kvarA);
			dataElectricity.setKvarB(kvarB);
			dataElectricity.setKvarC(kvarC);
			dataElectricity.setPowerFactor(powerFactor);
			dataElectricity.setPowerFactorA(powerFactorA);
			dataElectricity.setPowerFactorB(powerFactorB);
			dataElectricity.setPowerFactorC(powerFactorC);
			dataElectricity.setKwh(kwh);
			dataElectricity.setKwhForward(kwhForward);
			dataElectricity.setKwhReverse(kwhReverse);
			dataElectricity.setKvarh1(kvarh1);
			dataElectricity.setKvarh2(kvarh2);
			dataElectricity.setVoltageAB(voltageAB);
			dataElectricity.setVoltageBC(voltageBC);
			dataElectricity.setVoltageCA(voltageCA);
			services.dataElectricityService.save(dataElectricity);

			DataHarmonic dataHarmonic = new DataHarmonic();
			dataHarmonic.setReceiptCircuit(receiptCircuit);
			dataHarmonic.setReadTime(now);
			dataHarmonic.setHr02VoltageA(ratioVoltageA[0]);
			dataHarmonic.setHr03VoltageA(ratioVoltageA[1]);
			dataHarmonic.setHr04VoltageA(ratioVoltageA[2]);
			dataHarmonic.setHr05VoltageA(ratioVoltageA[3]);
			dataHarmonic.setHr06VoltageA(ratioVoltageA[4]);
			dataHarmonic.setHr07VoltageA(ratioVoltageA[5]);
			dataHarmonic.setHr08VoltageA(ratioVoltageA[6]);
			dataHarmonic.setHr09VoltageA(ratioVoltageA[7]);
			dataHarmonic.setHr10VoltageA(ratioVoltageA[8]);
			dataHarmonic.setHr11VoltageA(ratioVoltageA[9]);
			dataHarmonic.setHr12VoltageA(ratioVoltageA[10]);
			dataHarmonic.setHr13VoltageA(ratioVoltageA[11]);
			dataHarmonic.setHr14VoltageA(ratioVoltageA[12]);
			dataHarmonic.setHr15VoltageA(ratioVoltageA[13]);
			dataHarmonic.setHr16VoltageA(ratioVoltageA[14]);
			dataHarmonic.setHr17VoltageA(ratioVoltageA[15]);
			dataHarmonic.setHr18VoltageA(ratioVoltageA[16]);
			dataHarmonic.setHr19VoltageA(ratioVoltageA[17]);
			dataHarmonic.setHr20VoltageA(ratioVoltageA[18]);
			dataHarmonic.setHr21VoltageA(ratioVoltageA[19]);
			dataHarmonic.setHr22VoltageA(ratioVoltageA[20]);
			dataHarmonic.setHr23VoltageA(ratioVoltageA[21]);
			dataHarmonic.setHr24VoltageA(ratioVoltageA[22]);
			dataHarmonic.setHr25VoltageA(ratioVoltageA[23]);
			dataHarmonic.setHr26VoltageA(ratioVoltageA[24]);
			dataHarmonic.setHr27VoltageA(ratioVoltageA[25]);
			dataHarmonic.setHr28VoltageA(ratioVoltageA[26]);
			dataHarmonic.setHr29VoltageA(ratioVoltageA[27]);
			dataHarmonic.setHr30VoltageA(ratioVoltageA[28]);
			dataHarmonic.setHr31VoltageA(ratioVoltageA[29]);

			dataHarmonic.setHr02VoltageB(ratioVoltageB[0]);
			dataHarmonic.setHr03VoltageB(ratioVoltageB[1]);
			dataHarmonic.setHr04VoltageB(ratioVoltageB[2]);
			dataHarmonic.setHr05VoltageB(ratioVoltageB[3]);
			dataHarmonic.setHr06VoltageB(ratioVoltageB[4]);
			dataHarmonic.setHr07VoltageB(ratioVoltageB[5]);
			dataHarmonic.setHr08VoltageB(ratioVoltageB[6]);
			dataHarmonic.setHr09VoltageB(ratioVoltageB[7]);
			dataHarmonic.setHr10VoltageB(ratioVoltageB[8]);
			dataHarmonic.setHr11VoltageB(ratioVoltageB[9]);
			dataHarmonic.setHr12VoltageB(ratioVoltageB[10]);
			dataHarmonic.setHr13VoltageB(ratioVoltageB[11]);
			dataHarmonic.setHr14VoltageB(ratioVoltageB[12]);
			dataHarmonic.setHr15VoltageB(ratioVoltageB[13]);
			dataHarmonic.setHr16VoltageB(ratioVoltageB[14]);
			dataHarmonic.setHr17VoltageB(ratioVoltageB[15]);
			dataHarmonic.setHr18VoltageB(ratioVoltageB[16]);
			dataHarmonic.setHr19VoltageB(ratioVoltageB[17]);
			dataHarmonic.setHr20VoltageB(ratioVoltageB[18]);
			dataHarmonic.setHr21VoltageB(ratioVoltageB[19]);
			dataHarmonic.setHr22VoltageB(ratioVoltageB[20]);
			dataHarmonic.setHr23VoltageB(ratioVoltageB[21]);
			dataHarmonic.setHr24VoltageB(ratioVoltageB[22]);
			dataHarmonic.setHr25VoltageB(ratioVoltageB[23]);
			dataHarmonic.setHr26VoltageB(ratioVoltageB[24]);
			dataHarmonic.setHr27VoltageB(ratioVoltageB[25]);
			dataHarmonic.setHr28VoltageB(ratioVoltageB[26]);
			dataHarmonic.setHr29VoltageB(ratioVoltageB[27]);
			dataHarmonic.setHr30VoltageB(ratioVoltageB[28]);
			dataHarmonic.setHr31VoltageB(ratioVoltageB[29]);

			dataHarmonic.setHr02VoltageC(ratioVoltageC[0]);
			dataHarmonic.setHr03VoltageC(ratioVoltageC[1]);
			dataHarmonic.setHr04VoltageC(ratioVoltageC[2]);
			dataHarmonic.setHr05VoltageC(ratioVoltageC[3]);
			dataHarmonic.setHr06VoltageC(ratioVoltageC[4]);
			dataHarmonic.setHr07VoltageC(ratioVoltageC[5]);
			dataHarmonic.setHr08VoltageC(ratioVoltageC[6]);
			dataHarmonic.setHr09VoltageC(ratioVoltageC[7]);
			dataHarmonic.setHr10VoltageC(ratioVoltageC[8]);
			dataHarmonic.setHr11VoltageC(ratioVoltageC[9]);
			dataHarmonic.setHr12VoltageC(ratioVoltageC[10]);
			dataHarmonic.setHr13VoltageC(ratioVoltageC[11]);
			dataHarmonic.setHr14VoltageC(ratioVoltageC[12]);
			dataHarmonic.setHr15VoltageC(ratioVoltageC[13]);
			dataHarmonic.setHr16VoltageC(ratioVoltageC[14]);
			dataHarmonic.setHr17VoltageC(ratioVoltageC[15]);
			dataHarmonic.setHr18VoltageC(ratioVoltageC[16]);
			dataHarmonic.setHr19VoltageC(ratioVoltageC[17]);
			dataHarmonic.setHr20VoltageC(ratioVoltageC[18]);
			dataHarmonic.setHr21VoltageC(ratioVoltageC[19]);
			dataHarmonic.setHr22VoltageC(ratioVoltageC[20]);
			dataHarmonic.setHr23VoltageC(ratioVoltageC[21]);
			dataHarmonic.setHr24VoltageC(ratioVoltageC[22]);
			dataHarmonic.setHr25VoltageC(ratioVoltageC[23]);
			dataHarmonic.setHr26VoltageC(ratioVoltageC[24]);
			dataHarmonic.setHr27VoltageC(ratioVoltageC[25]);
			dataHarmonic.setHr28VoltageC(ratioVoltageC[26]);
			dataHarmonic.setHr29VoltageC(ratioVoltageC[27]);
			dataHarmonic.setHr30VoltageC(ratioVoltageC[28]);
			dataHarmonic.setHr31VoltageC(ratioVoltageC[29]);

			dataHarmonic.setHr02CurrentA(ratioCurrentA[0]);
			dataHarmonic.setHr03CurrentA(ratioCurrentA[1]);
			dataHarmonic.setHr04CurrentA(ratioCurrentA[2]);
			dataHarmonic.setHr05CurrentA(ratioCurrentA[3]);
			dataHarmonic.setHr06CurrentA(ratioCurrentA[4]);
			dataHarmonic.setHr07CurrentA(ratioCurrentA[5]);
			dataHarmonic.setHr08CurrentA(ratioCurrentA[6]);
			dataHarmonic.setHr09CurrentA(ratioCurrentA[7]);
			dataHarmonic.setHr10CurrentA(ratioCurrentA[8]);
			dataHarmonic.setHr11CurrentA(ratioCurrentA[9]);
			dataHarmonic.setHr12CurrentA(ratioCurrentA[10]);
			dataHarmonic.setHr13CurrentA(ratioCurrentA[11]);
			dataHarmonic.setHr14CurrentA(ratioCurrentA[12]);
			dataHarmonic.setHr15CurrentA(ratioCurrentA[13]);
			dataHarmonic.setHr16CurrentA(ratioCurrentA[14]);
			dataHarmonic.setHr17CurrentA(ratioCurrentA[15]);
			dataHarmonic.setHr18CurrentA(ratioCurrentA[16]);
			dataHarmonic.setHr19CurrentA(ratioCurrentA[17]);
			dataHarmonic.setHr20CurrentA(ratioCurrentA[18]);
			dataHarmonic.setHr21CurrentA(ratioCurrentA[19]);
			dataHarmonic.setHr22CurrentA(ratioCurrentA[20]);
			dataHarmonic.setHr23CurrentA(ratioCurrentA[21]);
			dataHarmonic.setHr24CurrentA(ratioCurrentA[22]);
			dataHarmonic.setHr25CurrentA(ratioCurrentA[23]);
			dataHarmonic.setHr26CurrentA(ratioCurrentA[24]);
			dataHarmonic.setHr27CurrentA(ratioCurrentA[25]);
			dataHarmonic.setHr28CurrentA(ratioCurrentA[26]);
			dataHarmonic.setHr29CurrentA(ratioCurrentA[27]);
			dataHarmonic.setHr30CurrentA(ratioCurrentA[28]);
			dataHarmonic.setHr31CurrentA(ratioCurrentA[29]);

			dataHarmonic.setHr02CurrentB(ratioCurrentB[0]);
			dataHarmonic.setHr03CurrentB(ratioCurrentB[1]);
			dataHarmonic.setHr04CurrentB(ratioCurrentB[2]);
			dataHarmonic.setHr05CurrentB(ratioCurrentB[3]);
			dataHarmonic.setHr06CurrentB(ratioCurrentB[4]);
			dataHarmonic.setHr07CurrentB(ratioCurrentB[5]);
			dataHarmonic.setHr08CurrentB(ratioCurrentB[6]);
			dataHarmonic.setHr09CurrentB(ratioCurrentB[7]);
			dataHarmonic.setHr10CurrentB(ratioCurrentB[8]);
			dataHarmonic.setHr11CurrentB(ratioCurrentB[9]);
			dataHarmonic.setHr12CurrentB(ratioCurrentB[10]);
			dataHarmonic.setHr13CurrentB(ratioCurrentB[11]);
			dataHarmonic.setHr14CurrentB(ratioCurrentB[12]);
			dataHarmonic.setHr15CurrentB(ratioCurrentB[13]);
			dataHarmonic.setHr16CurrentB(ratioCurrentB[14]);
			dataHarmonic.setHr17CurrentB(ratioCurrentB[15]);
			dataHarmonic.setHr18CurrentB(ratioCurrentB[16]);
			dataHarmonic.setHr19CurrentB(ratioCurrentB[17]);
			dataHarmonic.setHr20CurrentB(ratioCurrentB[18]);
			dataHarmonic.setHr21CurrentB(ratioCurrentB[19]);
			dataHarmonic.setHr22CurrentB(ratioCurrentB[20]);
			dataHarmonic.setHr23CurrentB(ratioCurrentB[21]);
			dataHarmonic.setHr24CurrentB(ratioCurrentB[22]);
			dataHarmonic.setHr25CurrentB(ratioCurrentB[23]);
			dataHarmonic.setHr26CurrentB(ratioCurrentB[24]);
			dataHarmonic.setHr27CurrentB(ratioCurrentB[25]);
			dataHarmonic.setHr28CurrentB(ratioCurrentB[26]);
			dataHarmonic.setHr29CurrentB(ratioCurrentB[27]);
			dataHarmonic.setHr30CurrentB(ratioCurrentB[28]);
			dataHarmonic.setHr31CurrentB(ratioCurrentB[29]);

			dataHarmonic.setHr02CurrentC(ratioCurrentC[0]);
			dataHarmonic.setHr03CurrentC(ratioCurrentC[1]);
			dataHarmonic.setHr04CurrentC(ratioCurrentC[2]);
			dataHarmonic.setHr05CurrentC(ratioCurrentC[3]);
			dataHarmonic.setHr06CurrentC(ratioCurrentC[4]);
			dataHarmonic.setHr07CurrentC(ratioCurrentC[5]);
			dataHarmonic.setHr08CurrentC(ratioCurrentC[6]);
			dataHarmonic.setHr09CurrentC(ratioCurrentC[7]);
			dataHarmonic.setHr10CurrentC(ratioCurrentC[8]);
			dataHarmonic.setHr11CurrentC(ratioCurrentC[9]);
			dataHarmonic.setHr12CurrentC(ratioCurrentC[10]);
			dataHarmonic.setHr13CurrentC(ratioCurrentC[11]);
			dataHarmonic.setHr14CurrentC(ratioCurrentC[12]);
			dataHarmonic.setHr15CurrentC(ratioCurrentC[13]);
			dataHarmonic.setHr16CurrentC(ratioCurrentC[14]);
			dataHarmonic.setHr17CurrentC(ratioCurrentC[15]);
			dataHarmonic.setHr18CurrentC(ratioCurrentC[16]);
			dataHarmonic.setHr19CurrentC(ratioCurrentC[17]);
			dataHarmonic.setHr20CurrentC(ratioCurrentC[18]);
			dataHarmonic.setHr21CurrentC(ratioCurrentC[19]);
			dataHarmonic.setHr22CurrentC(ratioCurrentC[20]);
			dataHarmonic.setHr23CurrentC(ratioCurrentC[21]);
			dataHarmonic.setHr24CurrentC(ratioCurrentC[22]);
			dataHarmonic.setHr25CurrentC(ratioCurrentC[23]);
			dataHarmonic.setHr26CurrentC(ratioCurrentC[24]);
			dataHarmonic.setHr27CurrentC(ratioCurrentC[25]);
			dataHarmonic.setHr28CurrentC(ratioCurrentC[26]);
			dataHarmonic.setHr29CurrentC(ratioCurrentC[27]);
			dataHarmonic.setHr30CurrentC(ratioCurrentC[28]);
			dataHarmonic.setHr31CurrentC(ratioCurrentC[29]);

			services.dataHarmonicService.save(dataHarmonic);
		}
	
	}

}
