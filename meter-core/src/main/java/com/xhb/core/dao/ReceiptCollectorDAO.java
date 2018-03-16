package com.xhb.core.dao;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.xhb.core.entity.ReceiptCollector;

@Repository
public class ReceiptCollectorDAO extends ApplicationDAO {

	@Override
	protected Class<?> getPersistentClass() {
		return ReceiptCollector.class;
	}

	public ReceiptCollector queryByDtuNo(String dtuNo) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				"from ReceiptCollector r where r.collectorNo=:dtuNo");
		query.setParameter("dtuNo", dtuNo);
		return (ReceiptCollector) query.uniqueResult();
	}

	public ReceiptCollector getByBuildIdAndCollectorNo(String buildId, String collectorNo) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				"from ReceiptCollector r where r.collectorNo=:collectorNo and r.buildInfo=:buildId");
		query.setParameter("collectorNo", collectorNo);
		query.setParameter("buildId", buildId);
		return (ReceiptCollector) query.uniqueResult();
	}

}
