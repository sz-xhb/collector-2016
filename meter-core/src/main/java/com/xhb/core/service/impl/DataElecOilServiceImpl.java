package com.xhb.core.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xhb.core.dao.ApplicationDAO;
import com.xhb.core.dao.DataElecOilDAO;
import com.xhb.core.service.DataElecOilService;

@Service
@Transactional
public class DataElecOilServiceImpl extends ApplicationServiceImpl implements DataElecOilService {

	@Resource
	private DataElecOilDAO dataElecOilDAO;
	@Override
	protected ApplicationDAO getApplicationDAO() {
		return dataElecOilDAO;
	}

}
