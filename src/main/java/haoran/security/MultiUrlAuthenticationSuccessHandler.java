/**
 * <p>Copyright (c) 2011 深圳市鹏途信息技术有限公司 </p>
 * <p>				   All right reserved. 		     </p>
 * 
 * <p>创建者   :	kenfo
 * 
 * <p>描   述  :   MultiSuccessHandler.java for com.pengtu.security    </p>
 * 
 * <p>最后修改 : $: 2011-7-4-上午11:18:32 v 1.0.0	 ce   $     </p>
 * 
*/

package haoran.security;


import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import haoran.entity.app.User;
import haoran.service.system.UserService;
import haoran.utils.DateUtils;


/**
 * 
 * MultiSuccessHandler
 * 
 * 2011-7-4 上午11:18:32
 * 
 * @version 1.0.0
 * 
 */
public class MultiUrlAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler  {
		protected final Log logger = LogFactory.getLog(this.getClass());
		
    @Autowired
    private UserService userService;
    private Integer maxDay; //允许不修改密码的最长时间

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws ServletException, IOException {
        User user = userService.getUserByUsername(authentication.getName());
//			当第一次登录没有修改密码或者是距离上次修改密码的时间超过90天 则跳转到修改密码界面
			if (user.getLastChangePwdTime()==null||DateUtils.betweenDays(new Date(), user.getLastChangePwdTime())>=maxDay) {
				 String targetUrl = "/user/sysFirstChangePw.do?id="+user.getId(); //修改密码地址
				 getRedirectStrategy().sendRedirect(request, response, targetUrl);
			}
			//修改用户的登录时间
			user.setLatestLoginTime(new Date());
			//成功登录之后将失败登录次数设置为0 
			user.setAttempts(0);
			userService.saveUser(user);
			super.onAuthenticationSuccess(request, response, authentication);
			return ;
    }

	public Integer getMaxDay() {
		return maxDay;
	}

	public void setMaxDay(Integer maxDay) {
		this.maxDay = maxDay;
	}

}
