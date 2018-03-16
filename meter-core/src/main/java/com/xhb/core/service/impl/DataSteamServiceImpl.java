package com.xhb.core.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xhb.core.dao.ApplicationDAO;
import com.xhb.core.dao.DataSteamDAO;
import com.xhb.core.service.DataSteamService;

@Service
@Transactional
public class DataSteamServiceImpl extends ApplicationServiceImpl implements DataSteamService {

	@Resource
	private DataSteamDAO dataSteamDAO;
	@Override
	protected ApplicationDAO getApplicationDAO() {
		return dataSteamDAO;
	}

}
