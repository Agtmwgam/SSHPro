package haoran.entity.base;

import java.security.GeneralSecurityException;
import java.util.Properties;

import com.sun.mail.util.MailSSLSocketFactory;

/**
 * 邮件内容实体类
 * 
 * MailInfo
 * 
 * 2017年4月19日 下午2:23:29
 * 
 * @version 1.0.0
 *
 */
public class MailInfo {

	private String mailHost; // 发送邮件的服务器的IP

	private String mailPort = "465"; // 发送邮件的服务器的端口 qq邮箱默认为465或587

	private String username; // 发送邮件的用户名（邮箱全名称）

	private String password; // 发送邮件的密码

	private String errorTo; // 错误信息发送地址（多个邮件地址以";"分隔）

	private String errorCc; // 错误信息抄送地址（多个邮件地址以";"分隔）

	private String warningTo; // 警告信息发送地址（多个邮件地址以";"分隔）

	private String warningCc; // 警告信息抄送地址（多个邮件地址以";"分隔）

	private String notifyTo; // 通知信息发送地址（多个邮件地址以";"分隔）

	private String notifyCc; // 通知信息抄送地址（多个邮件地址以";"分隔）

	private String subject; // 邮件主题

	private String content; // 邮件内容

	private String[] attachFileNames; // 邮件附件的文件名
	
	public Properties getProperties() throws GeneralSecurityException {
		Properties props = new Properties();
		props.put("mail.smtp.host", getMailHost());
		props.put("mail.smtp.port", getMailPort());
		props.put("mail.smtp.auth", "true");
		props.put("main.smtp.starttls.enable", "true");
		
		MailSSLSocketFactory  sslSF = new MailSSLSocketFactory();
		sslSF.setTrustAllHosts(true);
		props.put("mail.smtp.ssl.enable", "true");
		props.put("mail.smtp.ssl.socketFactory", sslSF);
		props.put("mail.smtp.transport.protocol", "smtp");
		
		props.put("mail.user", getUsername());
		props.put("mail.password", getPassword());
		return props;
	}

	public String getMailHost() {
		return mailHost;
	}


	public void setMailHost(String mailHost) {
		this.mailHost = mailHost;
	}


	public String getMailPort() {
		return mailPort;
	}


	public void setMailPort(String mailPort) {
		this.mailPort = mailPort;
	}


	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getErrorTo() {
		return errorTo;
	}

	public void setErrorTo(String errorTo) {
		this.errorTo = errorTo;
	}

	public String getErrorCc() {
		return errorCc;
	}

	public void setErrorCc(String errorCc) {
		this.errorCc = errorCc;
	}

	public String getWarningTo() {
		return warningTo;
	}

	public void setWarningTo(String warningTo) {
		this.warningTo = warningTo;
	}

	public String getWarningCc() {
		return warningCc;
	}

	public void setWarningCc(String warningCc) {
		this.warningCc = warningCc;
	}

	public String getNotifyTo() {
		return notifyTo;
	}

	public void setNotifyTo(String notifyTo) {
		this.notifyTo = notifyTo;
	}

	public String getNotifyCc() {
		return notifyCc;
	}

	public void setNotifyCc(String notifyCc) {
		this.notifyCc = notifyCc;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String[] getAttachFileNames() {
		return attachFileNames;
	}

	public void setAttachFileNames(String[] attachFileNames) {
		this.attachFileNames = attachFileNames;
	}
	
	
}
