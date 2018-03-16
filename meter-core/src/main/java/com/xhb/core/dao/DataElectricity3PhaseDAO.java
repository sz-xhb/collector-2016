package com.xhb.core.dao;

import org.springframework.stereotype.Repository;

import com.xhb.core.entity.DataElectricity3Phase;

@Repository
public class DataElectricity3PhaseDAO extends ApplicationDAO {

	@Override
	protected Class<?> getPersistentClass() {
		return DataElectricity3Phase.class;
	}

}
