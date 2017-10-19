package haoran.utils.exception;

/**
 * 全局公用的Exception.
 * 
 * 继承自RuntimeException
 *
 * @author zl
 */
public class ControllerException extends RuntimeException {

	private static final long serialVersionUID = 3583566093089790852L;

	public ControllerException() {
		super();
	}

	public ControllerException(String message) {
		super(message);
	}

	public ControllerException(Throwable cause) {
		super(cause);
	}

	public ControllerException(String message, Throwable cause) {
		super(message, cause);
	}
}
