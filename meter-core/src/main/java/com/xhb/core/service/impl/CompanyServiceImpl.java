package com.xhb.core.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xhb.core.dao.ApplicationDAO;
import com.xhb.core.dao.CompanyDAO;
import com.xhb.core.entity.Company;
import com.xhb.core.service.CompanyServcie;

@Service
@Transactional
public class CompanyServiceImpl extends ApplicationServiceImpl implements CompanyServcie {

	@Resource
	private CompanyDAO companyDAO;
	@Override
	protected ApplicationDAO getApplicationDAO() {
		return companyDAO;
	}
	@Override
	public Company findByCompanyName(String companyName) {
		return companyDAO.findByCompanyName(companyName);
	}
}
