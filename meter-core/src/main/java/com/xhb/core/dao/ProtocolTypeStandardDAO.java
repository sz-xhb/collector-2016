package com.xhb.core.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.xhb.core.entity.ProtocolTypeStandard;

@Repository
public class ProtocolTypeStandardDAO extends ApplicationDAO {

	@Override
	protected Class<?> getPersistentClass() {
		return ProtocolTypeStandard.class;
	}

	@SuppressWarnings("unchecked")
	public List<ProtocolTypeStandard> getAll() {
		Query query = sessionFactory.getCurrentSession().createQuery("from ProtocolTypeStandard");
		return query.list();
	}
}
