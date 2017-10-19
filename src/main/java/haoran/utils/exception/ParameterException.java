package haoran.utils.exception;
/** 
 * @author zl 
 * @version 创建时间：2017年4月11日 下午1:47:33 
 * 类说明 
 */
public class ParameterException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 781155230395932439L;

	public ParameterException() {
		super();
	}

	public ParameterException(String message) {
		super(message);
	}

	public ParameterException(Throwable cause) {
		super(cause);
	}

	public ParameterException(String message, Throwable cause) {
		super(message, cause);
	}
}
