package haoran.entity.base;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * 发送邮件时的身份验证器 MailAuthenticator
 * 
 * 2017年4月19日 下午2:22:57
 * 
 * @version 1.0.0
 * 
 */
public class MailAuthenticator extends Authenticator {

	private String userName = null; // 用户账号

	private String password = null; // 用户口令
	
	public MailAuthenticator(String userName, String password){
		this.userName = userName;
		this.password = password;
	}
	
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(userName, password);
	}
}
