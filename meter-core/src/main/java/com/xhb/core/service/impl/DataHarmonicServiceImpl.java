package com.xhb.core.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xhb.core.dao.ApplicationDAO;
import com.xhb.core.dao.DataHarmonicDAO;
import com.xhb.core.service.DataHarmonicService;

@Service
@Transactional
public class DataHarmonicServiceImpl extends ApplicationServiceImpl implements DataHarmonicService {

	@Resource
	private DataHarmonicDAO dataHarmonicDAO;
	@Override
	protected ApplicationDAO getApplicationDAO() {
		return dataHarmonicDAO;
	}

}
