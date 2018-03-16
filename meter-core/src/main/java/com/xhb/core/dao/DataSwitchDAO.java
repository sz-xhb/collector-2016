package com.xhb.core.dao;

import org.springframework.stereotype.Repository;

import com.xhb.core.entity.DataSwitch;

@Repository
public class DataSwitchDAO extends ApplicationDAO {

	@Override
	protected Class<?> getPersistentClass() {
		return DataSwitch.class;
	}

}
