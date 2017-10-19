package haoran.entity.app;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import haoran.entity.base.IdEntity;

/**
 * 角色.
 * 
 * 注释见{@link User}.
 * 
 * @author calvin
 */
@Entity
@Table(name = "T_APP_ROLE")
@JsonAutoDetect
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Role extends IdEntity implements GrantedAuthority{
	/**
	 * 
	 */
	public static final String AUTHORITY_PREFIX = "ROLE_";
	//1:所有数据 2：本公司及以下数据  3：个人数据
	public static final String DATA_SCOPE_ALL = "1";
	public static final String DATA_SCOPE_ORG_AND_CHILD = "2";
	public static final String DATA_SCOPE_SELF = "3";
	
	private static final long serialVersionUID = -8532109958070142630L;
	private String roleName;
	private String roleDesc;
	private String remark;
	private String category; //类型
	private String useFlag;    //是否启用
	
	private List<Sysmenu> menuList = Lists.newArrayList(); // 拥有菜单列表
	
	public Role() {

	}
	
	public Role(String id, String name) {
		this.id = id;
		this.roleName = name;
	}
	

	@Column(nullable = false,unique = true,length = 50,name="ROLENAME")
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	@Column(length = 200,name="ROLEDESC")
	public String getRoleDesc() {
		return roleDesc;
	}
	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}
	
	@Column(length = 400,name="REMARK")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
	@JoinTable(name="R_APP_ROLEMENU",joinColumns={@JoinColumn(name="ROLEID")},
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
	
	@Transient
	public List<String> getMenuIdList() {
		List<String> menuIdList = Lists.newArrayList();
		for (Sysmenu menu : menuList) {
			menuIdList.add(menu.getId());
		}
		return menuIdList;
	}
	/**
	 * 角色保存菜单
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
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Transient
	@Override
	public String getAuthority() {
		return AUTHORITY_PREFIX + roleName;
	}

	@Column(name="CATEGORY")
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	@Column(name = "USEFLAG")
	public String getUseFlag() {
		return useFlag;
	}

	public void setUseFlag(String useFlag) {
		this.useFlag = useFlag;
	}
	
}
