package com.xhb.core.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xhb.core.dao.ApplicationDAO;
import com.xhb.core.dao.DataLinbuDAO;
import com.xhb.core.service.DataLingbuService;

@Service
@Transactional
public class DataLingbuServiceImpl extends ApplicationServiceImpl implements DataLingbuService {

	@Resource
	private DataLinbuDAO dataLingbuDAO;
	@Override
	protected ApplicationDAO getApplicationDAO() {
		return dataLingbuDAO;
	}

}
