package com.xhb.core.service.impl;

import java.io.Serializable;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.transaction.annotation.Transactional;

import com.xhb.core.dao.ApplicationDAO;
import com.xhb.core.service.ApplicationService;

@Transactional
public abstract class ApplicationServiceImpl implements ApplicationService {

	protected abstract ApplicationDAO getApplicationDAO();

	@Override
	public <T> T save(Object entity) {
		return getApplicationDAO().save(entity);
	}

	@Override
	public void update(Object entity) {
		getApplicationDAO().update(entity);
	}

	@Override
	public void delete(Serializable id) {
		getApplicationDAO().delete(id);
	}

	@Override
	public <T> T get(Serializable id) {
		return getApplicationDAO().get(id);
	}

	@Override
	public <T> List<T> findByCriteria(DetachedCriteria detachedCriteria) {
		return getApplicationDAO().findByCriteria(detachedCriteria);
	}
}
