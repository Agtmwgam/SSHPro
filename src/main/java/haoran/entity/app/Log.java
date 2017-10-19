/**
 * <p>Copyright (c) 2016 深圳市鹏途交通科技有限公司</p>
 * <p>					All right reserved. 		 </p>
 * <p>项目名称 ： 	深圳公路信息资源整合及国省检日常化监管管理项目        </p>
 * <p>创建者   :	wy
 * <p>描   述  :  A1-07日志表    </p>
 * <p>最后修改 :  $: 2017-3-24-上午11:04:01	wy   $     </p>
 * 
 */
package haoran.entity.app;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import haoran.entity.base.IdEntity;

@Entity
@Table(name = "T_APP_LOG")
public class Log extends IdEntity {

	private static final long serialVersionUID = 1L;
	private String username; // 操作用户名
	private String opTime; // 操作时间
	private String className; // 操作类名
	private String opID; // 操作记录ID
	private String opType; // 操作类型
	private String opComment; // 操作内容
	private String remark;

	public Log() {

	}

	@Column(name = "USERNAME")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "OPTIME")
	public String getOpTime() {
		return opTime;
	}

	public void setOpTime(String opTime) {
		this.opTime = opTime;
	}

	@Column(name = "CLASSNAME")
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@Column(name = "OPID")
	public String getOpID() {
		return opID;
	}

	public void setOpID(String opID) {
		this.opID = opID;
	}

	@Column(name = "OPTYPE")
	public String getOpType() {
		return opType;
	}

	public void setOpType(String opType) {
		this.opType = opType;
	}

	@Column(name = "OPCOMMENT")
	public String getOpComment() {
		return opComment;
	}

	public void setOpComment(String opComment) {
		this.opComment = opComment;
	}

	@Column(name = "REMARK")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	} // 备注

}
