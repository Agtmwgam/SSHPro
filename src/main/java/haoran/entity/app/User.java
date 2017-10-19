/**
 * <p>Copyright (c) 2016 深圳市鹏途交通科技有限公司</p>
 * <p>					All right reserved. 		 </p>
 * <p>项目名称 ： 	深圳公路信息资源整合及国省检日常化监管管理项目        </p>
 * <p>创建者   :	wy
 * <p>描   述  :  A1-01用户信息表   </p>
 * <p>最后修改 :  $: 2017-3-24-上午11:04:01	wy   $     </p>
 * 
*/
package haoran.entity.app;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
import javax.persistence.ManyToOne;
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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import haoran.entity.base.IdEntity;
import haoran.utils.reflection.ConvertUtils;

@Entity
// 表名与类名不相同时重新定义表名.
@Table(name = "T_APP_USER")
@JsonAutoDetect 
//@JsonIgnoreProperties (value = { "roleList","roleNames"}) 
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class User extends IdEntity implements UserDetails {

	private static final long serialVersionUID = 1L;
	private String name; //姓名
	private String username; //用户名
	private String password; //密码
	private Date latestLoginTime; //上次登录时间
	private Date lastLoginTime; //最后登录失败时间
	private Integer sex; //性别
	private String age; //年龄
	private String email; //邮箱地址
	private String mobilePhone; //手机号码
	private String photo; //用户头像
	private String orgName; //所属单位
	private String dutyName; //岗位职责
	private String expired; //状态1
	private String locked; //状态2
	private String credexpired; //状态3
	private String enable; //状态4
	private String onlineType; //在线形式
	private String remark; //备注
	private Integer attempts=0;	//登录失败次数
	private Date lastChangePwdTime;  //最近修改密码时间
	private Organization org;
	
	private List<Role> roleList = Lists.newArrayList(); // 角色信息
	private Map<String, Object> cacheMap;

	
	
	public User() {

	}

	@Column(nullable = false, unique = true, length = 50, name = "USERNAME")
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
  
	@Column(nullable = false, length = 255, name = "PASSWORD")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "LATESTLOGINTIME")
	public Date getLatestLoginTime() {
		return latestLoginTime;
	}

	public void setLatestLoginTime(Date latestLoginTime) {
		this.latestLoginTime = latestLoginTime;
	}

	@Column(name = "EMAIL")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "MOBILEPHONE")
	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	@Column(name = "REMARK")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "EXPIRED")
	public String getExpired() {
		return expired;
	}

	public void setExpired(String expired) {
		this.expired = expired;
	}

	@Column(name = "LOCKED")
	public String getLocked() {
		return locked;
	}

	public void setLocked(String locked) {
		this.locked = locked;
	}
	
	@Column(name = "CREDEXPIRED")
	public String getCredexpired() {
		return credexpired;
	}
	
	public void setCredexpired(String credexpired) {
		this.credexpired = credexpired;
	}

	@Column(name = "ENABLE")
	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	// 多对多定义
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
	// 中间表定义,表名采用默认命名规则
	@JoinTable(name = "R_APP_USERROLE", joinColumns = {
			@JoinColumn(name = "USERID", referencedColumnName = "id") },
			inverseJoinColumns = { @JoinColumn(name = "ROLEID", referencedColumnName = "id") })
	// Fecth策略定义
	@Fetch(FetchMode.SUBSELECT)
	// 集合按id排序.
	// 集合中对象id的缓存.
	@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@JsonIgnore
	public List<Role> getRoleList() {
		return roleList;
	}
	

	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}
	
	@Transient
	@JsonIgnore
	public List<String> getRoleIdList() {
		List<String> roleIdList = Lists.newArrayList();
		for (Role role : roleList) {
			roleIdList.add(role.getId());
		}
		return roleIdList;
	}

	@Transient
	@JsonIgnore
	public void setRoleIdList(List<String> roleIdList) {
		roleList = Lists.newArrayList();
		for (String roleId : roleIdList) {
			Role role = new Role();
			role.setId(roleId);
			roleList.add(role);
		}
	}
	

	/**
	 * 用户拥有的角色名称字符串, 多个角色名称用','分隔.
	 */
	// 非持久化属性.
	@Transient
	@JsonIgnore
	public String getRoleNames() {
		return ConvertUtils.convertElementPropertyToString(roleList,
				"roleName", ", ");
	}
	@Transient
	@JsonIgnore
	public String getRoleDes() {
		return ConvertUtils.convertElementPropertyToString(roleList,
				"roleDesc", ", ");
	}
	/**
	 * 用户拥有的角色id字符串, 多个角色id用','分隔.
	 */
	// 非持久化属性.
	@Transient
	@SuppressWarnings("unchecked")
	@JsonIgnore
	public List<String> getRoleIds() {
		return ConvertUtils.convertElementPropertyToList(roleList, "id");
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	@Transient
	@Override
	@JsonIgnore
	public Collection<GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
//		if (DateUtils.checkChangePwdTime(getUserAttempts().getLastChangePwdTime())) {
			for (Role role : roleList) {
				for (Sysmenu authority : role.getMenuList()) {
					if(StringUtils.isNotEmpty(authority.getPermission())){
						authList.add(new SimpleGrantedAuthority(authority.getPrefixedName()));
					}
				}
			}
//		}
		return authList;
	}

	@Transient
	@Override
	public boolean isAccountNonExpired() {
	  return "0".equals(expired) ? true : false;
	}

	@Transient
	@Override
	public boolean isAccountNonLocked() {
		return "0".equals(locked) ? true : false;
	}

	@Transient
	@Override
	public boolean isCredentialsNonExpired() {
		return "0".equals(credexpired) ? true : false;
	}

	@Transient
	@Override
	public boolean isEnabled() {
		return "1".equals(enable) ? true : false;
	}
	
	@Column(name = "ONLINETYPE")
	public String getOnlineType() {
		return onlineType;
	}
	public void setOnlineType(String onlineType) {
		this.onlineType = onlineType;
	}
	
	@Column(name = "NAME")
	 public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name="LASTLOGINTIME")
	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	@Column(name="SEX")
	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	@Column(name = "AGE")
	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	@Column(name = "PHOTO")
	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	@Column(name = "ORGNAME")
	public String getOrgName() {
		orgName = org.getOrgName();
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	
	
	@Column(name = "DUTYNAME")
	public String getDutyName() {
		return dutyName;
	}

	public void setDutyName(String dutyName) {
		this.dutyName = dutyName;
	}

	public void setCacheMap(Map<String, Object> cacheMap) {
		this.cacheMap = cacheMap;
	}

	@Override
	public int hashCode() {
	        return username.hashCode();
	  } 
	 @Override
	public boolean equals(Object rhs) {
	        if (rhs instanceof User) {
	            return username.equals(((User) rhs).username);
	        }
	        return false;
	  }
	@Transient
	public boolean isAdmin(){
		if("superAdmin".equals(username)){
			return true;
		}
		return false;
	}
	 @Transient
	 public Map<String, Object> getCacheMap() {
			if (cacheMap==null){
				cacheMap = new HashMap<String, Object>();
			}
			return cacheMap;
		}

	 @Column(name="ATTEMPTS")
	public Integer getAttempts() {
		return attempts;
	}

	public void setAttempts(Integer attempts) {
		this.attempts = attempts;
	}

	@Column(name="LASTCHANGEPWDTIME")
	public Date getLastChangePwdTime() {
		return lastChangePwdTime;
	}

	public void setLastChangePwdTime(Date lastChangePwdTime) {
		this.lastChangePwdTime = lastChangePwdTime;
	}

	@ManyToOne(cascade = { CascadeType.PERSIST }, fetch = FetchType.EAGER)
	@JoinColumn(name = "ORGID")
	@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)	
	public Organization getOrg() {
		return org;
	}

	public void setOrg(Organization org) {
		this.org = org;
	}
	
	
	 
	
}
