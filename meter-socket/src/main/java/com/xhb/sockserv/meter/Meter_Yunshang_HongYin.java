package com.xhb.sockserv.meter;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import com.xhb.core.entity.DataElectricity;
import com.xhb.core.entity.ElectricityType;
import com.xhb.core.entity.ReceiptCircuit;
import com.xhb.core.entity.ReceiptCollector;
import com.xhb.core.entity.ReceiptMeter;

public class Meter_Yunshang_HongYin extends AbstractDevice {
	
	private int Ubb = 1;
	private int Ibb = 1;
	private double voltA;
	private double voltB;
	private double voltC;
	private double voltAB;
	private double voltBC;
	private double voltCA;
	private double currentA;
	private double currentB;
	private double currentC;
	private int bit;
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
	
	public Meter_Yunshang_HongYin(ReceiptCollector receiptCollector,ReceiptMeter receiptMeter) {
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
		data[2] = 0x00;
		data[3] = 0x00;
		data[4] = 0x00;
		data[5] = 0x4B;
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
		if (!CRC.isValid(data)){
			return false;
		}
		Ubb = data[7] * 256 + data[8];
		Ibb = data[9] * 256 + data[10];
		voltA = ( data[43] * 256 + data[44] )  / 10.0;
		voltB = ( data[45] * 256 + data[46] )  / 10.0;
		voltC = ( data[47] * 256 + data[48] )  / 10.0;
		voltAB = ( data[49] * 256 + data[50] )  / 10.0;
		voltBC = ( data[51] * 256 + data[52] )  / 10.0;
		voltCA = ( data[53] * 256 + data[54] )  / 10.0;
		currentA = ( data[55] * 256 + data[56] )  / 1000.0;
		currentB = ( data[57] * 256 + data[58] )  / 1000.0;
		currentC = ( data[59] * 256 + data[60] )  / 1000.0;
		bit = data[62];
		kwA = ( data[63] * 256 + data[64] )  / 10000.0 * (((bit & 0x01)== 0x01) ? -1 : 1 );
		kwB = ( data[65] * 256 + data[66] )  / 10000.0 * (((bit & 0x02)== 0x02) ? -1 : 1 );
		kwC = ( data[67] * 256 + data[68] )  / 10000.0 * (((bit & 0x04)== 0x04) ? -1 : 1 );
		kw = ( data[69] * 256 + data[70] ) / 10000.0 *  (((bit & 0x08)== 0x08) ? -1 : 1 );
		kvarA = ( data[71] * 256 + data[72] ) / 10000.0 * (((bit & 0x10)== 0x10) ? -1 : 1 );
		kvarB = ( data[73] * 256 + data[74] ) / 10000.0 * (((bit & 0x20)== 0x20) ? -1 : 1 );
		kvarC = ( data[75] * 256 + data[76] ) / 10000.0 * (((bit & 0x40)== 0x40) ? -1 : 1 );
		kvar = ( data[77] * 256 + data[78] ) / 10000.0  * (((bit & 0x80)== 0x80) ? -1 : 1 );
		kvaA = ( data[79] * 256 + data[80] ) / 10000.0;
		kvaB = ( data[81] * 256 + data[82] ) / 10000.0;
		kvaC = ( data[83] * 256 + data[84] ) / 10000.0;
		kva = ( data[85] * 256 + data[86] ) / 10000.0;
		powerFactorA = ( data[87] * 256 + data[88] ) / 1000.0;
		powerFactorB = ( data[89] * 256 + data[90] ) / 1000.0;
		powerFactorC = ( data[91] * 256 + data[92] ) / 1000.0;
		powerFactor = ( data[93] * 256 + data[94] ) / 1000.0;
		frequency = ( data[95] * 256 + data[96] ) / 100.0;
		
		kwhForward = (( data[97] * 256 + data[98] ) * 0x10000L + (data[99] * 256 + data[100]) 
				+ (data[101] * 256 + data[102]) / 1000.0);
		kwhReverse = (( data[103] * 256 + data[104] ) * 0x10000L + (data[105] * 256 + data[106]) 
				+ (data[107] * 256 + data[108]) / 1000.0);
		kwh = kwhForward + kwhReverse;
		kvarh1 = (( data[109] * 256 + data[110] ) * 0x10000L + (data[111] * 256 + data[112]) 
				+ (data[113] * 256 + data[114]) / 1000.0);
		kvarh2 = (( data[115] * 256 + data[116] ) * 0x10000L + (data[117] * 256 + data[118]) 
				+ (data[119] * 256 + data[120]) / 1000.0);
		return true;
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
		voltAB *= Ubb;
		voltBC *= Ubb;
		voltCA *= Ubb;
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
