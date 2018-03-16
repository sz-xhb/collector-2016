package com.xhb.core.dao;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;

public abstract class ApplicationDAO {

	@Resource
	protected SessionFactory sessionFactory;

	protected abstract Class<?> getPersistentClass();

	@SuppressWarnings("unchecked")
	public <T> T save(Object entity) {
		Session session = sessionFactory.getCurrentSession();
		return (T) session.save(entity);
	}

	public void update(Object entity) {
		Session session = sessionFactory.getCurrentSession();
		session.update(entity);
	}

	public void delete(Serializable id) {
		Object object = get(id);
		if (object == null) {
			return;
		}
		Session session = sessionFactory.getCurrentSession();
		session.delete(object);
	}

	@SuppressWarnings("unchecked")
	public <T> T get(Serializable id) {
		if (id == null) {
			return null;
		}
		Session session = sessionFactory.getCurrentSession();
		return (T) session.get(getPersistentClass(), id);
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> findByCriteria(DetachedCriteria detachedCriteria) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = detachedCriteria.getExecutableCriteria(session);
		return criteria.list();
	}

}
