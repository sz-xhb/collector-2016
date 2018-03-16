package com.xhb.core.dao;


import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.xhb.core.entity.ReceiptMeter;

@Repository
public class ReceiptMeterDAO extends ApplicationDAO {
	@Override
	protected Class<?> getPersistentClass() {
		return ReceiptMeter.class;
	}

	@SuppressWarnings("unchecked")
	public List<ReceiptMeter> findByCollectorId(Long collectorId) {
		Query query = sessionFactory.getCurrentSession().createQuery("from ReceiptMeter r where r.receiptCollector.id=:collectorId");
		query.setParameter("collectorId", collectorId);
		return query.list();
	}

	public ReceiptMeter findByDtuNoAndMeterNo(String dtuNo, String meterNo) {
		Query query = sessionFactory.getCurrentSession().createQuery("from ReceiptMeter r where r.receiptCollector.collectorNo=:dtuNo and r.meterNo=:meterNo");
		query.setParameter("dtuNo", dtuNo);
		query.setParameter("meterNo", meterNo);
		return (ReceiptMeter) query.uniqueResult();
	}

}
