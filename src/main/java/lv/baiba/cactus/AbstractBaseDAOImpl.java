package lv.baiba.cactus;

import lv.autentica.lapis.common.AppCacheUtil;
import lv.autentica.lapis.common.LapsaConstants;
import lv.autentica.lapis.common.Utilities;
import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.springframework.core.GenericTypeResolver;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.*;

@Transactional
public class AbstractBaseDAOImpl<T> extends HibernateDaoSupport implements
		AbstractBaseDAO<T> {

	/**
	 * Spring injects the SessionFactory, we pass it to HibernateDaoSupport
	 * 
	 * @param sessionFactory
	 */
	@Inject
	public void initSessionFactory(SessionFactory sessionFactory) {
		this.setSessionFactory(sessionFactory);
	}

	private Class<T> type;

	/**
	 * Convenience method. Hibernate returns untyped List objects. In our DAO
	 * implementations we can return typed Lists, but we need to cast + suppress
	 * the "unchecked cast" warning.
	 *
	 * Use this method, so that the warning suppression is localized to a single
	 * place.
	 * 
	 * @param l
	 *            Untyped list
	 * @return Typed list (List<T>) where T is the entity type of the current
	 *         DAO.
	 */
	protected List<T> castedList(List l) {
		@SuppressWarnings("unchecked")
		List<T> list = l;
		return list;
	}

	/**
	 * Returns the entity name associated with the DAO.
	 *
	 * This can be useful when concatenating HSQL queries, for example.
	 * 
	 * @return String Entity class name
	 */
	public String entityName() {
		return this.type.getName();
	}

	@SuppressWarnings("unchecked")
	public AbstractBaseDAOImpl() {
		this.type = (Class<T>) GenericTypeResolver.resolveTypeArgument(
				getClass(), AbstractBaseDAOImpl.class);
	}

	public void delete(T obj) {
		currentSession().delete(obj);
	}

	public void deleteAll() {
		getHibernateTemplate().deleteAll(this.findAll());
	}

	public T find(Integer id) {
		return (T) getHibernateTemplate().get(this.type, id);
	}

	public void save(T obj) {
		getHibernateTemplate().saveOrUpdate(obj);
	}

	public void update(T obj) {
		getHibernateTemplate().update(obj);
	}

	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		return (List<T>) getHibernateTemplate().find("FROM " + type.getName());
	}

	public Integer countAll() {
		return DataAccessUtils.intResult(getHibernateTemplate().find(
				"select count(*) from " + type.getName()));
	}
}