package haoran.utils;

import javax.servlet.http.HttpServletRequest;

/** 
 * @author zl 
 * @version 创建时间：2017年3月27日 下午4:04:43 
 * 类说明 
 */
public class ControllerTools {

	public static boolean isAjaxRequest(HttpServletRequest request){
		 String header = request.getHeader("X-Requested-With");
	        if (header != null && "XMLHttpRequest".equals(header))
	            return true;
	        else
	            return false;
	}
}
