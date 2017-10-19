package haoran.entity.base;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * 含审计信息的Entity基类.
 * 
 * @author calvin
 */
@MappedSuperclass
public class AuditableEntity extends IdEntity {

	/** 
	 * 
	 */
	private static final long serialVersionUID = 8121673157674037872L;
	protected String createBy;
	protected Date lastModifyTime;
	protected String lastModifyBy;


	/**
	 * 创建的操作员的登录名.
	 */
	@Column(updatable = false,name="CREATEBY")
	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	/**
	 * 最后修改时间.
	 */
	//本属性只在update时有效,save时无效.
	@Column(insertable = false,name="LASTMODIFYTIME")
	public Date getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(Date lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	/**
	 * 最后修改的操作员的登录名.
	 */
	@Column(insertable = false,name="LASTMODIFYBY")
	public String getLastModifyBy() {
		return lastModifyBy;
	}

	public void setLastModifyBy(String lastModifyBy) {
		this.lastModifyBy = lastModifyBy;
	}
}
