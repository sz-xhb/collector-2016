package com.xhb.core.dao;

import org.springframework.stereotype.Repository;

import com.xhb.core.entity.DataSteam;

@Repository
public class DataSteamDAO extends ApplicationDAO {

	@Override
	protected Class<?> getPersistentClass() {
		return DataSteam.class;
	}

}
