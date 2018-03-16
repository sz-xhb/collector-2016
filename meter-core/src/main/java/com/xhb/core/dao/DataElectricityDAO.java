package com.xhb.core.dao;

import org.springframework.stereotype.Repository;

import com.xhb.core.entity.DataElectricity;

@Repository
public class DataElectricityDAO extends ApplicationDAO {

	@Override
	protected Class<?> getPersistentClass() {
		return DataElectricity.class;
	}

}
