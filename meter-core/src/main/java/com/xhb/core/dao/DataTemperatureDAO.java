package com.xhb.core.dao;

import org.springframework.stereotype.Repository;

import com.xhb.core.entity.DataTemperature;

@Repository
public class DataTemperatureDAO extends ApplicationDAO {

	@Override
	protected Class<?> getPersistentClass() {
		return DataTemperature.class;
	}

}
