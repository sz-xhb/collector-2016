package com.xhb.core.dao;

import org.springframework.stereotype.Repository;

import com.xhb.core.entity.MeterStatus;

@Repository
public class MeterStatusDAO extends ApplicationDAO {

	@Override
	protected Class<?> getPersistentClass() {
		return MeterStatus.class;
	}

}
