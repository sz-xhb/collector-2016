package com.xhb.core.dao;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import com.xhb.core.entity.Customer;

@Repository
public class CustomerDAO extends ApplicationDAO {

	@Override
	protected Class<?> getPersistentClass() {
		return Customer.class;
	}

	public Customer findByCustomerName(String customerName) {
		Query query = sessionFactory.getCurrentSession().createQuery("from Customer c where c.name=:customerName");
		query.setParameter("customerName", customerName);
		return  (Customer) query.uniqueResult();
	}

}
