/**
 * Copyright (c) 2005-2010 springside.org.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * 
 * $Id: Struts2Utils.java,v 1.1 2016/03/15 05:52:49 zwm Exp $
 */
package haoran.utils.web;

import java.io.IOException;


import java.net.URLDecoder;
import java.text.SimpleDateFormat;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

import com.fasterxml.jackson.module.hibernate.HibernateModule;
import haoran.dao.base.Page;
import haoran.utils.DateUtils;

/**
 * Struts2工具类.
 * 
 * 实现获取Request/Response/Session与绕过jsp/freemaker直接输出文本的简化函数.
 * 
 * @author calvin
 */
public class SpringMvcUtils {

	//-- header 常量定义 --//
	private static final String HEADER_ENCODING = "encoding";
	private static final String HEADER_NOCACHE = "no-cache";
	private static final String CONTENT_RANGE = "Content-Range";
	private static final String DEFAULT_ENCODING = "UTF-8";
	private static final boolean DEFAULT_NOCACHE = true;

	private static ObjectMapper mapper =new ObjectMapper();
	   static { // if you need to change default configuration:
		   mapper.setDateFormat(new SimpleDateFormat(DateUtils.defaultDatePattern));
		   mapper.configure(SerializationConfig.Feature.WRITE_EMPTY_JSON_ARRAYS , true); // faster this way, not default
		   mapper.registerModule(new HibernateModule());
			//设置输出包含的属性
		   mapper.getSerializationConfig().withSerializationInclusion(Inclusion.ALWAYS);
		   mapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>()  
	        {  
	            @Override  
	            public void serialize(  
	                    Object value,  
	                    JsonGenerator jg,  
	                    SerializerProvider sp) throws IOException, JsonProcessingException  
	            {  
	                jg.writeString("");  
	            }  
	        });  
		   }
	private static Log log = LogFactory
	.getLog(SpringMvcUtils.class);
	//-- 取得Request/Response/Session的简化函数 --//
	/**
	 * 取得HttpSession的简化函数.
	 */
	public static HttpSession getSession() {
		return getRequest().getSession();
	}

	/**
	 * 取得HttpSession的简化函数.
	 */
	public static HttpSession getSession(boolean isNew) {
		return getRequest().getSession(isNew);
	}

	/**
	 * 取得HttpSession中Attribute的简化函数.
	 */
	public static Object getSessionAttribute(String name) {
		HttpSession session = getSession(false);
		return (session != null ? session.getAttribute(name) : null);
	}
	
	/**
	 * 取得HttpSession中Attribute的简化函数.
	 */
	public static void setSessionAttribute(String name,Object value) {
		HttpSession session = getSession(false);
		if(session != null)  session.setAttribute(name, value);
	}
	
	public static void setRequestAttribute(String name,Object value){
		getRequest().setAttribute(name, value);
	}

	/**
	 * 取得HttpRequest的简化函数.
	 */
	public static HttpServletRequest getRequest() {
		return  ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
	}

	/**
	 * 取得HttpRequest中Parameter的简化方法.
	 */
	public static String getParameter(String name) {
		return filterDangerString(getRequest().getParameter(name));
	}

	
	public static String getHeader(String name) {
		return filterDangerString(getRequest().getHeader(name));
	}
	
	public static  String filterDangerString(String value) {
		if (value == null) {
			return null;
		}
		value = value.replaceAll("\\|", "");
		value = value.replaceAll("&", "&amp;");
//		value = value.replaceAll("@", "");
		value = value.replaceAll("'", "");
		value = value.replaceAll("\"", "");
		value = value.replaceAll("\\'", "");
		value = value.replaceAll("\\\"", "");
		value = value.replaceAll("<", "");
		value = value.replaceAll(">", "");
		value = value.replaceAll("\\(", "");
		value = value.replaceAll("\\)", "");
		value = value.replaceAll("\\+", "");
		value = value.replaceAll("\r", "");
		value = value.replaceAll("\n", "");
		value = value.replaceAll("eval", "");
		value = value.replaceAll("src", "");
		value = value.replaceAll("%", "");
		value = value.replaceAll("iframe", "");
		value = value.replaceAll("window", "");
		value = value.replaceAll("location", "");
		value = value.replaceAll("script", "");
		value = value.replaceAll("%27", "");
		value = value.replaceAll("%22", "");
		value = value.replaceAll("%3E", "");
		value = value.replaceAll("%3C", "");
		value = value.replaceAll("%3D", "");
		value = value.replaceAll("%2F", "");
		return value;
	}
	
	/**
	 * 取得HttpRequest中getParameterValues的简化方法.
	 */
	public static String [] getParameterValues(String name){
		return getRequest().getParameterValues(name);
	}
	
	/**
	 * 取得HttpResponse的简化函数.
	 */
	public static HttpServletResponse getResponse() {
		return ((ServletWebRequest)RequestContextHolder.getRequestAttributes()).getResponse();
	}

	
	/**
	 * 跳转到URL页面 
	 * @throws IOException 
	 */
	public static void sendRedirectUrl(String url) throws IOException {
		String  targetUrl = URLDecoder.decode(url, "UTF-8");
		RedirectStrategy defaultRedirectStrategy = new DefaultRedirectStrategy();
		defaultRedirectStrategy.sendRedirect(getRequest(), getResponse(), targetUrl);
	}
	
	/**
	 * 跳转回原来页面  使用sendRedirect  重定向
	 * sendRefererUrl:
	 * 适用:
	 * @throws IOException
	 * @throws ServletException 
	 * @exception 
	 * @since  1.0.0
	 */
	public static void sendRefererUrl() throws IOException, ServletException {
		sendRefererUrl(false);
	}
	
	/**
	 * 跳转回原来页面
	 * @throws IOException 
	 * @throws ServletException 
	 */
	public static void sendRefererUrl(boolean useForward) throws IOException, ServletException {
		HttpServletRequest request = getRequest();
		HttpServletResponse response = getResponse();
		String  targetUrl = SpringMvcUtils.getHeader("Referer");
		if(useForward) {
			RequestDispatcher dispatcher = request.getRequestDispatcher(targetUrl);
            dispatcher.forward(request, response);
		} else {
			RedirectStrategy defaultRedirectStrategy = new DefaultRedirectStrategy();
			defaultRedirectStrategy.sendRedirect(request, getResponse(), targetUrl);
		}
	}
	
	
	@SuppressWarnings("rawtypes")
	public static  Page getPageForHead(Page <?> page){
		String range = getRequest().getHeader("Range");
		if(StringUtils.isNotEmpty(range)){
			range = range.replace("items=", "");
			String []sande = StringUtils.split(range,"-");
			if(sande.length==2){
				int startRow = Integer.valueOf(sande[0]);
				page.setStartRow(startRow);
				int endRow =Integer.valueOf(sande[1]);
				page.setPageSize(endRow -startRow);
			}
		}
		return page;
	}


	/**
	 * 分析并设置contentType与headers.
	 */
//	private static HttpServletResponse initResponseHeader(final String contentType, final String... headers) {
//		//分析headers参数
//		String encoding = DEFAULT_ENCODING;
//		boolean noCache = DEFAULT_NOCACHE;
//		HttpServletResponse response = ServletActionContext.getResponse();
//		for (String header : headers) {
//			String headerName = StringUtils.substringBefore(header, ":");
//			String headerValue = StringUtils.substringAfter(header, ":");
//			if (StringUtils.equalsIgnoreCase(headerName, HEADER_ENCODING)) {
//				encoding = headerValue;
//			} else if (StringUtils.equalsIgnoreCase(headerName, HEADER_NOCACHE)) {
//				noCache = Boolean.parseBoolean(headerValue);
//			}else if (StringUtils.equalsIgnoreCase(headerName, CONTENT_RANGE)) {
//				response.setHeader(CONTENT_RANGE, headerValue);
//			}  else {
//				throw new IllegalArgumentException(headerName + "不是一个合法的header类型");
//			}
//		}
//		//设置headers参数
//		String fullContentType = contentType + ";charset=" + encoding;
//		response.setContentType(fullContentType);
//		if (noCache) {
//			ServletUtils.setDisableCacheHeader(response);
//		}
//
//		return response;
//	}
	
	/**
	 * 获得web程序的在服务器上的发布路径
	 * ServletContext:
	 * 适用:
	 * @return 
	 * @exception 
	 * @since  1.0.0
	 */
	public static ServletContext getServletContext() {
		return getSession().getServletContext();
	}

	/**
	 * 获得web程序的在服务器上的发布路径
	 * getAppRootPath:
	 * 适用:
	 * @return 
	 * @exception 
	 * @since  1.0.0
	 */
	public static String getAppRootPath() {

		return getServletContext().getRealPath("/").toString();
	}
	
	
	/**
	 * 获得web程序的classpath--localhost:8080/项目名称
	 * @return 
	 * @exception 
	 * @since  1.0.0
	 * */
	public static String getRootPath(){
		return getRequest().getContextPath();
	}
	

	
}
