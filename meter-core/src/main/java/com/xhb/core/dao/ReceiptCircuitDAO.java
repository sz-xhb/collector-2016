package com.xhb.core.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.xhb.core.entity.ReceiptCircuit;

@Repository
public class ReceiptCircuitDAO extends ApplicationDAO {

	@Override
	protected Class<?> getPersistentClass() {
		return ReceiptCircuit.class;
	}

	public ReceiptCircuit findCircuitByDtuNoAndMeterNoAndLoopNo(String dtuNo, String meterNo, Integer loopNo) {
		String sqlStr = "SELECT * FROM `receipt_circuit` cir "
				+ "LEFT JOIN receipt_meter me on cir.meter_id = me.id "
				+ "left JOIN receipt_collector co on me.collector_id = co.id "
				+ "where cir.circuit_no = '" + loopNo +"' and me.meter_no = '" + meterNo 
				+ "' and co.collector_no = '" + dtuNo +"';";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sqlStr).addEntity(ReceiptCircuit.class);
		return (ReceiptCircuit) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<ReceiptCircuit> queryByMeterId(Long meterId) {
		Query query = sessionFactory.getCurrentSession().createQuery("from ReceiptCircuit r where r.receiptMeter.id=:meterId");
		query.setParameter("meterId", meterId);
		return query.list();
	}

	public ReceiptCircuit getByBuildIdAndCollectorNo(String buildId, String collectorNo, String monitorId) {
		String sqlStr = "SELECT * FROM `receipt_circuit` cir "
				+ " LEFT JOIN receipt_meter me on cir.meter_id = me.id "
				+ " left JOIN receipt_collector co on me.collector_id = co.id "
				+ " where cir.monitor_id = '" + monitorId 
				+ "' and co.collector_no = '" + collectorNo +"';"
				+ " and co.build_id = '" + buildId + "'";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sqlStr).addEntity(ReceiptCircuit.class);
		return (ReceiptCircuit) query.uniqueResult();
	}

}
