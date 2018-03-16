package com.xhb.core.service;

import com.xhb.core.entity.Company;

public interface CompanyServcie extends ApplicationService {

	Company findByCompanyName(String companyName);
	
}
