package com.xhb.core.dao;

import org.springframework.stereotype.Repository;

import com.xhb.core.entity.DataWater;

@Repository
public class DataWaterDAO extends ApplicationDAO {

	@Override
	protected Class<?> getPersistentClass() {
		return DataWater.class;
	}

}
