package com.xhb.core.service;

import com.xhb.core.entity.Customer;

public interface CustomerService extends ApplicationService{

	Customer findByCustomerName(String customerName);
	
}
