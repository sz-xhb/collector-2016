package com.xhb.core.dao;

import org.springframework.stereotype.Repository;

import com.xhb.core.entity.CollectorStatus;

@Repository
public class CollectorStatusDAO extends ApplicationDAO {
	@Override
	protected Class<?> getPersistentClass() {
		return CollectorStatus.class;
	}

}
