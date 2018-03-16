package com.xhb.core.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xhb.core.dao.ApplicationDAO;
import com.xhb.core.dao.DataElectricityDAO;
import com.xhb.core.service.DataElectricityService;

@Service
@Transactional
public class DataElectricityServiceImpl extends ApplicationServiceImpl implements DataElectricityService {

	@Resource
	private DataElectricityDAO dataElectricityDAO;
	@Override
	protected ApplicationDAO getApplicationDAO() {
		return dataElectricityDAO;
	}

}
