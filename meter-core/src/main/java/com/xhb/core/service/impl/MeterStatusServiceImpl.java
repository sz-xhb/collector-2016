package com.xhb.core.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xhb.core.dao.ApplicationDAO;
import com.xhb.core.dao.MeterStatusDAO;
import com.xhb.core.service.MeterStatusService;

@Service
@Transactional
public class MeterStatusServiceImpl extends ApplicationServiceImpl implements MeterStatusService {

	@Resource
	private MeterStatusDAO meterStatusDAO;
	@Override
	protected ApplicationDAO getApplicationDAO() {
		return meterStatusDAO;
	}

}
