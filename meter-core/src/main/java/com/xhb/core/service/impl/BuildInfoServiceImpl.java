package com.xhb.core.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xhb.core.dao.ApplicationDAO;
import com.xhb.core.dao.BuildInfoDao;

@Service
@Transactional
public class BuildInfoServiceImpl extends ApplicationServiceImpl implements com.xhb.core.service.BuildInfoService {

	@Resource
	private BuildInfoDao buildInfoDao;
	
	@Override
	protected ApplicationDAO getApplicationDAO() {
		return buildInfoDao;
	}

}
