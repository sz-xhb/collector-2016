package com.xhb.core.dao;

import org.springframework.stereotype.Repository;

import com.xhb.core.entity.DataLingbu;

@Repository
public class DataLinbuDAO extends ApplicationDAO {

	@Override
	protected Class<?> getPersistentClass() {
		return DataLingbu.class;
	}

}
