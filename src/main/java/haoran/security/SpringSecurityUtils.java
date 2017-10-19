/**
 * Copyright (c) 2005-2009 springside.org.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * 
 * $Id: SpringSecurityUtils.java,v 1.1 2016/03/15 05:50:48 zwm Exp $
 */
package haoran.security;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletWebRequest;

import haoran.entity.app.User;

/**
 * SpringSecurity的工具类.
 * 
 * 注意. 本类只支持SpringSecurity 3.0.x.
 * 
 * @author calvin
 */
public class SpringSecurityUtils {

	/**
	 * 取得当前用户, 返回值为SpringSecurity的User类或其子类, 如果当前用户未登录则返回null.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends UserDetails> T getCurrentUser() {
		Authentication authentication = getAuthentication();
		if (authentication == null) {
			return null;
		}
		Object principal = authentication.getPrincipal();
		if (!(principal instanceof UserDetails)) {
			HttpServletResponse response = ((ServletWebRequest)RequestContextHolder.getRequestAttributes()).getResponse();
			if(null != response){
				response.addHeader("__timeout","true");
			}
			return null;
		}
		return (T) principal;
	}

	/**
	 * 取得当前用户的登录名, 如果当前用户未登录则返回空字符串.
	 */
	public static String getCurrentLoginName() {
		Authentication authentication = getAuthentication();

		if (authentication == null || authentication.getPrincipal() == null) {
			return "";
		}

		return authentication.getName();
	}
	
	/**
	 * 取得当前用户的登录名, 如果当前用户未登录则返回空字符串.
	 */
	public static String getCurrentUserName() {
		User user = getCurrentUser();
		if (user == null) {
			return "";
		}else {
			return user.getUsername();
		}
	}
	
	/**
	 * 取得当前用户的姓名, 如果当前用户未登录则返回空字符串.
	 */
	public static String getCurrentName() {
		User user = getCurrentUser();
		if (user == null) {
			return "";
		}else {
			return user.getName();
		}
	}
	
	/**
	 * 取得当前用户的角色
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonGenerationException 
	 */
	public static String getCurrentUserRole() throws JsonGenerationException, 
				JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		User user = getCurrentUser();
		return mapper.writeValueAsString(user.getRoleList());
	}

	/**
	 * 取得当前用户登录IP, 如果当前用户未登录则返回空字符串.
	 */
	public static String getCurrentUserIp() {
		Authentication authentication = getAuthentication();
		if (authentication == null) {
			return "";
		}
		Object details = authentication.getDetails();
		if (!(details instanceof WebAuthenticationDetails)) {
			return "";
		}
		WebAuthenticationDetails webDetails = (WebAuthenticationDetails) details;
		return webDetails.getRemoteAddress();
	}
	

	/**
	 * 将UserDetails保存到Security Context.
	 * 
	 * @param userDetails 已初始化好的用户信息.
	 * @param request 用于获取用户IP地址信息,可为Null.
	 */
	public static void saveUserDetailsToContext(UserDetails userDetails, HttpServletRequest request) {
		PreAuthenticatedAuthenticationToken authentication = new PreAuthenticatedAuthenticationToken(userDetails,
				userDetails.getPassword(), userDetails.getAuthorities());
		if (request != null) {
			authentication.setDetails(new WebAuthenticationDetails(request));
		}
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	/**
	 * 取得Authentication, 如当前SecurityContext为空时返回null.
	 */
	public static Authentication getAuthentication() {
		SecurityContext context = SecurityContextHolder.getContext();
		if (context == null) {
			return null;
		}
		return context.getAuthentication();
	}
	/**
	 * 处理登录的页面
	 * determineLoginUrlToUseForThisRequest:
	 * 适用:
	 * @param request
	 * @param response
	 * @return 
	 * @exception 
	 * @since  1.0.0
	 */
    public static String determineLoginUrlToUseForThisRequest(HttpServletRequest request, 
    		HttpServletResponse response){
        String targetUrl = null;  
        String uri = request.getRequestURL().toString();
        if ((StringUtils.hasText(uri) && uri.indexOf("obusiness") != -1)) {  
            targetUrl = "/obusiness/login.jsp";  
        } else {  
            targetUrl = "/login.action";
        }  
        return targetUrl;
    }
	
	/**
	 * 处理登录的页面
	 * determineLoginUrlToUseForThisRequest:
	 * 适用:
	 * @param request
	 * @param response
	 * @return 
	 * @exception 
	 * @since  1.0.0
	 */
    public static String determineLogoutUrlToUseForThisRequest(HttpServletRequest request,
    		HttpServletResponse response){
        String targetUrl = null;  
        String uri = request.getHeader("Referer");
        if (StringUtils.hasText(uri) && uri.indexOf("obusiness") != -1) {  
            targetUrl = "/obusiness/login.jsp";  
        } else {  
//            targetUrl = "/login.action";
            targetUrl = "/login.jsp?error=1";
        }  
        return targetUrl;
    }
    
    /**
     * 处理登录成功后的页面
     * determineDefaultTarget:
     * 适用:
     * @param request
     * @param response
     * @return 
     * @exception 
     * @since  1.0.0
     */
    public static String determineDefaultTarget(HttpServletRequest request, 
    		HttpServletResponse response){
        String targetUrl = null;  
        String uri = request.getHeader("Referer"); 
        if (StringUtils.hasText(uri) && uri.indexOf("obusiness") != -1) {  
            targetUrl = "/obusiness/";  
        } else {  
            targetUrl = "/";
        }  
        return targetUrl;
    }  
    
	/**
	 * 判断用户是否拥有角色, 如果用户拥有参数中的任意一个角色则返回true.
	 */
	public static boolean hasAnyRole(String... roles) {
		Authentication authentication = getAuthentication();
		if (authentication == null) {
			return false;
		}
		Collection<? extends GrantedAuthority> grantedAuthorityList = authentication.getAuthorities();
		for (String role : roles) {
			for (GrantedAuthority authority : grantedAuthorityList) {
				if (role.equals(authority.getAuthority())) {
					return true;
				}
			}
		}
		return false;
	}


}
