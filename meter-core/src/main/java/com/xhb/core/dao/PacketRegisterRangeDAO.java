package com.xhb.core.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.xhb.core.entity.PacketRegisterRange;

@Repository
public class PacketRegisterRangeDAO extends ApplicationDAO {

	@Override
	protected Class<?> getPersistentClass() {
		return PacketRegisterRange.class;
	}

	@SuppressWarnings("unchecked")
	public List<PacketRegisterRange> findAll() {
		Query query = sessionFactory.getCurrentSession().createQuery("from PacketRegisterRange");
		return query.list();
	}

}
