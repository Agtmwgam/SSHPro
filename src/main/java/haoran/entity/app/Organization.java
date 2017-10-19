/**
 * <p>Copyright (c) 2016 深圳市鹏途交通科技有限公司</p>
 * <p>					All right reserved. 		 </p>
 * <p>项目名称 ： 	深圳公路信息资源整合及国省检日常化监管管理项目        </p>
 * <p>创建者   :	wy
 * <p>描   述  :  A1-08单位信息表   </p>
 * <p>最后修改 :  $: 2017-3-24-上午11:04:01	wy   $     </p>
 * 
*/

package haoran.entity.app;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import haoran.entity.base.IdEntity;

@Entity
@Table(name = "T_APP_ORGANIZATION")
public class Organization  extends IdEntity implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	private String type; //单位类型
	private String orgName; //单位名称
	private String shortName; //单位简称
	private String orgForm; //单位性质
	private String burderDep; //主管部门
	private String orgNum; //机构代码证
	private String principal; //负责人
	private String postCode; //邮政编码
	private String orgTelephone; //单位电话
	private String portraiture; //传真
	private String orgLevel; //级别
	private String email; //电子邮箱
	private String orgHome; //单位主页
	private String district; //区
	private Date finishDate; //成立时间
	private String politicsLevel; //行政级别
	private Date checkDate; //考核时间
	private String moneySource; //经费来源
	private String orgAddress; //单位地址
	private String functionsOrg; //单位职能
	private String basicInfo; //基本简况
	private String remark; //备注
	private Double manageArea; //管辖面积
	private Organization parent;
	private String parentIds;
	private String dockingId; //对接养护单位Id
	private List<Organization> childList = Lists.newArrayList(); // 下属单位
	
	private List<Attachment> attachments;

	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
	@Where(clause = " DELFLAG = '0' ")
	@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@JsonIgnore 
	public List<Organization> getChildList() {
		return childList;
	}

	public void setChildList(List<Organization> childList) {
		this.childList = childList;
	}
	
	
	
	@ManyToOne(targetEntity = Organization.class, 
			cascade = { CascadeType.PERSIST }, fetch = FetchType.LAZY)
//	@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@JoinColumn(name = "PARENTID", insertable = true, updatable = true)
	@JsonIgnore
	public Organization getParent() {
		return parent;
	}

	public void setParent(Organization parent) {
		this.parent = parent;
	}

	@Column(name = "PARENTIDS")
	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	@Column(name = "ORGNAME")
	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	@Column(name = "SHORTNAME")
	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	@Column(name = "BURDERDEP")
	public String getBurderDep() {
		return burderDep;
	}

	public void setBurderDep(String burderDep) {
		this.burderDep = burderDep;
	}

	@Column(name = "ORGNUM")
	public String getOrgNum() {
		return orgNum;
	}

	public void setOrgNum(String orgNum) {
		this.orgNum = orgNum;
	}

	@Column(name = "POSTCODE")
	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	@Column(name = "ORGTELEPHONE")
	public String getOrgTelephone() {
		return orgTelephone;
	}

	public void setOrgTelephone(String orgTelephone) {
		this.orgTelephone = orgTelephone;
	}

	@Column(name = "PORTRAITURE")
	public String getPortraiture() {
		return portraiture;
	}

	public void setPortraiture(String portraiture) {
		this.portraiture = portraiture;
	}

	@Column(name = "ORGLEVEL")
	public String getOrgLevel() {
		return orgLevel;
	}

	public void setOrgLevel(String orgLevel) {
		this.orgLevel = orgLevel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "ORGHOME")
	public String getOrgHome() {
		return orgHome;
	}

	public void setOrgHome(String orgHome) {
		this.orgHome = orgHome;
	}

	@Column(name = "FINISHDATE")
	@Temporal(TemporalType.DATE)
	public Date getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(Date finishDate) {
		this.finishDate = finishDate;
	}

	@Column(name = "POLITICSLEVEL")
	public String getPoliticsLevel() {
		return politicsLevel;
	}

	public void setPoliticsLevel(String politicsLevel) {
		this.politicsLevel = politicsLevel;
	}

	@Column(name = "CHECKDATE")
	@Temporal(TemporalType.DATE)
	public Date getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	@Column(name = "MONEYSOURCE")
	public String getMoneySource() {
		return moneySource;
	}

	public void setMoneySource(String moneySource) {
		this.moneySource = moneySource;
	}

	@Column(name = "ORGADDRESS")
	public String getOrgAddress() {
		return orgAddress;
	}

	public void setOrgAddress(String orgAddress) {
		this.orgAddress = orgAddress;
	}

	@Column(name = "FUNCTIONSORG")
	public String getFunctionsOrg() {
		return functionsOrg;
	}

	public void setFunctionsOrg(String functionsOrg) {
		this.functionsOrg = functionsOrg;
	}

	@Column(name = "BASICINFO")
	public String getBasicInfo() {
		return basicInfo;
	}

	public void setBasicInfo(String basicInfo) {
		this.basicInfo = basicInfo;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


	@Column(name = "TYPE")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "ORGFORM")
	public String getOrgForm() {
		return orgForm;
	}

	public void setOrgForm(String orgForm) {
		this.orgForm = orgForm;
	}

	@Column(name = "PRINCIPAL")
	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}
	

	@Column(name = "DISTRICT")
	public String getDistrict() {
		return district;
	}


	public void setDistrict(String district) {
		this.district = district;
	}
	
	@Column(name="DOCKINGID")
	public String getDockingId() {
		return dockingId;
	}

	public void setDockingId(String dockingId) {
		this.dockingId = dockingId;
	}

	@Transient
	public static void sortList(List<Organization> list, List<Organization> sourcelist, String parentId){
		for (int i=0; i<sourcelist.size(); i++){
			Organization e = sourcelist.get(i);
			if (e.getParent()!=null && e.getParent().getId()!=null
					&& e.getParent().getId().equals(parentId)){
				list.add(e);
				// 判断是否还有子节点, 有则继续获取子节点
				for (int j=0; j<sourcelist.size(); j++){
					Organization child = sourcelist.get(j);
					if (child.getParent()!=null && child.getParent().getId()!=null
							&& child.getParent().getId().equals(e.getId())){
						sortList(list, sourcelist, e.getId());
						break;
					}
				}
			}
		}
	}
	
	@Column(name = "MANAGEAREA")
	public Double getManageArea() {
		return manageArea;
	}

	public void setManageArea(Double manageArea) {
		this.manageArea = manageArea;
	}
	
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "PARTID")
	public List<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}
}