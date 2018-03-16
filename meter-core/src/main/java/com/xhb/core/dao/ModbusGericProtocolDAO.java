package com.xhb.core.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.xhb.core.entity.ModbusGericProtocol;

@Repository
public class ModbusGericProtocolDAO extends ApplicationDAO {

	@Override
	protected Class<?> getPersistentClass() {
		return ModbusGericProtocol.class;
	}

	@SuppressWarnings("unchecked")
	public List<ModbusGericProtocol> findAll() {
		Query query = sessionFactory.getCurrentSession().createQuery("from ModbusGericProtocol");
		return query.list();
	}

}
