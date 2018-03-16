package com.xhb.core.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xhb.core.dao.ApplicationDAO;
import com.xhb.core.dao.DataTemperatureDAO;
import com.xhb.core.service.DataTemperatureService;

@Service
@Transactional
public class DataTemperatureServiceImpl extends ApplicationServiceImpl implements DataTemperatureService {

	@Resource
	private DataTemperatureDAO dataTemperatureDAO;
	@Override
	protected ApplicationDAO getApplicationDAO() {
		return dataTemperatureDAO;
	}

}
