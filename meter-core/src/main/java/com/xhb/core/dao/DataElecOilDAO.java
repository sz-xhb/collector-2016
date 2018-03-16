package com.xhb.core.dao;

import org.springframework.stereotype.Repository;

import com.xhb.core.entity.DataElecOil;

@Repository
public class DataElecOilDAO extends ApplicationDAO {

	@Override
	protected Class<?> getPersistentClass() {
		return DataElecOil.class;
	}

}
