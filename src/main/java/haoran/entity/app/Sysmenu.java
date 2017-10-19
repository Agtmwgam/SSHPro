/**
 * <p>Copyright (c) 2012 深圳市鹏途信息技术有限公司 </p>
 * <p>				   All right reserved. 		     </p>
 * 
 * <p>项目名称 ： 	深圳道路检测管理系统        </p>
 * <p>创建者   :	iibm 
 * 
 * <p>描   述  :   Resource.java for com.pengtu.entity.ss    </p>
 * 
 * <p>最后修改 : $: 2012-10-24-下午4:52:51 v 1.0.0	 iibm   $     </p>
 * 
*/

package haoran.entity.app;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Lists;
import haoran.config.Constants;
import haoran.entity.base.IdEntity;

@Entity
@Table(name = "T_APP_SYSMENU")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class Sysmenu extends IdEntity{
	/**
	 * 菜单
	 */
	private static final long serialVersionUID = 1L;
	private Sysmenu parent;	// 父级菜单
	private String parentIds; // 所有父级编号
	private String name; 	// 名称
	private String href; 	// 链接
	private String checkHref; //国省检地址
	private String target; 	// 目标（ mainFrame、_blank、_self、_parent、_top）
	private String icon; 	// 图标
	private Integer sort; 	// 排序
	private String isShow; 	// 是否在菜单中显示（1：显示；0：不显示）
	private String permission; // 权限标识
	private String remark; // 备注
	private String tag;		//标签
	private String system;  //系统菜单还是日常监管菜单  0 系统菜单 1 日常监管菜单
	private String menuType; //菜单类型  2000 0 业务模块菜单  1  文件数据菜单
	
	private List<Sysmenu> childList = Lists.newArrayList();// 拥有子菜单列表
	
	
	public Sysmenu(){
		
	}
	
	public Sysmenu(String id, String name){
		this.id = id;
		this.name = name;
	}
	
	@ManyToOne(targetEntity = Sysmenu.class, 
			cascade = { CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@JoinColumn(name = "PARENTID")
	@JsonIgnore 
	public Sysmenu getParent() {
		return parent;
	}
	
	public void setParent(Sysmenu parent) {
		this.parent = parent;
	}
	@Column(name = "PARENTIDS")
	public String getParentIds() {
		return parentIds;
	}
	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}
	@Column(name = "NAME")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(name = "HREF")
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	@Column(name = "TARGET")
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	@Column(name = "ICON")
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	@Column(name = "SORT")
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
	@Column(name = "PERMISSION")
	public String getPermission() {
		return permission;
	}
	public void setPermission(String permission) {
		this.permission = permission;
	}
	
	@Column(name = "REMARK")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Column(name="TAG")
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	
	@Column(name = "SYSTEM")
	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}
	
	@Column(name = "MENUTYPE")
	public String getMenuType() {
		return menuType;
	}

	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}
	
	
	@Column(name = "CHECKHREF")
	public String getCheckHref() {
		return checkHref;
	}

	public void setCheckHref(String checkHref) {
		this.checkHref = checkHref;
	}

	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
	@Where(clause = " DELFLAG = '0' ")
	@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@JsonIgnore 
	public List<Sysmenu> getChildList() {
		return childList;
	}
	public void setChildList(List<Sysmenu> childList) {
		this.childList = childList;
	}
	@Transient
	public static void sortList(List<Sysmenu> list, List<Sysmenu> sourcelist, String parentId){
		for (int i=0; i<sourcelist.size(); i++){
			Sysmenu e = sourcelist.get(i);
				if (e.getParent()!=null && e.getParent().getId()!=null
						&& e.getParent().getId().equals(parentId)){
					list.add(e);
					// 判断是否还有子节点, 有则继续获取子节点
					for (int j=0; j<sourcelist.size(); j++){
						Sysmenu child = sourcelist.get(j);
						if (child.getParent()!=null && child.getParent().getId()!=null
								&& child.getParent().getId().equals(e.getId())){
							sortList(list, sourcelist, e.getId());
							break;
						}
					}
				}
		}
			
	}
	/**
	 * 
	 * @return
	 */
	@Transient
	public String getPrefixedName() {
		return Constants.AUTHORITY_PREFIX + permission;
	}
	
	@Transient
	public static List<Sysmenu> sort(List<Sysmenu> list) {
		Collections.sort(list, new Comparator<Sysmenu>() {
			@Override
			public int compare(Sysmenu o1, Sysmenu o2) {
				if (o1.getSort() > o2.getSort()) {
					return 1;
				}
				if (o1.getSort().equals(o2.getSort())) {
					return 0;
				}
				return -1;
			}
			
		});
		Iterator<Sysmenu> iterator = list.iterator();
		while (iterator.hasNext()) {
			Sysmenu sysmenu = (Sysmenu) iterator.next();
			if ("0".equals(sysmenu.getIsShow())) {
				iterator.remove();
			}
		}
		return list;
	}
}
