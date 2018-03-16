package com.xhb.core.dao;

import org.springframework.stereotype.Repository;

import com.xhb.core.entity.BuildInfo;

@Repository
public class BuildInfoDao extends ApplicationDAO {

	@Override
	protected Class<?> getPersistentClass() {
		return BuildInfo.class;
	}

	
}
