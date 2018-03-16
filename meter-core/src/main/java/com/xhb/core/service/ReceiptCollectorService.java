package com.xhb.core.service;

import com.xhb.core.entity.ReceiptCollector;

public interface ReceiptCollectorService extends ApplicationService {

	ReceiptCollector getByDtuNo(String dtuNo);

	ReceiptCollector getByBuildIdAndCollectorNo(String buildId, String collectorNo);
}
