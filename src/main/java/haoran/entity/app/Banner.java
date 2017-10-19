/**
 * <p>Copyright (c) 2016 深圳市鹏途交通科技有限公司</p>
 * <p>					All right reserved. 		 </p>
 * <p>项目名称 ： 	深圳公路信息资源整合及国省检日常化监管管理项目        </p>
 * <p>创建者   :	wy
 * <p>描   述  :  A1-05Banner表  </p>
 * <p>最后修改 :  $: 2017-3-24-上午11:04:01	wy   $     </p>
 * 
*/
package haoran.entity.app;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import haoran.entity.base.IdEntity;

@Entity
@Table(name = "T_APP_BANNER")
public class Banner extends IdEntity {


	private static final long serialVersionUID = 1L;

	private String name; //名称
	private String path; //地址
	private Integer sort; //排序
	private String uploadPerson; //上传人
	private String isShow; //是否显示
	private String remark; //备注
	private String bannerContent ;
	

	public Banner() {
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "PATH")
	public String getPath() {
		return path;
	}
	
	public void setPathName(String path) {
		this.path = path;
	}


	@Column(name = "UPLOADPERSON")
	public String getUploadPerson() {
		return uploadPerson;
	}

	public void setUploadPerson(String uploadPerson) {
		this.uploadPerson = uploadPerson;
	}
	@Column(name="SORT")
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	@Column(name = "ISSHOW")
	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}
	

	@Column(name = "REMARK")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "BANNERCONTENT")
	public String getBannerContent() {
		return bannerContent;
	}

	public void setBannerContent(String bannerContent) {
		this.bannerContent = bannerContent;
	}


}
