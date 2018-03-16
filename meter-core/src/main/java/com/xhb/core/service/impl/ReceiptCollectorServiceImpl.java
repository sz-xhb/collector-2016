package com.xhb.core.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xhb.core.dao.ApplicationDAO;
import com.xhb.core.dao.ReceiptCollectorDAO;
import com.xhb.core.entity.ReceiptCollector;
import com.xhb.core.service.ReceiptCollectorService;

@Service
@Transactional
public class ReceiptCollectorServiceImpl extends ApplicationServiceImpl
		implements ReceiptCollectorService {

	@Resource
	private ReceiptCollectorDAO receiptCollectorDAO;
	@Override
	protected ApplicationDAO getApplicationDAO() {
		return receiptCollectorDAO;
	}
	@Override
	public ReceiptCollector getByDtuNo(String dtuNo) {
		return receiptCollectorDAO.queryByDtuNo(dtuNo);
	}
	@Override
	public ReceiptCollector getByBuildIdAndCollectorNo(String buildId, String collectorNo) {
		return receiptCollectorDAO.getByBuildIdAndCollectorNo(buildId,collectorNo);
	}
}
