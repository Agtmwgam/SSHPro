package haoran.security;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import haoran.utils.ControllerTools;

/** 
 * @author zl 
 * @version 创建时间：2017年3月27日 下午3:40:50 
 * 类说明 
 */
public class DefaultAccessDeniedHandler implements AccessDeniedHandler {
	
	private String errorPage;

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		boolean isAjax = ControllerTools.isAjaxRequest(request); 
		if(!isAjax){
            request.setAttribute("isAjaxRequest", isAjax);
            request.setAttribute("message", accessDeniedException.getMessage());
            RequestDispatcher dispatcher = request.getRequestDispatcher(errorPage);
            dispatcher.forward(request, response);
        }else{
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/plain");
            response.getWriter().write("权限不足");
            response.getWriter().close();
        }
	}

	public void setErrorPage(String errorPage) {
		if ((errorPage!=null)&&(!errorPage.startsWith("/"))) {
			throw new IllegalArgumentException("errorPage must begin with '/'");
		}
		this.errorPage = errorPage;
	}
}
