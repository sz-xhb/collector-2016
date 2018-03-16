package com.xhb.sockserv.meter;

import java.text.SimpleDateFormat;

import com.xhb.core.entity.DataWater;
import com.xhb.core.entity.ReceiptCircuit;

public class MeterPlusWater extends MeterPlusAbstract {

	private String time;
	//private String meterStatus;
	private double flow;

	@Override
	protected void saveEnergyInfo(ReceiptCircuit circuit) throws Exception {
		DataWater dw = new DataWater();
		dw.setConsumption(flow);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		dw.setReadTime(sdf.parse(time));
		dw.setReceiptCircuit(circuit);
		services.dataWaterService.save(dw);
	}

	@Override
	protected void analyzeEnergyInfo(String dataInfo,String ProtocolType) {
		time = "20" + dataInfo.substring(0, 12);
		//meterStatus = dataInfo.substring(12, 16);
		flow = Long.parseLong(dataInfo.substring(16, 24)) / 100.0;
	}

	@Override
	protected void resultMultiplyRatio(ReceiptCircuit circuit) {
		
	}

}
