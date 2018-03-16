package com.xhb.core.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xhb.core.dao.ApplicationDAO;
import com.xhb.core.dao.DataRateDAO;
import com.xhb.core.service.DataRateService;

@Service
@Transactional
public class DataRateServiceImpl extends ApplicationServiceImpl implements DataRateService {

	@Resource
	private DataRateDAO dataRateDAO;
	@Override
	protected ApplicationDAO getApplicationDAO() {
		return dataRateDAO;
	}

}
