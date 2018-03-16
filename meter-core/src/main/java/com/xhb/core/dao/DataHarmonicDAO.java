package com.xhb.core.dao;

import org.springframework.stereotype.Repository;

import com.xhb.core.entity.DataHarmonic;

@Repository
public class DataHarmonicDAO extends ApplicationDAO {

	@Override
	protected Class<?> getPersistentClass() {
		return DataHarmonic.class;
	}

}
