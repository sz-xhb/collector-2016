package com.xhb.sockserv.meter;

import java.text.SimpleDateFormat;

import com.xhb.core.entity.DataElectricity;
import com.xhb.core.entity.ElectricityType;
import com.xhb.core.entity.ReceiptCircuit;

public class MeterPlusAMDC extends MeterPlusAbstract {

	private String time;
	//private String meterStatus;
	private Integer Ubb = 1;
	private Integer Ibb = 1;
	private double volt;
	private double current;
	private double kw;
	private double kwhTotal;

	@Override
	protected void saveEnergyInfo(ReceiptCircuit circuit) throws Exception {
		DataElectricity det = new DataElectricity();
		det.setCurrent(current);
		det.setElectricityType(ElectricityType.DC);
		det.setVoltage(volt);
		det.setKw(kw);
		det.setKwh(kwhTotal);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		det.setReadTime(sdf.parse(time));
		det.setReceiptCircuit(circuit);
		services.dataElectricityService.save(det);
	}

	@Override
	protected void analyzeEnergyInfo(String dataInfo,String ProtocolType) {
		time = "20" + dataInfo.substring(0, 12);
		//meterStatus = dataInfo.substring(12, 16);
//		Ubb = Integer.parseInt(dataInfo.substring(16, 20),16);
//		Ibb = Integer.parseInt(dataInfo.substring(20, 24),16);
		volt = Long.parseLong(dataInfo.substring(24, 28),16) / 10.0;
		if (ProtocolType.matches(".*2007.*")) {
			current = convertToSignedInt(Long.parseLong(dataInfo.substring(28, 32),16),100.0);
		}else{
			current = convertToUnSignedInt(Long.parseLong(dataInfo.substring(28, 32),16),100.0);
		}
		current = convertToSignedInt(Long.parseLong(dataInfo.substring(28, 32),16),100.0);
		kw = convertToSignedInt(Long.parseLong(dataInfo.substring(32, 36),16),100.0);
		kwhTotal = Long.parseLong(dataInfo.substring(36, 44),16) / 100.0;
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
		kwhTotal *= Ubb * Ibb;
	}

}
