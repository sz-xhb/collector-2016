package com.xhb.core.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xhb.core.dao.ApplicationDAO;
import com.xhb.core.dao.CollectorStatusDAO;
import com.xhb.core.service.CollectorStatusService;

@Service
@Transactional
public class CollectorStatusServiceImpl extends ApplicationServiceImpl implements CollectorStatusService {

	@Resource
	private CollectorStatusDAO collectorStatusDAO;
	@Override
	protected ApplicationDAO getApplicationDAO() {
		return collectorStatusDAO;
	}

}
