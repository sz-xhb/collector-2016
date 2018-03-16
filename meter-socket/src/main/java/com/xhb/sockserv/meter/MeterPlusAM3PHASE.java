package com.xhb.sockserv.meter;

import java.text.SimpleDateFormat;
import com.xhb.core.entity.DataElectricity;
import com.xhb.core.entity.ElectricityType;
import com.xhb.core.entity.ReceiptCircuit;

public class MeterPlusAM3PHASE extends MeterPlusAbstract{

	private String time;
	//private String meterStatus;
	private Integer Ubb = 1;
	private Integer Ibb = 1;
	private double frequency;
	private double voltA;
	private double voltB;
	private double voltC;
	private double voltAB;
	private double voltBC;
	private double voltCA;
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
	//private double currentZero;
	private double kwhTotal;
	private double kwh;
	private double kwhRev;
	private double kvarh1;
	private double kvarh2;
	//private double kwhTotalA;
	//private double kwhTotalB;
	//private double kwhTotalC;
	
	//对数据字符串dataInfo按照协议文本进行拆分，给各个设备的参量进行赋值
	protected void analyzeEnergyInfo(String dataInfo,String ProtocolType){
		time = "20" + dataInfo.substring(0, 12);
		//meterStatus = dataInfo.substring(12, 16);
		Ubb = Integer.parseInt(dataInfo.substring(16, 20),16);
		Ibb = Integer.parseInt(dataInfo.substring(20, 24),16);
		frequency = Long.parseLong(dataInfo.substring(24, 28),16) / 100.0;
		voltA = Long.parseLong(dataInfo.substring(28, 32),16) / 10.0;
		voltB = Long.parseLong(dataInfo.substring(32, 36),16) / 10.0;
		voltC = Long.parseLong(dataInfo.substring(36, 40),16) / 10.0;
		voltAB = Long.parseLong(dataInfo.substring(40, 44),16) / 10.0;
		voltBC = Long.parseLong(dataInfo.substring(44, 48),16) / 10.0;
		voltCA = Long.parseLong(dataInfo.substring(48, 52),16) / 10.0;
		if (ProtocolType.matches(".*2007.*")) {
			currentA = convertToSignedInt(Long.parseLong(dataInfo.substring(52, 56), 16), 100.0);
			currentB = convertToSignedInt(Long.parseLong(dataInfo.substring(56, 60), 16), 100.0);
			currentC = convertToSignedInt(Long.parseLong(dataInfo.substring(60, 64), 16), 100.0);
		}else{
			currentA = convertToUnSignedInt(Long.parseLong(dataInfo.substring(52, 56), 16), 100.0);
			currentB = convertToUnSignedInt(Long.parseLong(dataInfo.substring(56, 60), 16), 100.0);
			currentC = convertToUnSignedInt(Long.parseLong(dataInfo.substring(60, 64), 16), 100.0);
		}
		kw = convertToSignedInt(Long.parseLong(dataInfo.substring(64, 68),16) ,100.0);
		kwA = convertToSignedInt(Long.parseLong(dataInfo.substring(68, 72),16) ,100.0);
		kwB = convertToSignedInt(Long.parseLong(dataInfo.substring(72, 76),16) ,100.0);
		kwC = convertToSignedInt(Long.parseLong(dataInfo.substring(76, 80),16) ,100.0);
		kvar = convertToSignedInt(Long.parseLong(dataInfo.substring(80, 84),16) ,100.0);
		kvarA = convertToSignedInt(Long.parseLong(dataInfo.substring(84, 88),16) ,100.0);
		kvarB = convertToSignedInt(Long.parseLong(dataInfo.substring(88, 92),16) ,100.0);
		kvarC = convertToSignedInt(Long.parseLong(dataInfo.substring(92, 96),16) ,100.0);
		kva = convertToSignedInt(Long.parseLong(dataInfo.substring(96, 100),16) ,100.0);
		kvaA = convertToSignedInt(Long.parseLong(dataInfo.substring(100, 104),16) ,100.0);
		kvaB = convertToSignedInt(Long.parseLong(dataInfo.substring(104, 108),16) ,100.0);
		kvaC = convertToSignedInt(Long.parseLong(dataInfo.substring(108, 112),16) ,100.0);
		powerFactor = convertToSignedInt(Long.parseLong(dataInfo.substring(112, 116),16) ,100.0);
		powerFactorA = convertToSignedInt(Long.parseLong(dataInfo.substring(116, 120),16) ,100.0);
		powerFactorB = convertToSignedInt(Long.parseLong(dataInfo.substring(120, 124),16) ,100.0);
		powerFactorC = convertToSignedInt(Long.parseLong(dataInfo.substring(124, 128),16) ,100.0);
		//currentZero = Long.parseLong(dataInfo.substring(128, 132),16) / 100.0;
		kwhTotal = Long.parseLong(dataInfo.substring(132, 140),16) / 100.0;
		kwh = Long.parseLong(dataInfo.substring(140, 148),16) / 100.0;
		kwhRev = Long.parseLong(dataInfo.substring(148, 156),16) / 100.0;
		kvarh1 = Long.parseLong(dataInfo.substring(156, 164),16) / 100.0;
		kvarh2 = Long.parseLong(dataInfo.substring(164, 172),16) / 100.0;
		//kwhTotalA = Long.parseLong(dataInfo.substring(132, 140),16) / 100.0;
		//kwhTotalB = Long.parseLong(dataInfo.substring(132, 140),16) / 100.0;
		//kwhTotalC = Long.parseLong(dataInfo.substring(132, 140),16) / 100.0;
	}
	
	//将分析过后的设备参量存储到数据库
	protected void saveEnergyInfo(ReceiptCircuit circuit) throws Exception{
		DataElectricity det = new DataElectricity();
		det.setCurrentA(currentA);
		det.setCurrentB(currentB);
		det.setCurrentC(currentC);
		det.setElectricityType(ElectricityType.AC_THREE);
		det.setFrequency(frequency);
		det.setKva(kva);
		det.setKvaA(kvaA);
		det.setKvaB(kvaB);
		det.setKvaC(kvaC);
		det.setKvar(kvar);
		det.setKvarA(kvarA);
		det.setKvarB(kvarB);
		det.setKvarC(kvarC);
		det.setKvarh1(kvarh1);
		det.setKvarh2(kvarh2);
		det.setKw(kw);
		det.setKwA(kwA);
		det.setKwB(kwB);
		det.setKwC(kwC);
		det.setKwh(kwhTotal);
		det.setKwhForward(kwh);
		det.setKwhReverse(kwhRev);
		det.setPowerFactor(powerFactor);
		det.setPowerFactorA(powerFactorA);
		det.setPowerFactorB(powerFactorB);
		det.setPowerFactorC(powerFactorC);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		det.setReadTime(sdf.parse(time));
		det.setVoltageA(voltA);
		det.setVoltageB(voltB);
		det.setVoltageC(voltC);
		det.setVoltageAB(voltAB);
		det.setVoltageBC(voltBC);
		det.setVoltageCA(voltCA);
		det.setReceiptCircuit(circuit);
		services.dataElectricityService.save(det);
	}

	@Override
	protected void resultMultiplyRatio(ReceiptCircuit circuit) {
		if (circuit.getVoltageRatio() != null) {
			Ubb = circuit.getVoltageRatio();
		}
		if (circuit.getCurrentRatio() != null) {
			Ibb = circuit.getCurrentRatio();
		}
		voltA *= Ubb;
		voltB *= Ubb;
		voltC *= Ubb;
		voltAB *= Ubb;
		voltBC *= Ubb;
		voltCA *= Ubb;
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
		kwhTotal *= Ubb * Ibb;
		kwh *= Ubb * Ibb;
		kwhRev *= Ubb * Ibb;
		kvarh1 *= Ubb * Ibb;
		kvarh2 *= Ubb * Ibb;
	}

}
