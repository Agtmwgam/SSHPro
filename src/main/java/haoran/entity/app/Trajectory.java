package haoran.entity.app;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import haoran.entity.base.IdEntity;

/**
 * 
 * 历史轨迹信息表
 * Trajectory
 * 
 * 2017年9月4日 上午9:34:44
 * 
 * @version 1.0.0
 *
 */
@Entity
@Table(name = "T_APP_TRAJECTORY")
public class Trajectory extends IdEntity {

	/**
	 * serialVersionUID:TODO
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = 1L;
	
	private String sysmenuId; //操作位置id
	private String sysmenuName; //操作菜单名称
	private String userId; //操作人id
	private String userName; //操作人名称
	private Date operateTime; //操作时间
	private String operateHref; //操作地址
	private String remark;
	
	
	@Column(name = "SYSMENUID")
	public String getSysmenuId() {
		return sysmenuId;
	}
	public void setSysmenuId(String sysmenuId) {
		this.sysmenuId = sysmenuId;
	}
	@Column(name = "SYSMENUNAME")
	public String getSysmenuName() {
		return sysmenuName;
	}
	public void setSysmenuName(String sysmenuName) {
		this.sysmenuName = sysmenuName;
	}
	@Column(name = "USERID")
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	@Column(name = "USERNAME")
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	@Column(name = "OPERATETIME")
	public Date getOperateTime() {
		return operateTime;
	}
	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}
	
	@Column(name = "OPERATEHREF")
	public String getOperateHref() {
		return operateHref;
	}
	public void setOperateHref(String operateHref) {
		this.operateHref = operateHref;
	}
	
	@Column(name = "REMARK")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}
