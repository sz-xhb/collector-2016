package com.xhb.core.service;

import java.io.Serializable;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

public interface ApplicationService {

	<T> T save(Object entity);

	void update(Object entity);

	void delete(Serializable id);

	<T> T get(Serializable id);

	<T> List<T> findByCriteria(DetachedCriteria detachedCriteria);

}
