package com.xhb.core.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xhb.core.dao.ApplicationDAO;
import com.xhb.core.dao.ReceiptCircuitDAO;
import com.xhb.core.entity.ReceiptCircuit;
import com.xhb.core.service.ReceiptCircuitService;

@Service
@Transactional
public class ReceiptCircuitServiceImpl extends ApplicationServiceImpl implements ReceiptCircuitService {

	@Resource
	private ReceiptCircuitDAO receiptCircuitDAO;
	@Override
	protected ApplicationDAO getApplicationDAO() {
		return receiptCircuitDAO;
	}
	@Override
	public ReceiptCircuit findCircuitByDtuNoAndMeterNoAndLoopNo(String dtuNo, String meterNo, Integer loopNo) {
		return receiptCircuitDAO.findCircuitByDtuNoAndMeterNoAndLoopNo(dtuNo,meterNo,loopNo);
	}
	@Override
	public List<ReceiptCircuit> queryByMeterId(Long meterId) {
		return receiptCircuitDAO.queryByMeterId(meterId);
	}
	@Override
	public ReceiptCircuit getByBuildIdAndCollectorNo(String buildId, String collectorNo, String monitorId) {
		return receiptCircuitDAO.getByBuildIdAndCollectorNo(buildId,collectorNo,monitorId);
	}
}
