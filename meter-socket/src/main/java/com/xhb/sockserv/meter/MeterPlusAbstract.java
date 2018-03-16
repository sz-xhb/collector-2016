package com.xhb.sockserv.meter;

import com.xhb.core.entity.ReceiptMeter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xhb.core.entity.ReceiptCircuit;
import com.xhb.core.service.Services;
import com.xhb.sockserv.config.ApplicationContext;

public abstract class MeterPlusAbstract {

	protected Services services = ApplicationContext.getServices();
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	protected void handleEnergyInfo(String dtuNo, String meterNo, Integer loopNo, String dataInfo){
		try {
			ReceiptCircuit circuit = services.receiptCircuitService.findCircuitByDtuNoAndMeterNoAndLoopNo(dtuNo,meterNo,loopNo);
			ReceiptMeter meter = services.ReceiptMeterService.findByDtuNoAndMeterNo(dtuNo, meterNo);
			if (circuit == null || meter == null ) {
				logger.info("the circuit of dtuNo:" + dtuNo + "meterNo:" + meterNo + "loopNo:" + loopNo + " is not exist!");
				return;
			}
			analyzeEnergyInfo(dataInfo,meter.getProtocolType());
			resultMultiplyRatio(circuit);
			saveEnergyInfo(circuit);
		} catch (Exception ex) {
			logger.error("the circuit of dtuNo:" + dtuNo + "meterNo:" + meterNo + "loopNo:" 
					+ loopNo + " handle energy data error!", ex);
		}
	}
	
	protected double convertToSignedInt(long data,double rate){
		if ((data & 0x8000) != 0) {
			return (data - 65536) / rate;
		}
		return data / rate;
	}
	protected double convertToUnSignedInt(long data,double rate){
		return data / rate;
	}
	
	protected abstract void saveEnergyInfo(ReceiptCircuit circuit) throws Exception;
	
	protected abstract void analyzeEnergyInfo(String dataInfo,String ProtocolType);
	
	protected abstract void resultMultiplyRatio(ReceiptCircuit circuit);
}
