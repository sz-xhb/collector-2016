package com.xhb.core.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xhb.core.dao.ApplicationDAO;
import com.xhb.core.dao.CustomerDAO;
import com.xhb.core.entity.Customer;
import com.xhb.core.service.CustomerService;

@Service
@Transactional
public class CustomerServiceImpl extends ApplicationServiceImpl implements CustomerService {

	@Resource
	private CustomerDAO customerDAO;
	@Override
	protected ApplicationDAO getApplicationDAO() {
		return customerDAO;
	}
	@Override
	public Customer findByCustomerName(String customerName) {
		return customerDAO.findByCustomerName(customerName);
	}

}
