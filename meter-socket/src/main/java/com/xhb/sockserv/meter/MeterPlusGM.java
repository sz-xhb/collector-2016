package com.xhb.sockserv.meter;

import java.text.SimpleDateFormat;

import com.xhb.core.entity.DataSteam;
import com.xhb.core.entity.ReceiptCircuit;

public class MeterPlusGM extends MeterPlusAbstract {

	private String time;
	//private String meterStatus;
	private double momentaryFlow;
	private double pressure;
	private double temperature;
	private double flow;

	@Override
	protected void saveEnergyInfo(ReceiptCircuit circuit) throws Exception {
		DataSteam ds = new DataSteam();
		ds.setConsumption(flow);
		ds.setMomentaryFlow(momentaryFlow);
		ds.setPressure(pressure);
		ds.setTemperature(temperature);
		ds.setReceiptCircuit(circuit);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		ds.setReadTime(sdf.parse(time));
		services.dataSteamService.save(ds);
	}

	@Override
	protected void analyzeEnergyInfo(String dataInfo,String ProtocolType) {
		time = "20" + dataInfo.substring(0, 12);
		//meterStatus = dataInfo.substring(12, 16);
		momentaryFlow = Long.parseLong(dataInfo.substring(16, 24),16) / 10000.0;
		pressure = Long.parseLong(dataInfo.substring(40, 48),16) / 10000.0;
		temperature = Long.parseLong(dataInfo.substring(48, 56),16) / 100.0;
		flow = Long.parseLong(dataInfo.substring(104, 112),16);
	}

	@Override
	protected void resultMultiplyRatio(ReceiptCircuit circuit) {
		
	}

}
