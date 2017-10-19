package haoran.entity.check;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.context.annotation.Lazy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Lists;
import haoran.entity.app.Sysmenu;
import haoran.entity.base.IdEntity;

@Entity
@Table(name = "T_CK_CHECKPROJECT")
@JsonIgnoreProperties(value = { "menuList", "checkTables" })
public class CheckProject extends IdEntity {

	/**
	 * serialVersionUID:TODO
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = 2040009411424091166L;
	private String projectName; //项目名称
	private String startTime; //起始时间
	private String endTime; //结束时间
	private String content; //概述
	private String status; //状态  0 编辑  1发布
	private String countryOrProvince;  //国省检  0 国检 1 省检
	private String remark; //备注
	private String showHref; //默认详情的链接
	
	private List<Sysmenu> menuList = Lists.newArrayList(); // 拥有菜单列表
	private List<CheckTable> checkTables = new ArrayList<CheckTable>();
	
	private Integer menuListSize = 0;
	
	public  CheckProject(){
		
	}

	@Column(name = "PROJECTNAME")
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	@Column(name = "STARTTIME")
	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	@Column(name = "ENDTIME")
	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	@Column(name = "CONTENT")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "STATUS")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "COUNTRYORPROVINCE")
	public String getCountryOrProvince() {
		return countryOrProvince;
	}

	public void setCountryOrProvince(String countryOrProvince) {
		this.countryOrProvince = countryOrProvince;
	}

	@Column(name = "REMARK")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	@Column(name = "SHOWHREF")
	public String getShowHref() {
		return showHref;
	}

	public void setShowHref(String showHref) {
		this.showHref = showHref;
	}

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
	@JoinTable(name="R_APP_CHECKMENU",joinColumns={@JoinColumn(name="CHECKID")},
	        inverseJoinColumns = {@JoinColumn(name="MENUID")})
	@Fetch(FetchMode.SUBSELECT)
	@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@JsonIgnore
	public List<Sysmenu> getMenuList() {
		return menuList;
	}

	public void setMenuList(List<Sysmenu> menuList) {
		this.menuList = menuList;
	}

	@OneToMany(mappedBy="checkProject")
	@Lazy(true)
	public List<CheckTable> getCheckTables() {
		return checkTables;
	}

	public void setCheckTables(List<CheckTable> checkTables) {
		this.checkTables = checkTables;
	}	
	
	@Transient
	public Integer getMenuListSize() {
		menuListSize = menuList.size();
		return menuListSize;
	}

	public void setMenuListSize(Integer menuListSize) {
		this.menuListSize = menuListSize;
	}

	/**
	 * 国省检保存菜单
	 * @return
	 */
	@Transient
	public String getMenuIds() {
		List<String> nameIdList = Lists.newArrayList();
		for (Sysmenu menu : menuList) {
			nameIdList.add(menu.getId());
		}
		return StringUtils.join(nameIdList, ",");
	}

	@Transient
	public void setMenuIds(String menuIds) {
		menuList = Lists.newArrayList();
		if (menuIds != null){
			String[] ids = StringUtils.split(menuIds, ",");
			for (String menuId : ids) {
				Sysmenu menu = new Sysmenu();
				menu.setId(menuId);
				menuList.add(menu);
			}
		}
	}
	
	@Transient
	public List<Sysmenu> getTableSysmenu() {
		List<Sysmenu> list = new ArrayList<Sysmenu>();
		Map<String, Sysmenu> map = new HashMap<String, Sysmenu>();
		for (Sysmenu sysmenu : menuList) {
			map.put(sysmenu.getParent().getId(), sysmenu);
		}
		for (Sysmenu sysmenu : menuList) {
			if (!map.containsKey(sysmenu.getId())) {
				list.add(sysmenu);
			}
		}
		return list;
	}
	
}
