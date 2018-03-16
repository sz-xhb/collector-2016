package com.xhb.core.service;

import java.util.List;

import com.xhb.core.entity.ReceiptMeter;

public interface ReceiptMeterService extends ApplicationService{

	List<ReceiptMeter> findByCollectorId(Long id);

	ReceiptMeter findByDtuNoAndMeterNo(String dtuNo, String meterNo);
}
