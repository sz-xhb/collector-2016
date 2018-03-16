package com.xhb.core.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.xhb.core.entity.ElecParamAnalyse;

@Repository
public class ElecParamAnalyseDAO extends ApplicationDAO {

	@Override
	protected Class<?> getPersistentClass() {
		return ElecParamAnalyse.class;
	}

	@SuppressWarnings("unchecked")
	public List<ElecParamAnalyse> findAll() {
		Query query = sessionFactory.getCurrentSession().createQuery("from ElecParamAnalyse");
		return query.list();
	}

}
