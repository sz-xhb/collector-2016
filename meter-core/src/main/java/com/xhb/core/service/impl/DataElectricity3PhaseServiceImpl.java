package com.xhb.core.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xhb.core.dao.ApplicationDAO;
import com.xhb.core.dao.DataElectricity3PhaseDAO;
import com.xhb.core.service.DataElectricity3PhaseService;

@Service
@Transactional
public class DataElectricity3PhaseServiceImpl extends ApplicationServiceImpl implements DataElectricity3PhaseService {

	@Resource
	private DataElectricity3PhaseDAO dataElectricity3PhaseDAO;
	@Override
	protected ApplicationDAO getApplicationDAO() {
		return dataElectricity3PhaseDAO;
	}
}
