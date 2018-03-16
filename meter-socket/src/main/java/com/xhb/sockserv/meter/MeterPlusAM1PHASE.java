package com.xhb.sockserv.meter;

import java.text.SimpleDateFormat;

import com.xhb.core.entity.DataElectricity;
import com.xhb.core.entity.ElectricityType;
import com.xhb.core.entity.ReceiptCircuit;

public class MeterPlusAM1PHASE extends MeterPlusAbstract {
	private String time;
	//private String meterStatus;
	private Integer Ubb = 1;
	private Integer Ibb = 1;
	private double frequency;
	private double volt;
	private double current;
	private double kw;
	private double kvar;
	private double kva;
	private double powerFactor;
	private double kwhTotal;
	private double kwh;
	private double kwhReverse;
	private double kvarh1;
	private double kvarh2;

	@Override
	protected void saveEnergyInfo(ReceiptCircuit circuit) throws Exception {
		DataElectricity det = new DataElectricity();
		det.setCurrent(current);
		det.setElectricityType(ElectricityType.AC_SINGLE);
		det.setFrequency(frequency);
		det.setVoltage(volt);
		det.setKva(kva);
		det.setKvar(kvar);
		det.setKvarh1(kvarh1);
		det.setKvarh2(kvarh2);
		det.setKw(kw);
		det.setKwh(kwhTotal);
		det.setKwhForward(kwh);
		det.setKwhReverse(kwhReverse);
		det.setPowerFactor(powerFactor);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		det.setReadTime(sdf.parse(time));
		det.setReceiptCircuit(circuit);
		services.dataElectricityService.save(det);
	}

	@Override
	protected void analyzeEnergyInfo(String dataInfo,String ProtocolType) {
		time = "20" + dataInfo.substring(0, 12);
		//meterStatus = dataInfo.substring(12, 16);
		Ubb = Integer.parseInt(dataInfo.substring(16, 20),16);
		Ibb = Integer.parseInt(dataInfo.substring(20, 24),16);
		frequency = Long.parseLong(dataInfo.substring(24, 28),16) / 100.0;
		volt = Long.parseLong(dataInfo.substring(28, 32),16) / 10.0;
		if(ProtocolType.matches(".*2007.*")){
			current = convertToSignedInt(Long.parseLong(dataInfo.substring(32, 36),16),100.0);
		}else{
			current = convertToUnSignedInt(Long.parseLong(dataInfo.substring(32, 36),16),100.0);
		}
		kw = convertToSignedInt(Long.parseLong(dataInfo.substring(36, 40),16),100.0);
		kvar = convertToSignedInt(Long.parseLong(dataInfo.substring(40, 44),16),100.0);
		kva = convertToSignedInt(Long.parseLong(dataInfo.substring(44, 48),16),100.0);
		powerFactor = convertToSignedInt(Long.parseLong(dataInfo.substring(48, 52),16),100.0);
		kwhTotal = Long.parseLong(dataInfo.substring(52, 60),16) / 100.0;
		kwh = Long.parseLong(dataInfo.substring(60, 68),16) / 100.0;
		kwhReverse = Long.parseLong(dataInfo.substring(68, 76),16) / 100.0;
		kvarh1 = Long.parseLong(dataInfo.substring(76, 84),16) / 100.0;
		kvarh2 = Long.parseLong(dataInfo.substring(76, 84),16) / 100.0;
	}
	
	
	@Override
	protected void resultMultiplyRatio(ReceiptCircuit circuit) {
		if (circuit.getVoltageRatio() != null) {
			Ubb = circuit.getVoltageRatio();
		}
		if (circuit.getCurrentRatio() != null) {
			Ibb = circuit.getCurrentRatio();
		}
		volt *= Ubb;
		current *= Ibb;
		kw *= Ubb * Ibb;
		kvar *= Ubb * Ibb;
		kva *= Ubb * Ibb;
		kwhTotal *= Ubb * Ibb;
		kwh *= Ubb * Ibb;
		kwhReverse *= Ubb * Ibb;
		kvarh1 *= Ubb * Ibb;
		kvarh2 *= Ubb * Ibb;
	}
}
