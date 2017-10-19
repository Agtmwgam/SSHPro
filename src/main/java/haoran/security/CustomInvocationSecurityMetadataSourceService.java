/**
 * <p>Copyright (c) 2012 深圳市鹏途信息技术有限公司 </p>
 * <p>				   All right reserved. 		     </p>
 * 
 * <p>项目名称 ： 	深圳道路检测管理系统        </p>
 * <p>创建者   :	iibm 
 * 
 * <p>描   述  :   MyInvocationSecurityMetadataSourceService.java for com.pengtu.security.ss    </p>
 * 
 * <p>最后修改 : $: 2012-10-24-下午3:34:48 v 1.0.0	 iibm   $     </p>
 * 
*/

package haoran.security;

/**
 * 
 * MyInvocationSecurityMetadataSourceService
 * 
 * 2012-10-24 下午3:34:48
 * 
 * @version 1.0.0
 * 
 */
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import haoran.utils.spring.SpringContextHolder;

/**
 * 最核心的地方，就是提供某个资源对应的权限定义，即getAttributes方法返回的结果。 此类在初始化时，应该取到所有资源及其对应角色的定义。
 * 
 */
// @Service
@Component
@Transactional
public class CustomInvocationSecurityMetadataSourceService implements FilterInvocationSecurityMetadataSource {

	private AntPathRequestMatcher urlMatcher;
	public static Map<String, Collection<ConfigAttribute>> resourceMap = null;

	public CustomInvocationSecurityMetadataSourceService() {
		loadResourceDefine();
	}

	public static void loadResourceDefine() {
		ApplicationContext ctx = SpringContextHolder.getApplicationContext();
		SessionFactory sessionFactory = (SessionFactory) ctx.getBean("sessionFactory");
		Session session = sessionFactory.openSession();
		// 在Web服务器启动时，提取系统中的所有权限。
		String hql = "select m.permission from Sysmenu m where m.delFlag = '0'";
		@SuppressWarnings("unchecked")
		List<String> query = session.createQuery(hql).list();
		/*
		 * 应当是资源为key， 权限为value。 资源通常为url， 权限就是那些以ROLE_为前缀的角色。 一个资源可以由多个权限来访问。
		 * sparta
		 */
		resourceMap = new HashMap<String, Collection<ConfigAttribute>>();
		for (String auth : query) {
			if (StringUtils.isEmpty(auth)) {
				continue;
			}
			ConfigAttribute ca = new SecurityConfig(auth);
			hql = "select m.href from Sysmenu m where m.delFlag='0' and m.permission=?";
			@SuppressWarnings("unchecked")
			List<String> query1 =session.createQuery(hql).setParameter(0, auth).list();
			/** query1是这个资源想匹配的所有地址，就是url集合 */
			for (String res : query1) {
				String url = StringUtils.isNotEmpty(res) ? res : "/NOURL";
				int loc = url.indexOf("?");//首先获取字符的位置
				if(loc > -1){  //截取？之前的字符串
					url = url.substring(0,loc);//
				}
				/*
				 * 判断资源文件和权限的对应关系，如果已经存在相关的资源url，则要通过该url为key提取出权限集合，
				 * 将权限增加到权限集合中。
				 */
				if (resourceMap.containsKey(url)) {
					Collection<ConfigAttribute> value = resourceMap.get(url);
					value.add(ca);
					resourceMap.put(url, value);
				} else {
					Collection<ConfigAttribute> atts = new ArrayList<ConfigAttribute>();
					atts.add(ca);
					resourceMap.put(url, atts);
				}

			}

		}
		
	}

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}

	// 根据URL，找到相关的权限配置。
	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		HttpServletRequest request = ((FilterInvocation) object).getRequest();
		Iterator<String> ite = resourceMap.keySet().iterator();
		while (ite.hasNext()) {
			String resURL = ite.next();
			urlMatcher = new AntPathRequestMatcher(resURL);
			if (urlMatcher.matches(request)) {
				return resourceMap.get(resURL);
			}
		}
		return null;
	}

	@Override
	public boolean supports(Class<?> arg0) {
		return true;
	}
}
