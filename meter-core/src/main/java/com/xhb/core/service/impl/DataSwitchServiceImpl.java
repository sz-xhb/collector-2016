package com.xhb.core.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xhb.core.dao.ApplicationDAO;
import com.xhb.core.dao.DataSwitchDAO;
import com.xhb.core.service.DataSwitchService;

@Service
@Transactional
public class DataSwitchServiceImpl extends ApplicationServiceImpl implements DataSwitchService {

	@Resource
	private DataSwitchDAO dataSwitchDAO;
	@Override
	protected ApplicationDAO getApplicationDAO() {
		return dataSwitchDAO;
	}

}
