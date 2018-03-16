package com.xhb.core.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xhb.core.dao.ApplicationDAO;
import com.xhb.core.dao.DataWaterDAO;
import com.xhb.core.service.DataWaterService;

@Service
@Transactional
public class DataWaterServiceImpl extends ApplicationServiceImpl implements DataWaterService {

	@Resource
	private DataWaterDAO dataWaterDAO;
	@Override
	protected ApplicationDAO getApplicationDAO() {
		return dataWaterDAO;
	}

}
