package com.xhb.core.service;

import java.util.List;

import com.xhb.core.entity.ReceiptCircuit;

public interface ReceiptCircuitService extends ApplicationService {

	ReceiptCircuit findCircuitByDtuNoAndMeterNoAndLoopNo(String dtuNo, String meterNo, Integer loopNo);

	List<ReceiptCircuit> queryByMeterId(Long meterId);

	ReceiptCircuit getByBuildIdAndCollectorNo(String buildId, String collectorNo, String monitorId);
	
}
