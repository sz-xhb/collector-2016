package com.xhb.core.dao;

import org.springframework.stereotype.Repository;

import com.xhb.core.entity.DataAmHarm;

@Repository
public class DataAmHarmDAO extends ApplicationDAO {

	@Override
	protected Class<?> getPersistentClass() {
		return DataAmHarm.class;
	}

}
