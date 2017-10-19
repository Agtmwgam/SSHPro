/**
 * <p>Copyright (c) 2016 深圳市鹏途交通科技有限公司</p>
 * <p>					All right reserved. 		 </p>
 * <p>项目名称 ： 	深圳公路信息资源整合及国省检日常化监管管理项目        </p>
 * <p>创建者   :	wy
 * <p>描   述  :  A1-02码表   </p>
 * <p>最后修改 :  $: 2017-3-24-上午11:04:01	wy   $     </p>
 * 
*/

package haoran.entity.app;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import haoran.entity.base.IdEntity;
@Entity
@Table(name = "T_APP_CODE")
public class Code extends IdEntity{

	private static final long serialVersionUID = -6429870906188976336L;

	private Integer category; // 类型
	private String codeId;    // 编码
	private String codeName;	// 中文
	private String categoryDesc; // 表述
	private String description;	 // 描述
	private String useFlag;    //是否启用
	private String remark;
	
	public Code() {
	}

	@Column(name = "CATEGORY")
	public Integer getCategory() {
		return category;
	}

	public void setCategory(Integer category) {
		this.category = category;
	}

	@Column(name = "CODEID")
	public String getCodeId() {
		return codeId;
	}

	public void setCodeId(String codeId) {
		this.codeId = codeId;
	}

	@Column(name = "CODENAME")
	public String getCodeName() {
		return codeName;
	}

	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}

	@Column(name = "CATEGORYDESC")
	public String getCategoryDesc() {
		return categoryDesc;
	}

	public void setCategoryDesc(String categoryDesc) {
		this.categoryDesc = categoryDesc;
	}

	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "USEFLAG")
	public String getUseFlag() {
		return useFlag;
	}

	public void setUseFlag(String useFlag) {
		this.useFlag = useFlag;
	}

	@Column(name = "REMARK")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
