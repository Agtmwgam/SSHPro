/**
 * <p>Copyright (c) 2011 深圳市鹏途信息技术有限公司 </p>
 * <p>				   All right reserved. 		     </p>
 * 
 * <p>项目名称 ： 	东莞交通工程质量监督综合业务系统        </p>
 * <p>创建者   :	ce 
 * 
 * <p>描   述  :   MultiAuthenticationFailureHandler.java for com.pengtu.security    </p>
 * 
 * <p>最后修改 : $: 2011-7-4-上午11:30:11 v 1.0.0	 ce   $     </p>
 * 
*/

package haoran.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.util.StringUtils;
//import com.pengtu.entity.app.UserAttempts;
//import com.pengtu.service.app.AccountManager;
//import com.pengtu.service.app.UserAttemptsManager;
//import com.pengtu.utils.web.Struts2Utils;

import haoran.entity.app.User;
import haoran.service.system.UserService;

/**
 * 
 * MultiUrlAuthenticationFailureHandler
 * 
 * 2011-7-4 上午11:30:11
 * 
 * @version 1.0.0
 * 
 */
public class MultiUrlAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    public static String DEFAULT_TARGET_PARAMETER = "redirectUrl";
    private String targetUrlParameter = DEFAULT_TARGET_PARAMETER;
	private String defaultFailureUrl;
	private Integer maxFailureCount; 
	@Autowired
	private UserService userService;
    /**
     * Performs the redirect or forward to the {@code defaultFailureUrl} if set, otherwise returns a 401 error code.
     * <p>
     * If redirecting or forwarding, {@code saveException} will be called to cache the exception for use in
     * the target view.
     */
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        if (defaultFailureUrl == null) {
            logger.debug("No failure URL set, sending 401 Unauthorized error");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication Failed: " + exception.getMessage());
        } else {
            String targetUrl =request.getParameter(targetUrlParameter);
            if (StringUtils.hasText(targetUrl)) {
            	defaultFailureUrl = defaultFailureUrl + (defaultFailureUrl.contains("?")?"&":"?")+ DEFAULT_TARGET_PARAMETER +"=" + targetUrl;	
            }
            saveException(request, exception);
			@SuppressWarnings("deprecation")
			User user = userService.getUserByName(exception.getAuthentication().getName());
            if (user!=null) {
            	if(user.getAttempts()+1>maxFailureCount){ // 如果已经失败5次 
            		user.setAttempts(maxFailureCount);
            	}else {
            		user.setAttempts(user.getAttempts()+1);  // 登录失败次数加1
				}
				userService.saveUser(user);
				if(user.getAttempts()>=maxFailureCount){ //当登录失败次数大于最大允许值时 设置该用户账号为锁定状态
						user.setLocked("1");
						userService.saveUser(user);
				}
//				model.addAttribute("failureCount", maxFailureCount-user.getAttempts());
//				model.addAttribute("maxFailureCount", maxFailureCount);
				request.getSession().setAttribute("failureCount",maxFailureCount-user.getAttempts());
	            request.getSession().setAttribute("maxFailureCount",maxFailureCount);
			}else{
				request.getSession().removeAttribute("failureCount");
			}
           
            if (this.isUseForward()) {
                logger.debug("Forwarding to " + defaultFailureUrl);
                request.getRequestDispatcher(defaultFailureUrl).forward(request, response);
            } else {
                logger.debug("Redirecting to " + defaultFailureUrl);
                this.getRedirectStrategy().sendRedirect(request, response, defaultFailureUrl);
            }
        }
    }

	public String getTargetUrlParameter() {
		return targetUrlParameter;
	}

	public void setTargetUrlParameter(String targetUrlParameter) {
		this.targetUrlParameter = targetUrlParameter;
	}

	public String getDefaultFailureUrl() {
		return defaultFailureUrl;
	}

	public void setDefaultFailureUrl(String defaultFailureUrl) {
		this.defaultFailureUrl = defaultFailureUrl;
	}

	public Integer getMaxFailureCount() {
		return maxFailureCount;
	}

	public void setMaxFailureCount(Integer maxFailureCount) {
		this.maxFailureCount = maxFailureCount;
	}
	
	

}
