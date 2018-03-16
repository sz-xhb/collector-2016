package com.xhb.core.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xhb.core.dao.ApplicationDAO;
import com.xhb.core.dao.DataAmHarmDAO;
import com.xhb.core.service.DataAmHarmService;

@Service
@Transactional
public class DataAmHarmServiceImpl extends ApplicationServiceImpl implements DataAmHarmService {

	@Resource
	private DataAmHarmDAO dataAmHarmDAO;
	@Override
	protected ApplicationDAO getApplicationDAO() {
		return dataAmHarmDAO;
	}

}
