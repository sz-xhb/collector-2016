package com.xhb.core.dao;

import org.springframework.stereotype.Repository;

import com.xhb.core.entity.DataRate;

@Repository
public class DataRateDAO extends ApplicationDAO {

	@Override
	protected Class<?> getPersistentClass() {
		return DataRate.class;
	}

}
