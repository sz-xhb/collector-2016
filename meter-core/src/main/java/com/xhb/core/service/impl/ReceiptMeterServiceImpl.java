package com.xhb.core.service.impl;

import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xhb.core.dao.ApplicationDAO;
import com.xhb.core.dao.ReceiptMeterDAO;
import com.xhb.core.entity.ReceiptMeter;
import com.xhb.core.service.ReceiptMeterService;

@Service
@Transactional
public class ReceiptMeterServiceImpl extends ApplicationServiceImpl implements ReceiptMeterService {

	@Resource
	private ReceiptMeterDAO receiptMeterDAO;
	@Override
	protected ApplicationDAO getApplicationDAO() {
		return receiptMeterDAO;
	}
	@Override
	public List<ReceiptMeter> findByCollectorId(Long collectorId) {
		return receiptMeterDAO.findByCollectorId(collectorId);
	}
	@Override
	public ReceiptMeter findByDtuNoAndMeterNo(String dtuNo, String meterNo) {
		return receiptMeterDAO.findByDtuNoAndMeterNo(dtuNo,meterNo);
	}

}
