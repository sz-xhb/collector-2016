package com.xhb.sockserv.meter;

import java.text.SimpleDateFormat;

import com.xhb.core.entity.DataAmHarm;
import com.xhb.core.entity.DataElectricity;
import com.xhb.core.entity.ElectricityType;
import com.xhb.core.entity.ReceiptCircuit;

public class MeterPlusAMHARM extends MeterPlusAbstract {

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
	private Double baseVoltA; 
	private Double baseVoltB; 
	private Double baseVoltC; 
	private Double baseCurrentA;
	private Double baseCurrentB;
	private Double baseCurrentC;
	private Double baseKw;
	private Double baseKwA;
	private Double baseKwB;
	private Double baseKwC;
	private Double harmVoltA;
	private Double harmVoltB;
	private Double harmVoltC;
	private Double harmCurrentA;
	private Double harmCurrentB;
	private Double harmCurrentC;
	private Double harmKw;
	private Double harmKwA;
	private Double harmKwB;
	private Double harmKwC;
	private Double distorUa;
	private Double distorUb;
	private Double distorUc;
	private Double totalVoltDistor;
	private Double distorIa;
	private Double distorIb;
	private Double distorIc;
	private Double totalCurrentDistor;
	
	private Double[] harmUa = new Double[20];
	private Double oddHarmUa;
	private Double evenHarmUa;
	private Double ridgeUa;
	private Double teleUa;
	
	private Double[] harmUb = new Double[20];
	private Double oddHarmUb;
	private Double evenHarmUb;
	private Double ridgeUb;
	private Double teleUb;
	
	private Double[] harmUc = new Double[20];
	private Double oddHarmUc;
	private Double evenHarmUc;
	private Double ridgeUc;
	private Double teleUc;
	
	private Double[] harmIa = new Double[20];
	private Double oddHarmIa;
	private Double evenHarmIa;
	private Double kIa;
	
	private Double[] harmIb = new Double[20];
	private Double oddHarmIb;
	private Double evenHarmIb;
	private Double kIb;
	
	private Double[] harmIc = new Double[20];
	private Double oddHarmIc;
	private Double evenHarmIc;
	private Double kIc;
	
	@Override
	protected void saveEnergyInfo(ReceiptCircuit circuit) throws Exception {
		
		DataElectricity det = new DataElectricity();
		//存储基础数据
		saveBaseData(det,circuit);
		
		//存储谐波数据
		saveHarmData(det,circuit);
		
	}

	/*
	 * 存储基础数据
	 */
	private void saveBaseData(DataElectricity det, ReceiptCircuit circuit) throws Exception{
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

	/*
	 * 存储谐波数据
	 */
	private void saveHarmData(DataElectricity det, ReceiptCircuit circuit) {
		DataAmHarm dah = new DataAmHarm();
		dah.setBaseCurrentA(baseCurrentA);
		dah.setBaseCurrentB(baseCurrentB);
		dah.setBaseCurrentC(baseCurrentC);
		dah.setBaseKw(baseKw);
		dah.setBaseKwA(baseKwA);
		dah.setBaseKwB(baseKwB);
		dah.setBaseKwC(baseKwC);
		dah.setBaseVoltA(baseVoltA);
		dah.setBaseVoltB(baseVoltB);
		dah.setBaseVoltC(baseVoltC);
		dah.setDataElectricity(det);
		dah.setDistorIa(distorIa);
		dah.setDistorIb(distorIb);
		dah.setDistorIc(distorIc);
		dah.setDistorUa(distorUa);
		dah.setDistorUb(distorUb);
		dah.setDistorUc(distorUc);
		dah.setEvenHarmIa(evenHarmIa);
		dah.setEvenHarmIb(evenHarmIb);
		dah.setEvenHarmIc(evenHarmIc);
		dah.setEvenHarmUa(evenHarmUa);
		dah.setEvenHarmUb(evenHarmUb);
		dah.setEvenHarmUc(evenHarmUc);
		dah.setHarmCurrentA(harmCurrentA);
		dah.setHarmCurrentB(harmCurrentB);
		dah.setHarmCurrentC(harmCurrentC);
		dah.setHarmIa2(harmIa[0]);
		dah.setHarmIa3(harmIa[1]);
		dah.setHarmIa4(harmIa[2]);
		dah.setHarmIa5(harmIa[3]);
		dah.setHarmIa6(harmIa[4]);
		dah.setHarmIa7(harmIa[5]);
		dah.setHarmIa8(harmIa[6]);
		dah.setHarmIa9(harmIa[7]);
		dah.setHarmIa10(harmIa[8]);
		dah.setHarmIa11(harmIa[9]);
		dah.setHarmIa12(harmIa[10]);
		dah.setHarmIa13(harmIa[11]);
		dah.setHarmIa14(harmIa[12]);
		dah.setHarmIa15(harmIa[13]);
		dah.setHarmIa16(harmIa[14]);
		dah.setHarmIa17(harmIa[15]);
		dah.setHarmIa18(harmIa[16]);
		dah.setHarmIa19(harmIa[17]);
		dah.setHarmIa20(harmIa[18]);
		dah.setHarmIa21(harmIa[19]);
		dah.setHarmIb2(harmIb[0]);
		dah.setHarmIb3(harmIb[1]);
		dah.setHarmIb4(harmIb[2]);
		dah.setHarmIb5(harmIb[3]);
		dah.setHarmIb6(harmIb[4]);
		dah.setHarmIb7(harmIb[5]);
		dah.setHarmIb8(harmIb[6]);
		dah.setHarmIb9(harmIb[7]);
		dah.setHarmIb10(harmIb[8]);
		dah.setHarmIb11(harmIb[9]);
		dah.setHarmIb12(harmIb[10]);
		dah.setHarmIb13(harmIb[11]);
		dah.setHarmIb14(harmIb[12]);
		dah.setHarmIb15(harmIb[13]);
		dah.setHarmIb16(harmIb[14]);
		dah.setHarmIb17(harmIb[15]);
		dah.setHarmIb18(harmIb[16]);
		dah.setHarmIb19(harmIb[17]);
		dah.setHarmIb20(harmIb[18]);
		dah.setHarmIb21(harmIb[19]);
		dah.setHarmIc2(harmIc[0]);
		dah.setHarmIc3(harmIc[1]);
		dah.setHarmIc4(harmIc[2]);
		dah.setHarmIc5(harmIc[3]);
		dah.setHarmIc6(harmIc[4]);
		dah.setHarmIc7(harmIc[5]);
		dah.setHarmIc8(harmIc[6]);
		dah.setHarmIc9(harmIc[7]);
		dah.setHarmIc10(harmIc[8]);
		dah.setHarmIc11(harmIc[9]);
		dah.setHarmIc12(harmIc[10]);
		dah.setHarmIc13(harmIc[11]);
		dah.setHarmIc14(harmIc[12]);
		dah.setHarmIc15(harmIc[13]);
		dah.setHarmIc16(harmIc[14]);
		dah.setHarmIc17(harmIc[15]);
		dah.setHarmIc18(harmIc[16]);
		dah.setHarmIc19(harmIc[17]);
		dah.setHarmIc20(harmIc[18]);
		dah.setHarmIc21(harmIc[19]);
		dah.setHarmKw(harmKw);
		dah.setHarmKwA(harmKwA);
		dah.setHarmKwB(harmKwB);
		dah.setHarmKwC(harmKwC);
		dah.setHarmUa2(harmUa[0]);
		dah.setHarmUa3(harmUa[1]);
		dah.setHarmUa4(harmUa[2]);
		dah.setHarmUa5(harmUa[3]);
		dah.setHarmUa6(harmUa[4]);
		dah.setHarmUa7(harmUa[5]);
		dah.setHarmUa8(harmUa[6]);
		dah.setHarmUa9(harmUa[7]);
		dah.setHarmUa10(harmUa[8]);
		dah.setHarmUa11(harmUa[9]);
		dah.setHarmUa12(harmUa[10]);
		dah.setHarmUa13(harmUa[11]);
		dah.setHarmUa14(harmUa[12]);
		dah.setHarmUa15(harmUa[13]);
		dah.setHarmUa16(harmUa[14]);
		dah.setHarmUa17(harmUa[15]);
		dah.setHarmUa18(harmUa[16]);
		dah.setHarmUa19(harmUa[17]);
		dah.setHarmUa20(harmUa[18]);
		dah.setHarmUa21(harmUa[19]);
		dah.setHarmUb2(harmUb[0]);
		dah.setHarmUb3(harmUb[1]);
		dah.setHarmUb4(harmUb[2]);
		dah.setHarmUb5(harmUb[3]);
		dah.setHarmUb6(harmUb[4]);
		dah.setHarmUb7(harmUb[5]);
		dah.setHarmUb8(harmUb[6]);
		dah.setHarmUb9(harmUb[7]);
		dah.setHarmUb10(harmUb[8]);
		dah.setHarmUb11(harmUb[9]);
		dah.setHarmUb12(harmUb[10]);
		dah.setHarmUb13(harmUb[11]);
		dah.setHarmUb14(harmUb[12]);
		dah.setHarmUb15(harmUb[13]);
		dah.setHarmUb16(harmUb[14]);
		dah.setHarmUb17(harmUb[15]);
		dah.setHarmUb18(harmUb[16]);
		dah.setHarmUb19(harmUb[17]);
		dah.setHarmUb20(harmUb[18]);
		dah.setHarmUb21(harmUb[19]);
		dah.setHarmUc2(harmUc[0]);
		dah.setHarmUc3(harmUc[1]);
		dah.setHarmUc4(harmUc[2]);
		dah.setHarmUc5(harmUc[3]);
		dah.setHarmUc6(harmUc[4]);
		dah.setHarmUc7(harmUc[5]);
		dah.setHarmUc8(harmUc[6]);
		dah.setHarmUc9(harmUc[7]);
		dah.setHarmUc10(harmUc[8]);
		dah.setHarmUc11(harmUc[9]);
		dah.setHarmUc12(harmUc[10]);
		dah.setHarmUc13(harmUc[11]);
		dah.setHarmUc14(harmUc[12]);
		dah.setHarmUc15(harmUc[13]);
		dah.setHarmUc16(harmUc[14]);
		dah.setHarmUc17(harmUc[15]);
		dah.setHarmUc18(harmUc[16]);
		dah.setHarmUc19(harmUc[17]);
		dah.setHarmUc20(harmUc[18]);
		dah.setHarmUc21(harmUc[19]);
		dah.setkIa(kIa);
		dah.setkIb(kIb);
		dah.setkIc(kIc);
		dah.setOddHarmIa(oddHarmIa);
		dah.setOddHarmIb(oddHarmIb);
		dah.setOddHarmIc(oddHarmIc);
		dah.setHarmVoltA(harmVoltA);
		dah.setHarmVoltB(harmVoltB);
		dah.setHarmVoltC(harmVoltC);
		dah.setOddHarmUa(oddHarmUa);
		dah.setOddHarmUb(oddHarmUb);
		dah.setOddHarmUc(oddHarmUc);
//		dah.setInsertTime(new Date());
//		dah.setReceiptCircuit(circuit);
		dah.setRidgeUa(ridgeUa);
		dah.setRidgeUb(ridgeUb);
		dah.setRidgeUc(ridgeUc);
		dah.setTeleUa(teleUa);
		dah.setTeleUb(teleUb);
		dah.setTeleUc(teleUc);
		dah.setTotalCurrentDistor(totalCurrentDistor);
		dah.setTotalVoltDistor(totalVoltDistor);
		services.dataAmHarmService.save(dah);
	}

	@Override
	protected void analyzeEnergyInfo(String dataInfo,String ProtocolType) {
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
		currentA = convertToSignedInt(Long.parseLong(dataInfo.substring(52, 56),16),100.0);
		currentB = convertToSignedInt(Long.parseLong(dataInfo.substring(56, 60),16),100.0);
		currentC = convertToSignedInt(Long.parseLong(dataInfo.substring(60, 64),16),100.0);
		kw = convertToSignedInt(Long.parseLong(dataInfo.substring(64, 68),16),100.0);
		kwA = convertToSignedInt(Long.parseLong(dataInfo.substring(68, 72),16),100.0);
		kwB = convertToSignedInt(Long.parseLong(dataInfo.substring(72, 76),16),100.0);
		kwC = convertToSignedInt(Long.parseLong(dataInfo.substring(76, 80),16),100.0);
		kvar = convertToSignedInt(Long.parseLong(dataInfo.substring(80, 84),16),100.0);
		kvarA = convertToSignedInt(Long.parseLong(dataInfo.substring(84, 88),16),100.0);
		kvarB = convertToSignedInt(Long.parseLong(dataInfo.substring(88, 92),16),100.0);
		kvarC = convertToSignedInt(Long.parseLong(dataInfo.substring(92, 96),16),100.0);
		kva = convertToSignedInt(Long.parseLong(dataInfo.substring(96, 100),16),100.0);
		kvaA = convertToSignedInt(Long.parseLong(dataInfo.substring(100, 104),16),100.0);
		kvaB = convertToSignedInt(Long.parseLong(dataInfo.substring(104, 108),16),100.0);
		kvaC = convertToSignedInt(Long.parseLong(dataInfo.substring(108, 112),16),100.0);
		powerFactor = convertToSignedInt(Long.parseLong(dataInfo.substring(112, 116),16),100.0);
		powerFactorA = convertToSignedInt(Long.parseLong(dataInfo.substring(116, 120),16),100.0);
		powerFactorB = convertToSignedInt(Long.parseLong(dataInfo.substring(120, 124),16),100.0);
		powerFactorC = convertToSignedInt(Long.parseLong(dataInfo.substring(124, 128),16),100.0);
		//currentZero = Long.parseLong(dataInfo.substring(128, 132),16) / 100.0;
		kwhTotal = Long.parseLong(dataInfo.substring(132, 140),16) / 100.0;
		kwh = Long.parseLong(dataInfo.substring(140, 148),16) / 100.0;
		kwhRev = Long.parseLong(dataInfo.substring(148, 156),16) / 100.0;
		kvarh1 = Long.parseLong(dataInfo.substring(156, 164),16) / 100.0;
		kvarh2 = Long.parseLong(dataInfo.substring(164, 172),16) / 100.0;
		
		baseVoltA = convertToSignedInt(Integer.parseInt(dataInfo.substring(172, 176),16),10.0);
		baseVoltB = convertToSignedInt(Integer.parseInt(dataInfo.substring(176, 180),16) ,10.0);
		baseVoltC = convertToSignedInt(Integer.parseInt(dataInfo.substring(180, 184),16) ,10.0);
		
		baseCurrentA = convertToSignedInt(Integer.parseInt(dataInfo.substring(184, 188),16) ,100.0);
		baseCurrentB = convertToSignedInt(Integer.parseInt(dataInfo.substring(188, 192),16) ,100.0);
		baseCurrentC = convertToSignedInt(Integer.parseInt(dataInfo.substring(192, 196),16) ,100.0);
		
		baseKw = convertToSignedInt(Integer.parseInt(dataInfo.substring(196, 200),16) ,100.0);
		baseKwA = convertToSignedInt(Integer.parseInt(dataInfo.substring(200, 204),16),100.0);
		baseKwB = convertToSignedInt(Integer.parseInt(dataInfo.substring(204, 208),16) ,100.0);
		baseKwC = convertToSignedInt(Integer.parseInt(dataInfo.substring(208, 212),16),100.0);
		
		harmVoltA = Integer.parseInt(dataInfo.substring(212, 216),16) / 10.0;
		harmVoltB = Integer.parseInt(dataInfo.substring(216, 220),16) / 10.0;
		harmVoltC = Integer.parseInt(dataInfo.substring(220, 224),16) / 10.0;
		
		harmCurrentA = convertToSignedInt(Integer.parseInt(dataInfo.substring(224, 228),16) ,100.0);
		harmCurrentB = convertToSignedInt(Integer.parseInt(dataInfo.substring(228, 232),16) ,100.0);
		harmCurrentC = convertToSignedInt(Integer.parseInt(dataInfo.substring(232, 236),16) ,100.0);
		
		harmKw = convertToSignedInt(Integer.parseInt(dataInfo.substring(236, 240),16) ,100.0);
		harmKwA = convertToSignedInt(Integer.parseInt(dataInfo.substring(240, 244),16),100.0);
		harmKwB = convertToSignedInt(Integer.parseInt(dataInfo.substring(244, 248),16) ,100.0);
		harmKwC = convertToSignedInt(Integer.parseInt(dataInfo.substring(248, 252),16) ,100.0);
		
		distorUa = Integer.parseInt(dataInfo.substring(252, 256),16) / 100.0;
		distorUb = Integer.parseInt(dataInfo.substring(256, 260),16) / 100.0;
		distorUc = Integer.parseInt(dataInfo.substring(260, 264),16) / 100.0;
		totalVoltDistor = Integer.parseInt(dataInfo.substring(264, 268),16) / 100.0;
		
		distorIa = Integer.parseInt(dataInfo.substring(268, 272),16) / 100.0;
		distorIb = Integer.parseInt(dataInfo.substring(272, 276),16) / 100.0;
		distorIc = Integer.parseInt(dataInfo.substring(276, 280),16) / 100.0;
		totalCurrentDistor = Integer.parseInt(dataInfo.substring(280, 284),16) / 100.0;
		
		for (int i = 0; i < 20; i++) {
			harmUa[i] = Integer.parseInt(dataInfo.substring(284 + i * 4, 288 + i * 4),16) / 100.0;
		}
		oddHarmUa = Integer.parseInt(dataInfo.substring(364, 368),16) / 100.0;
		evenHarmUa = Integer.parseInt(dataInfo.substring(368, 372),16) / 100.0;
		ridgeUa = Integer.parseInt(dataInfo.substring(372, 376),16) / 100.0;
		teleUa = Integer.parseInt(dataInfo.substring(376, 380),16) / 100.0;
		
		for (int i = 0; i < 20; i++) {
			harmUb[i] = Integer.parseInt(dataInfo.substring(380 + i * 4, 384 + i * 4),16) / 100.0;
		}
		oddHarmUb = Integer.parseInt(dataInfo.substring(460, 464),16) / 100.0;
		evenHarmUb = Integer.parseInt(dataInfo.substring(464, 468),16) / 100.0;
		ridgeUb = Integer.parseInt(dataInfo.substring(468, 472),16) / 100.0;
		teleUb = Integer.parseInt(dataInfo.substring(472, 476),16) / 100.0;
		
		for (int i = 0; i < 20; i++) {
			harmUc[i] = Integer.parseInt(dataInfo.substring(476 + i * 4, 480 + i * 4),16) / 100.0;
		}
		oddHarmUc = Integer.parseInt(dataInfo.substring(556, 560),16) / 100.0;
		evenHarmUc = Integer.parseInt(dataInfo.substring(560, 564),16) / 100.0;
		ridgeUc = Integer.parseInt(dataInfo.substring(564, 568),16) / 100.0;
		teleUc = Integer.parseInt(dataInfo.substring(568, 572),16) / 100.0;
		
		for (int i = 0; i < 20; i++) {
			harmIa[i] = Integer.parseInt(dataInfo.substring(572 + i * 4, 576 + i * 4),16) / 100.0;
		}
		oddHarmIa = Integer.parseInt(dataInfo.substring(652, 656),16) / 100.0;
		evenHarmIa = Integer.parseInt(dataInfo.substring(656, 660),16) / 100.0;
		kIa = Integer.parseInt(dataInfo.substring(660, 664),16) / 100.0;
		
		for (int i = 0; i < 20; i++) {
			harmIb[i] = Integer.parseInt(dataInfo.substring(664 + i * 4, 668 + i * 4),16) / 100.0;
		}
		oddHarmIb = Integer.parseInt(dataInfo.substring(744, 748),16) / 100.0;
		evenHarmIb = Integer.parseInt(dataInfo.substring(748, 752),16) / 100.0;
		kIb = Integer.parseInt(dataInfo.substring(752, 756),16) / 100.0;
		
		for (int i = 0; i < 20; i++) {
			harmIc[i] = Integer.parseInt(dataInfo.substring(756 + i * 4, 760 + i * 4),16) / 100.0;
		}
		oddHarmIc = Integer.parseInt(dataInfo.substring(836, 840),16) / 100.0;
		evenHarmIc = Integer.parseInt(dataInfo.substring(840, 844),16) / 100.0;
		kIc = Integer.parseInt(dataInfo.substring(844, 848),16) / 100.0;
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
