package haoran.entity.app;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import haoran.entity.base.IdEntity;

/**
 * 
 * 系统预警表
 * Warning
 * 
 * 2017年9月1日 下午5:50:28
 * @author zl
 * @version 1.0.0
 *
 */
@Entity
@Table(name = "T_APP_WARNING")
public class Warning extends IdEntity {

	/**
	 * serialVersionUID:TODO
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = 1L;

	private String warContent; // 预警内容
	
	private String notifier; // 通知人
	
	private Date notifiTime; // 预警时间
	
	private String projectId; // 相关项目id
	
	private String status; // 预警状态 0 预警中 1 关闭
	
	private String href;

	@Column(name = "WARCONTENT")
	public String getWarContent() {
		return warContent;
	}

	public void setWarContent(String warContent) {
		this.warContent = warContent;
	}

	@Column(name = "NOTIFIER")
	public String getNotifier() {
		return notifier;
	}

	public void setNotifier(String notifier) {
		this.notifier = notifier;
	}

	@Column(name = "NOTIFITIME")
	public Date getNotifiTime() {
		return notifiTime;
	}

	public void setNotifiTime(Date notifiTime) {
		this.notifiTime = notifiTime;
	}

	@Column(name = "PROJECTID")
	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	@Column(name = "STATUS")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "HREF")
	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}
	
	
}
