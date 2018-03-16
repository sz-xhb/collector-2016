package com.xhb.core.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xhb.core.dao.ApplicationDAO;
import com.xhb.core.dao.ElecParamAnalyseDAO;
import com.xhb.core.entity.ElecParamAnalyse;
import com.xhb.core.service.ElecParamAnalyseService;

@Service
@Transactional
public class ElecParamAnalyseServiceImpl extends ApplicationServiceImpl implements ElecParamAnalyseService {

	@Resource
	private ElecParamAnalyseDAO elecParamAnalyseDAO;
	@Override
	protected ApplicationDAO getApplicationDAO() {
		return elecParamAnalyseDAO;
	}
	@Override
	public List<ElecParamAnalyse> findAll() {
		return elecParamAnalyseDAO.findAll();
	}

}
