package haoran.utils.exception;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author zl
 * @version 创建时间：2017年4月11日 下午1:44:54 
 * @desc  系统统一解决异常的类
 */
public class MyExceptionHandler implements HandlerExceptionResolver {
	
	private static  Logger logger = Logger.getLogger(MyExceptionHandler.class);

	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("ex", ex);
		ex.printStackTrace();
		logger.info("这是异常信息：");
		logger.info(ex);
		// 根据不同错误转向不同页面
		if (ex instanceof BusinessException) {
			return new ModelAndView("exception/error-business", model);
		} else if (ex instanceof ParameterException) {
			return new ModelAndView("exception/error-parameter", model);
		} else {
			return new ModelAndView("exception/error", model);
		}
	}

}
