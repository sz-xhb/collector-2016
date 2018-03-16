package com.xhb.core.dao;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.xhb.core.entity.Company;

@Repository
public class CompanyDAO extends ApplicationDAO {

	@Override
	protected Class<?> getPersistentClass() {
		return Company.class;
	}

	public Company findByCompanyName(String companyName) {
		Query query = sessionFactory.getCurrentSession().createQuery("from Company c where c.name=:companyName");
		query.setParameter("companyName", companyName);
		return (Company) query.uniqueResult();
	}

}
