package haoran.dao.base;

import haoran.dao.base.QueryFilter.MatchType;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.util.Assert;

import haoran.utils.reflection.ConvertUtils;
import haoran.utils.web.ServletUtils;

/**
 * 属性过滤器
 * 
 */
public class QueryFilter {

	/** 多个属性间OR关系的分隔符. */
	public static final String OR_SEPARATOR = "_OR_";
	/** 多个值 用于多选查询的 分隔符. */
	public static final String VAL_SEPARATOR = ";";
	
	private List<Filter> filters = new ArrayList<Filter>();
	private Set<String> alias = new HashSet<String>(); // 防止重复添加alias用
	
	public enum MatchType {
	    EQ, LIKE, LIKESTART, LIKEND, LT, GT, LE, GE, BETWEEN, NE, OR, NULL, NOTNULL, EMPTY, NOTEMPTY;  
	}  
	/** 属性数据类型. */
	public enum PropertyType {
		S(String.class), I(Integer.class), L(Long.class), N(Double.class), D(Date.class), B(Boolean.class);

		private Class<?> clazz;

		private PropertyType(Class<?> clazz) {
			this.clazz = clazz;
		}

		public Class<?> getValue() {
			return clazz;
		}
	}
	
	public QueryFilter() {
		
	}
	
	public QueryFilter(String property, Object value) {
		this.addFilter(property, value,MatchType.EQ);
	}
	public QueryFilter(String property, Object value,MatchType m) {
		this.addFilter(property, value,m);
	}
	public boolean hasFilters() {
		return filters.size() > 0;
	}

	public void addFilter(final String [] propertys, final Object value, final MatchType matchType) {
		if (matchType == MatchType.OR || matchType == MatchType.BETWEEN) {
			throw new RuntimeException("匹配模式不正确");
		} else {
			filters.add(new Filter(propertys, value, matchType));
		}
	}

	public void addFilter(final String property, final Object value, final MatchType matchType) {
		if (matchType == MatchType.OR || matchType == MatchType.BETWEEN) {
			throw new RuntimeException("匹配模式不正确");
		} else {
			filters.add(new Filter(new String []{property}, value, matchType));
		}
	}

	public void addFilter(final String  property, final Object value) {
		filters.add(new Filter(new String []{property}, value));
	}
	
	public void addFilter(final String [] propertys, final Object value) {
		filters.add(new Filter(propertys, value));
	}

	public void addFilter(final String property, final Object lo, final Object hi) {
		filters.add(new Filter(new String []{property}, lo, hi));
	}
	
	public void addFilter(final String [] propertys, final Object lo, final Object hi) {
		filters.add(new Filter(propertys, lo, hi));
	}

	public void addFilter(final Set<OrClausePair> set) {
		filters.add(new Filter(set));
	}
	/**
	 * 
	 * addFilterVN:
	 * 适用:为空或者为o
	 * @param property
	 * @param o 
	 * @exception 
	 * @since  1.0.0
	 */
	public void addFilterVN(final String property,final Object o){
		Set<OrClausePair> set = new HashSet<OrClausePair>();
		OrClausePair orClausePair1 = new OrClausePair(property,null,MatchType.NULL);
		OrClausePair orClausePair2 = new OrClausePair(property,o,MatchType.EQ);
		set.add(orClausePair1);
		set.add(orClausePair2);
		filters.add(new Filter(set));
	}
	/**
	 * 
	 * addFilterVN2:
	 * 适用:为空或者为o
	 * @param property
	 * @param o 
	 * @exception 
	 * @since  1.0.0
	 */
	public void addFilterVN2(final String property, String[] arrs){
		Set<OrClausePair> set = new HashSet<OrClausePair>();
		for(String mcString : arrs){
		OrClausePair orClausePair2 = new OrClausePair(property,mcString,MatchType.EQ);
		set.add(orClausePair2);
		}
		filters.add(new Filter(set));
	}
	/**
	 * 
	 * addFilterList:
	 * 适用:
	 * @param property
	 * @param ms 
	 * @exception 
	 * @since  1.0.0
	 */
	public void addFilterList(String property, List<String> ms) {
		if(ms.size()==0){
			return;
		}
		Set<OrClausePair> set = new HashSet<OrClausePair>();
		for (String val : ms) {
			OrClausePair orClausePair = new OrClausePair(property,val,MatchType.EQ);
			set.add(orClausePair);
		}
		filters.add(new Filter(set));
		
	}
	/**
	 * 
	 * addFilterList2:
	 * 适用: 或者
	 * @param property
	 * @param ms
	 * @param p2
	 * @param o2 
	 * @exception 
	 * @since  1.0.0
	 */
	public void addFilterList2(String property, List<String> ms,String p2,Object o2) {
		Set<OrClausePair> set = new HashSet<OrClausePair>();
		for (String val : ms) {
			OrClausePair orClausePair = new OrClausePair(property,val,MatchType.EQ);
			set.add(orClausePair);
		}
		OrClausePair orClausePair2 = new OrClausePair(p2,o2,MatchType.LIKE);
		set.add(orClausePair2);
		filters.add(new Filter(set));
	}
//	/**
//	 * 场站数据过滤
//	 * @param user
//	 */
//	public void dataScopeFilter(User user){
//		List<Role> roles = user.getRoleList();
//		Set<OrClausePair> set = new HashSet<OrClausePair>();
//		List<String> dataScope = Lists.newArrayList();
//		boolean isDataScopeAll = false;
//		for(Role r:roles){
//			if (!dataScope.contains(r.getDataScope())){
//				if(Role.DATA_SCOPE_ALL.equals(r.getDataScope())){
//					isDataScopeAll = true;
//				}else if(Role.DATA_SCOPE_ORG_AND_CHILD.equals(r.getDataScope())){
//					set.add(new OrClausePair("manageOrg.id",user.getEmploye().getOrgId(),MatchType.EQ));
//				}
//				dataScope.add(r.getDataScope());
//			}
//		}
//		if (!isDataScopeAll){
//			filters.add(new Filter(set));
//		}
//		
//	}
	/**
	 *  获得根据当前经纬度 的  约等于500米的数据
	 * encode:
	 * 适用:
	 * @param latitude   纬度
	 * @param longitude  经度
	 * @return 
	 * @exception 
	 * @since  1.0.0
	 */
	public void  addFilterForDistance(final String property,Double lat,Double lon) {
//		 GeoHash[]  GeoHashs =geoHash.getAdjacent();
//			Set<OrClausePair> set = new HashSet<OrClausePair>();
//		 for (GeoHash geoHash8 : GeoHashs) {
//				OrClausePair orClausePair = new OrClausePair(property,geoHash8.toBase32(),MatchType.LIKEND);
//				set.add(orClausePair);
//		}
//		 this.addFilter(set);
	};

	/**
	 * 从HttpRequest中创建PropertyFilter列表, 默认Filter属性名前缀为filter.
	 * 
	 * @see #buildFromHttpRequest(HttpServletRequest, String)
	 */
	public static QueryFilter  buildFromHttpRequest(final HttpServletRequest request) {
		return buildFromHttpRequestMultilist(request, "filter");
	}


	/**
	 * 从HttpRequest中创建Filter列表
	 * Filter命名规则为Filter属性前缀_比较类型属性类型_属性名.
	 * 
	 * eg.
	 * f_EQS_name  value =zhou
	 * f_EQS_user.name value =ming
	 * f_BETWEENS_year  value[0] =2010 value[1]=2011
	 * f_LIKES_name_OR_email
	 */
	public static QueryFilter buildFromHttpRequestMultilist(final HttpServletRequest request, final String filterPrefix) {
		Class<?> propertyClass = null;
		MatchType matchType = null;
		QueryFilter simpleFilter = new QueryFilter();
		//从request中获取含属性前缀名的参数,构造去除前缀名后的参数Map.
		Map<String, Object> filterParamMap = ServletUtils.getParametersStartingWith(request, filterPrefix + "_");
		//分析参数Map,构造PropertyFilter列表
		for (Map.Entry<String, Object> entry : filterParamMap.entrySet()) {
			String filterName = entry.getKey();
			String firstPart = StringUtils.substringBefore(filterName, "_");
			//比较类型  eq le ge between 等
			String matchTypeCode = StringUtils.substring(firstPart, 0, firstPart.length() - 1);
			// 数据 value 类型
			String propertyTypeCode = StringUtils.substring(firstPart, firstPart.length() - 1, firstPart.length());
			try {
				matchType = Enum.valueOf(MatchType.class, matchTypeCode);
			} catch (RuntimeException e) {
				throw new IllegalArgumentException("filter名称" + filterName + "没有按规则编写,无法得到属性比较类型.", e);
			}
			try {
				propertyClass = Enum.valueOf(PropertyType.class, propertyTypeCode).getValue();
			} catch (RuntimeException e) {
				throw new IllegalArgumentException("filter名称" + filterName + "没有按规则编写,无法得到属性值类型.", e);
			}
			Object value = entry.getValue();
			String propertyNameStr = StringUtils.substringAfter(filterName, "_");
			Assert.isTrue(StringUtils.isNotBlank(propertyNameStr), "filter名称" + filterName + "没有按规则编写,无法得到属性名称.");
			String[] propertyNames = StringUtils.splitByWholeSeparator(propertyNameStr, OR_SEPARATOR);
			// 如果是类似于 f_EQS_name =342_342   between 
			if(matchType == MatchType.BETWEEN &&  value instanceof String[]){
				 String [] betweenV = (String[]) value;
				 if(betweenV.length ==2) {
					Object lo = ConvertUtils.convertStringToObject(betweenV[0], propertyClass); 
					Object hi = ConvertUtils.convertStringToObject(betweenV[1], propertyClass); 
					simpleFilter.addFilter(propertyNames, lo,hi);
				 }
				// 如果不是 between 比较  
			} else if(matchType == MatchType.OR ){
				String  stringV = (String) value;
				// 为空 那么跳出
				if (StringUtils.isBlank(stringV)) {
					continue;
				} else {
					String [] vals = StringUtils.splitByWholeSeparator(stringV, VAL_SEPARATOR);
					Set<OrClausePair> set = new HashSet<OrClausePair>();
					for (String val : vals) {
						OrClausePair orClausePair = new OrClausePair(propertyNames[0],val,MatchType.EQ);
						set.add(orClausePair);
					}
					simpleFilter.addFilter(set);
				}
				
			} else {
				String  stringV = (String) value;
				if (StringUtils.isBlank(stringV)) {
					continue;
				}
				Object matchValue = ConvertUtils.convertStringToObject(stringV, propertyClass); 
				simpleFilter.addFilter(propertyNames, matchValue,matchType);
			} 
		}
		return simpleFilter;
	}

	/**
	 * 清理过滤器
	 * 在具有级联条件的查询中，alias不能重复添加，只允许添加一次
	 * 在执行完一次count(*)查询后，之前添加的criteria全部失效
	 * 重新添加criteria的时候，需要清空alias的Set集合
	 * 以便正确执行createAlias语句
	 * 
	 */
	public void clearFilter() {
		alias.clear();
	}

	/**
	 * 构建当前filter的Criteria对象
	 */
	public Criteria buildCriteria(final Criteria criteria) {
		for (Filter filter : filters) {
				if (filter.getMatchType() == MatchType.BETWEEN) {
					buildCriteria(criteria, filter.getProperty(), filter.getLo(), filter.getHi());
				} else if (filter.getMatchType() == MatchType.OR) {
					builCriteria(criteria, filter.getSet());
				} else {
					if (!filter.hasMultiProperties()) { //只有一个属性需要比较的情况.
						buildCriteria(criteria, filter.getProperty(), filter.getValue(), filter.getMatchType());
					}
					 else {
						 //包含多个属性需要比较的情况,进行or处理.
						 Disjunction disjunction = Restrictions.disjunction();
							for (String param : filter.getPropertys()) {
								Criterion criterion = buildCriterion(criteria,param,filter.getValue() , filter.getMatchType());
								disjunction.add(criterion);
							}
							criteria.add(disjunction);
				}
			}
				
			
		}
		return criteria;
	}

	/**
	 * 根据多个属性名、值和匹配类型构建Hibernate的Criteria的OR对象
	 */
	@SuppressWarnings("incomplete-switch")
	private Criteria builCriteria(final Criteria criteria, final Set<OrClausePair> set) {
		Disjunction disjunction = Restrictions.disjunction();
		for (OrClausePair pair : set) {
			String alias = createAlias(criteria, pair.getProperty());
			if (StringUtils.isNotBlank(alias)) {
				MatchType matchType = pair.getMatchType();
				if (matchType == MatchType.BETWEEN) {
					if (pair.getLo() != null && pair.getHi() != null) {
						disjunction.add(Restrictions.between(alias, pair.getLo(), pair.getHi()));
					}
				} else {
					switch (matchType) {
					case NULL:
						disjunction.add(Restrictions.isNull(alias));
						break;
					case NOTNULL:
						disjunction.add(Restrictions.isNotNull(alias));
						break;
					case EMPTY:
						disjunction.add(Restrictions.isEmpty(alias));
						break;
					case NOTEMPTY:
						disjunction.add(Restrictions.isNotEmpty(alias));
						break;
					}
					Object value = pair.getValue();
					if (value != null) {
						switch (matchType) {
						case EQ:
							disjunction.add(Restrictions.eq(alias, value));
							break;
						case LIKE:
							disjunction.add(Restrictions.like(alias, (String) value, MatchMode.ANYWHERE));
							break;
						case LIKESTART:
							disjunction.add(Restrictions.like(alias, (String) value, MatchMode.START));
							break;
						case LIKEND:
							disjunction.add(Restrictions.like(alias, (String) value, MatchMode.END));
							break;
						case LE:
							disjunction.add(Restrictions.le(alias, value));
							break;
						case LT:
							disjunction.add(Restrictions.lt(alias, value));
							break;
						case GE:
							disjunction.add(Restrictions.ge(alias, value));
							break;
						case GT:
							disjunction.add(Restrictions.gt(alias, value));
							break;
						case NE:
							disjunction.add(Restrictions.ne(alias, value));
							break;
						}
					}
				}
			}
		}
		criteria.add(disjunction);
		return criteria;
	}

	/**
	 * 根据属性名、值和匹配类型构建Hibernate的Criteria对象
	 */
	@SuppressWarnings("incomplete-switch")
	private Criteria buildCriteria(final Criteria criteria, final String property, final Object value,
			final MatchType matchType) {
		String alias = createAlias(criteria, property);
		if (StringUtils.isNotBlank(alias)) {
			switch (matchType) {
			case NULL:
				criteria.add(Restrictions.isNull(alias));
				return criteria;
			case NOTNULL:
				criteria.add(Restrictions.isNotNull(alias));
				return criteria;
			case EMPTY:
				criteria.add(Restrictions.isEmpty(alias));
				return criteria;
			case NOTEMPTY:
				criteria.add(Restrictions.isNotEmpty(alias));
				return criteria;
			}
			if (value != null) {
				switch (matchType) {
				case EQ:
					criteria.add(Restrictions.eq(alias, value));
					break;
				case LIKE:
					criteria.add(Restrictions.like(alias, (String) value, MatchMode.ANYWHERE));
					break;
				case LIKESTART:
					criteria.add(Restrictions.like(alias, (String) value, MatchMode.START));
					break;
				case LIKEND:
					criteria.add(Restrictions.like(alias, (String) value, MatchMode.END));
					break;
				case LE:
					criteria.add(Restrictions.le(alias, value));
					break;
				case LT:
					criteria.add(Restrictions.lt(alias, value));
					break;
				case GE:
					criteria.add(Restrictions.ge(alias, value));
					break;
				case GT:
					criteria.add(Restrictions.gt(alias, value));
					break;
				case NE:
					criteria.add(Restrictions.ne(alias, value));
					break;
				}
			}
		}
		return criteria;
	}

	
	
	/**
	 * 按属性条件参数创建Criterion,辅助函数.
	 */
	@SuppressWarnings("incomplete-switch")
	protected Criterion buildCriterion(final Criteria criteria,final String propertyName, final Object value, final MatchType matchType) {
		Assert.hasText(propertyName, "propertyName不能为空");
		Criterion criterion = null;
		String alias = createAlias(criteria,propertyName);
		if (StringUtils.isNotBlank(alias)) {
				switch (matchType) {
				case NULL:
					criterion =Restrictions.isNull(alias);
					break;
				case NOTNULL:
					criterion = Restrictions.isNotNull(alias);
					break;
				case EMPTY:
					criterion = Restrictions.isEmpty(alias);
					break;
				case NOTEMPTY:
					criterion = Restrictions.isNotEmpty(alias);
					break;
				}
				if (value != null) {
					switch (matchType) {
					case EQ:
						criterion = Restrictions.eq(alias, value);
						break;
					case LIKE:
						criterion = Restrictions.like(alias, (String) value, MatchMode.ANYWHERE);
						break;
					case LIKESTART:
						criterion =  Restrictions.like(alias, (String) value, MatchMode.START);
						break;
					case LIKEND:
						criterion =   Restrictions.like(alias, (String) value, MatchMode.END);
						break;
					case LE:
						criterion =  Restrictions.le(alias, value);
						break;
					case LT:
						criterion =  Restrictions.lt(alias, value);
						break;
					case GE:
						criterion =  Restrictions.ge(alias, value);
						break;
					case GT:
						criterion =  Restrictions.gt(alias, value);
						break;
					case NE:
						criterion =  Restrictions.ne(alias, value);
						break;
					}
				}
			}
		return criterion;
	}

	/**
	 * 创建BETWEEN匹配类型的Criteria对象
	 */
	private Criteria buildCriteria(final Criteria criteria, final String property, final Object lo, final Object hi) {
		String alias = createAlias(criteria, property);
		if (StringUtils.isNotBlank(alias) && lo != null && hi != null) {
			criteria.add(Restrictions.between(alias, lo, hi));
		}
		return criteria;
	}

	/**
	 * 为Criteria对象添加别名
	 * 最多支持2级级联，例如animal.pet.cat
	 * @param criteria Criteria对象
	 * @param property 属性
	 * @return 别名
	 */
	private String createAlias(final Criteria criteria, final String property) {
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
	 * 过滤器
	 * 两个参数的构造方法，默认查询条件为LIKE
	 */
	private static class Filter {
		private String [] propertys;
		private Object value;
		private Object lo;
		private Object hi;
		private Set<OrClausePair> set;
		private MatchType matchType;

		/**
		 * 构造一个OR条件的过滤器，使用disjunction进行连接
		 * @param set OR条件参数集合
		 */
		public Filter(Set<OrClausePair> set) {
			this.set = set;
			this.matchType = MatchType.OR;
		}

		/**
		 * 构造LIKE类型查询
		 * @param property 属性
		 * @param value 值
		 */
		public Filter(String[] propertys, Object value) {
			this.propertys = propertys;
			this.value = value;
			this.matchType = MatchType.LIKE;
		}

		/**
		 * 构造MatchType类型的查询
		 * @param property 属性
		 * @param value 值
		 * @param matchType 匹配类型
		 */
		public Filter(String[] propertys, Object value, MatchType matchType) {
			this.propertys = propertys;
			this.value = value;
			this.matchType = matchType;
		}

		/**
		 * 构造Between类型的查询
		 * @param property 属性
		 * @param lo lowValue
		 * @param hi hiValue
		 */
		public Filter(String[] propertys, Object lo, Object hi) {
			this.propertys = propertys;
			this.lo = lo;
			this.hi = hi;
			this.matchType = MatchType.BETWEEN;
		}

		public String []getPropertys() {
			return propertys;
		}
		/**
		 * 获取唯一的比较属性名称.
		 */
		public String getProperty() {
			Assert.isTrue(propertys.length == 1, "There are not only one property in this filter.");
			return propertys[0];
		}
		/**
		 * 是否比较多个属性.
		 */
		public boolean hasMultiProperties() {
			return (propertys.length > 1);
		}

		public Object getValue() {
			return value;
		}

		public Object getLo() {
			return lo;
		}

		public Object getHi() {
			return hi;
		}

		public Set<OrClausePair> getSet() {
			return set;
		}

		public MatchType getMatchType() {
			return matchType;
		}
	}
	
}


/**
 * 用于为SimpleFilter添加OR过滤条件
 *
 */
 class OrClausePair {

	private String property;
	private Object value;
	private Object lo;
	private Object hi;
	private MatchType matchType;
	private Criterion criterion;

	public OrClausePair(String property, Object value, MatchType matchType) {
		if (matchType == MatchType.BETWEEN) {
			throw new RuntimeException("匹配模式不正确");
		} else {
			this.property = property;
			this.value = value;
			this.matchType = matchType;
		}
	}

	public OrClausePair(String property, Object lo, Object hi) {
		this.property = property;
		this.lo = lo;
		this.hi = hi;
		this.matchType = MatchType.BETWEEN;
	}

	public OrClausePair(Criterion criterion) {
		this.criterion = criterion;
	}

	public String getProperty() {
		return property;
	}

	public Object getValue() {
		return value;
	}

	public Object getLo() {
		return lo;
	}

	public Object getHi() {
		return hi;
	}

	public MatchType getMatchType() {
		return matchType;
	}

	public Criterion getCriterion() {
		return criterion;
	}

}