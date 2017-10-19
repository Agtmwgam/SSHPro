/**
 * Copyright (c) 2005-2010 springside.org.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * 
 * $Id: HibernateDao.java,v 1.1 2016/03/15 05:48:53 zwm Exp $
 */
package haoran.dao.Hibernate;

import haoran.dao.base.Page;
import haoran.dao.base.QueryFilter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.internal.CriteriaImpl.OrderEntry;
import org.hibernate.internal.CriteriaImpl.Subcriteria;
//import org.hibernate.impl.CriteriaImpl;
//import org.hibernate.impl.CriteriaImpl.OrderEntry;
//import org.hibernate.impl.CriteriaImpl.Subcriteria;
import org.hibernate.transform.ResultTransformer;
import org.springframework.util.Assert;

import haoran.config.Constants;
import haoran.entity.base.AuditableEntity;
import haoran.entity.base.IdEntity;
import haoran.utils.reflection.ReflectionUtils;

/**
 * 封装SpringSide扩展功能的Hibernat DAO泛型基类.
 * 
 * 扩展功能包括分页查询,按属性过滤条件列表查询.
 * 可在Service层直接使用,也可以扩展泛型DAO子类使用,见两个构造函数的注释.
 * 
 * @param <T> DAO操作的对象类型
 * @param <PK> 主键类型
 * 
 * @author calvin
 */
@SuppressWarnings("unchecked")
public class HibernateDao<T, PK extends Serializable> extends SimpleHibernateDao<T, PK> {
	
	/**
	 * 用于Dao层子类的构造函数.
	 * 通过子类的泛型定义取得对象类型Class.
	 * eg.
	 * public class UserDao extends HibernateDao<User, Long>{
	 * }
	 */
	public HibernateDao() {
		super();
	}

	/**
	 * 用于省略Dao层, Service层直接使用通用HibernateDao的构造函数.
	 * 在构造函数中定义对象类型Class.
	 * eg.
	 * HibernateDao<User, Long> userDao = new HibernateDao<User, Long>(sessionFactory, User.class);
	 */
	public HibernateDao(final SessionFactory sessionFactory, final Class<T> entityClass) {
		super(sessionFactory, entityClass);
	}

	//-- 分页查询函数 --//

	/**
	 * 分页获取全部对象.
	 */
	public Page<T> getAll(final Page<T> page) {
		return findPage(page);
	}
	
	/**
	 * 按属性查找对象列表, 匹配方式为相等. 排序按照基本的排序方式
	 */
	
	public List<T> findBy(final String propertyName, final Object value,String orderBy,String order) {
		Assert.hasText(propertyName, "propertyName不能为空");
		Criterion criterion = Restrictions.eq(propertyName, value);
		Criteria criteria = createCriteria(criterion);
		setPageParameterToCriteria(criteria,orderBy,order);
		return criteria.list();
	}
	

	/**
	 * 按HQL分页查询.
	 * 
	 * @param page 分页参数. 注意不支持其中的orderBy参数.
	 * @param hql hql语句 hql 语句 查询的主表 需要增加 delFlag ='0'.
	 * @param values 数量可变的查询参数,按顺序绑定.
	 * 
	 * @return 分页查询结果, 附带结果列表及所有查询输入参数.
	 */
	public Page<T> findPage(final Page<T> page, final String hql, final Object... values) {
		Assert.notNull(page, "page不能为空");

		Query q = createQuery(hql, values);

		if (page.isAutoCount()) {
			long totalCount = countHqlResult(hql, values);
			page.setTotalCount(totalCount);
		}
		if (page.isPageByDatabase()) {
			setPageParameterToQuery(q, page);
		}
		List<T> result = q.list();
		page.setResult(result);
		return page;
	}

	/**
	 * 按HQL分页查询.
	 * 
	 * @param page 分页参数. 注意不支持其中的orderBy参数.
	 * @param hql hql语句 hql 语句 查询的主表 需要增加 delFlag ='0'.
	 * @param values 命名参数,按名称绑定.
	 * 
	 * @return 分页查询结果, 附带结果列表及所有查询输入参数.
	 */
	public Page<T> findPage(final Page<T> page, final String hql, final Map<String, ?> values) {
		Assert.notNull(page, "page不能为空");

		Query q = createQuery(hql, values);

		if (page.isAutoCount()) {
			long totalCount = countHqlResult(hql, values);
			page.setTotalCount(totalCount);
		}
		if (page.isPageByDatabase()) {
			setPageParameterToQuery(q, page);
		}
		List<T> result = q.list();
		page.setResult(result);
		return page;
	}
	/**
	 * 按Criteria分页查询.
	 * 
	 * @param page 分页参数.
	 * @param criterions 数量可变的Criterion.
	 * 
	 * @return 分页查询结果.附带结果列表及所有查询输入参数.
	 */
	public Page<T> findPage(final Page<T> page, final Criterion... criterions) {
		Assert.notNull(page, "page不能为空");

		Criteria c = createCriteria(criterions);

		if (page.isAutoCount()) {
			long totalCount = countCriteriaResult(c);
			page.setTotalCount(totalCount);
		}
		if (page.isPageByDatabase()) {
			setPageParameterToCriteria(c, page);
		}
		List<T> result = c.list();
		page.setResult(result);
		return page;
	}
	/**
	 * 设置分页参数到Query对象,辅助函数.
	 */
	protected Query setPageParameterToQuery(final Query q, final Page<T> page) {

		Assert.isTrue(page.getPageSize() > 0, "Page Size must larger than zero");

		//hibernate的firstResult的序号从0开始
		q.setFirstResult(page.getStartRow());
		q.setMaxResults(page.getPageSize());
		return q;
	}

	/**
	 * 设置分页参数到Criteria对象,辅助函数.
	 */
	protected Criteria setPageParameterToCriteria(final Criteria c, final Page<T> page) {

		Assert.isTrue(page.getPageSize() > 0, "Page Size must larger than zero");

		//hibernate的firstResult的序号从0开始
		c.setFirstResult(page.getStartRow());
		c.setMaxResults(page.getPageSize());
		if (page.isOrderBySetted()) {
			String[] orderByArray = StringUtils.split(page.getOrderBy(), ',');
			String[] orderArray = StringUtils.split(page.getOrder(), ',');
			Assert.isTrue(orderByArray.length == orderArray.length, "分页多重排序参数中,排序字段与排序方向的个数不相等");
			// 防止重复添加alias用
			Set<String> alias = new HashSet<String>();
			CriteriaImpl impl = (CriteriaImpl) c;
			
			alias.add(impl.getAlias());
			Iterator<Subcriteria> it = impl.iterateSubcriteria();
			while (it.hasNext()) {
				    Subcriteria subcriteria = (Subcriteria) it.next();
					alias.add(subcriteria.getAlias());
				}
			for (int i = 0; i < orderByArray.length; i++) {
				String property = orderByArray[i];
				String alia= createAlias(c,alias,property);
				if (Page.ASC.equals(orderArray[i])) {
					c.addOrder(Order.asc(alia));
				} else {
					c.addOrder(Order.desc(alia));
				}
			}
		}
		return c;
	}
	
	/**
	 * 设置分页参数到Criteria对象,辅助函数.
	 * orderBy 形如 Parameter1,Parameter2,Parameter3
	 * order 形如 desc,desc,asc
	 * orderby 与order 要一一对应
	 */
	protected Criteria setPageParameterToCriteria(final Criteria c,String orderBy,String order) {
		if (StringUtils.isNotBlank(orderBy) && StringUtils.isNotBlank(order)) {
			String[] orderByArray = StringUtils.split(orderBy, ',');
			String[] orderArray = StringUtils.split(order, ',');
			Assert.isTrue(orderByArray.length == orderArray.length, "分页多重排序参数中,排序字段与排序方向的个数不相等");
			// 防止重复添加alias用
			Set<String> alias = new HashSet<String>();
			CriteriaImpl impl = (CriteriaImpl) c;
			alias.add(impl.getAlias());
			Iterator<Subcriteria> it = impl.iterateSubcriteria();
			while (it.hasNext()) {
				    Subcriteria subcriteria = (Subcriteria) it.next();
					alias.add(subcriteria.getAlias());
				}
			for (int i = 0; i < orderByArray.length; i++) {
				String property = orderByArray[i];
				String alia= createAlias(c,alias,property);
				if (Page.ASC.equals(orderArray[i])) {
					c.addOrder(Order.asc(alia));
				} else {
					c.addOrder(Order.desc(alia));
				}
			}
		}
		return c;
	}

	/**
	 * 为Criteria对象添加别名
	 * 最多支持2级级联，例如animal.pet.cat
	 * @param criteria Criteria对象
	 * @param property 属性
	 * @return 别名
	 */
	private String createAlias(final Criteria criteria, Set<String> alias, final String property) {
		String[] names = StringUtils.split(property, ".");
		if (names != null && names.length == 2) {
			if (alias.add(names[0])) {
				criteria.createAlias(names[0], names[0]);
			}
			return names[0] + "." + names[1];
		} else if (names != null && names.length == 3) {
			if (alias.add(names[0])) {
				criteria.createAlias(names[0], names[0]);
			}
			if (alias.add(names[0] + "." + names[1])) {
				criteria.createAlias(names[0] + "." + names[1], names[1]);
			}
			return names[1] + "." + names[2];
		} else {
			return property;
		}
	}

	/**
	 * 执行count查询获得本次Hql查询所能获得的对象总数.
	 * 
	 * 本函数只能自动处理简单的hql语句,复杂的hql查询请另行编写count语句查询.
	 */
	protected long countHqlResult(final String hql, final Object... values) {
		String countHql = prepareCountHql(hql);

		try {
			Long count = findUnique(countHql, values);
			return count;
		} catch (Exception e) {
			throw new RuntimeException("hql can't be auto count, hql is:" + countHql, e);
		}
	}

	/**
	 * 执行count查询获得本次Hql查询所能获得的对象总数.
	 * 
	 * 本函数只能自动处理简单的hql语句,复杂的hql查询请另行编写count语句查询.
	 */
	protected long countHqlResult(final String hql, final Map<String, ?> values) {
		String countHql = prepareCountHql(hql);

		try {
			Long count = findUnique(countHql, values);
			return count;
		} catch (Exception e) {
			throw new RuntimeException("hql can't be auto count, hql is:" + countHql, e);
		}
	}

	private String prepareCountHql(String orgHql) {
		String fromHql = orgHql;
		//select子句与order by子句会影响count查询,进行简单的排除.
		fromHql = "from " + StringUtils.substringAfter(fromHql, "from");
		fromHql = StringUtils.substringBefore(fromHql, "order by");

		String countHql = "select count(*) " + fromHql;
		return countHql;
	}

	/**
	 * 执行count查询获得本次Criteria查询所能获得的对象总数.
	 */
	protected long countCriteriaResult(final Criteria c) {
		CriteriaImpl impl = (CriteriaImpl) c;

		// 先把Projection、ResultTransformer、OrderBy取出来,清空三者后再执行Count操作
		Projection projection = impl.getProjection();
		ResultTransformer transformer = impl.getResultTransformer();

		List<CriteriaImpl.OrderEntry> orderEntries = null;
		try {
			orderEntries = (List<OrderEntry>) ReflectionUtils.getFieldValue(impl, "orderEntries");
			ReflectionUtils.setFieldValue(impl, "orderEntries", new ArrayList<T>());
		} catch (Exception e) {
			logger.error("不可能抛出的异常:{}", e.getMessage());
		}

		// 执行Count查询
		Long totalCountObject = (Long) c.setProjection(Projections.rowCount()).uniqueResult();
		long totalCount = (totalCountObject != null) ? totalCountObject : 0;

		// 将之前的Projection,ResultTransformer和OrderBy条件重新设回去
		c.setProjection(projection);

		if (projection == null) {
			c.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		}
		if (transformer != null) {
			c.setResultTransformer(transformer);
		}
		try {
			ReflectionUtils.setFieldValue(impl, "orderEntries", orderEntries);
		} catch (Exception e) {
			logger.error("不可能抛出的异常:{}", e.getMessage());
		}

		return totalCount;
	}

	
	/**
	 * 按属性过滤条件列表查找对象列表.
	 */
	public List<T> find(QueryFilter fileter) {
		Criteria criteria = getSession().createCriteria(entityClass);
		criteria= fileter.buildCriteria(criteria);
	//  非物理删除  查询所有状态正常的类
			if(IdEntity.class.isAssignableFrom(entityClass)) {
				Criterion c = Restrictions.eq("delFlag", Constants.DELFLAG_IN);
				criteria.add(c);
			}
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		
		return criteria.list();
	}

	public List<T> find(QueryFilter fileter,String orderByProperty,boolean isAsc) {
		Criteria criteria = getSession().createCriteria(entityClass);
		criteria= fileter.buildCriteria(criteria);
		if (isAsc) {
			criteria.addOrder(Order.asc(orderByProperty));
		} else {
			criteria.addOrder(Order.desc(orderByProperty));
		}
	//  非物理删除  查询所有状态正常的类
			if(IdEntity.class.isAssignableFrom(entityClass)) {
				Criterion c = Restrictions.eq("delFlag", Constants.DELFLAG_IN);
				criteria.add(c);
			}
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		
		return criteria.list();
	}

	/**
	 * 按属性过滤条件列表查找对象列表.
	 */
	public Page<T> findPage(final Page<T> page, final QueryFilter fileter) {
		Criteria criteria = getSession().createCriteria(entityClass);
		
		criteria= fileter.buildCriteria(criteria);
		//  非物理删除  查询所有状态正常的类
		if(IdEntity.class.isAssignableFrom(entityClass)) {
			Criterion c = Restrictions.eq("delFlag", Constants.DELFLAG_IN);
			criteria.add(c);
		}
		//  查询分类
		if(AuditableEntity.class.isAssignableFrom(entityClass)) {
			if (!page.isOrderBySetted()) {
				page.setOrderBy("lastModifyTime,createDate");
				page.setOrder("desc,desc");
			}
		}
		if (page.isAutoCount()) {
			long totalCount = countCriteriaResult(criteria);
			page.setTotalCount(totalCount);
		}
		if (page.isPageByDatabase()) {
			setPageParameterToCriteria(criteria, page);
		}
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		List<T> result = criteria.list();
		page.setResult(result);
		return page;
	}
	/**
	 * 
	 * findTotalCount:
	 * 适用:根据过滤条件得到总和
	 * @param fileter
	 * @return 
	 * @exception 
	 * @since  1.0.0
	 */
	public long findTotalCount(final QueryFilter fileter){
		Criteria criteria = getSession().createCriteria(entityClass);
		criteria= fileter.buildCriteria(criteria);
		//  非物理删除  查询所有状态正常的类
		if(IdEntity.class.isAssignableFrom(entityClass)) {
			Criterion c = Restrictions.eq("delFlag", Constants.DELFLAG_IN);
			criteria.add(c);
		}
		long totalCount = countCriteriaResult(criteria);
		return totalCount;
	}
	public long findTotalCount(final String hql, final Object... values){
		long totalCount = countHqlResult(hql, values);
		return totalCount;
	}
}
