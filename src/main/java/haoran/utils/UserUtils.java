package haoran.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Maps;
import haoran.entity.app.Code;
import haoran.entity.app.Role;
import haoran.entity.app.Sysmenu;
import haoran.entity.app.User;
import haoran.security.SpringSecurityUtils;
import haoran.service.system.SysmenuManagerService;
import haoran.service.system.SystemManageService;
import haoran.utils.spring.SpringContextHolder;

/**
 * 用户工具类
 */
public class UserUtils {
	
	private static SysmenuManagerService sysmenuManagerService = SpringContextHolder.getBean(SysmenuManagerService.class);
	private static SystemManageService systemManageService = SpringContextHolder.getBean(SystemManageService.class);

	public static final String CACHE_USER = "user";
	public static final String CACHE_NAME = "name";
	public static final String CACHE_MENU_LIST = "menuList";
	public static final String CACHE_ORGNUMBER = "orgNum";
	public static final String CACHE_ORGTYPE = "orgtype";
	public static final String CACHE_ORGNAME = "orgname";
	
	
	public static final String HASSSJ_ROLE="ssjrole";//设施局
	public static final String HASXQJ_ROLE="xqjrole";//辖区局
	public static final String HASADMIN_ROLE="admin";//辖区局
	
	 /**
	  * 判断是有有交委角色
	  * @return
	  */
	public static boolean hasSsjRole(){
		boolean hasRole=false;
		User user =getUser();
		for (int i = 0; i < user.getRoleList().size(); i++) {
			Role role =user.getRoleList().get(i);
			if(HASSSJ_ROLE.equals(role.getRoleName())){
				hasRole=true;
				break;
			}
		}
			return hasRole;
	}
	
	 /**
	  * 判断是否辖区局角色
	  * @return
	  */
	public static boolean hasXqjRole(){
		boolean hasRole=false;
		User user =getUser();
		for (int i = 0; i < user.getRoleList().size(); i++) {
			Role role =user.getRoleList().get(i);
			if(HASXQJ_ROLE.equals(role.getRoleName())){
				hasRole=true;
				break;
			}
		}
			
			return hasRole;
	}
	/**
	 * 判断是否辖区局角色
	 * @return
	 */
	public static boolean hasAdminRole(){
		boolean hasRole=false;
		User user =getUser();
		for (int i = 0; i < user.getRoleList().size(); i++) {
			Role role =user.getRoleList().get(i);
			if(HASADMIN_ROLE.equals(role.getRoleName())){
				hasRole=true;
				break;
			}
		}
		
		return hasRole;
	}
	
	public static String getRole(){
		if (hasAdminRole()) {
			return "3";
		} else if (hasSsjRole()) {
			return "2";
		} else if (hasXqjRole()) {
			return "1";
		} else {
			return "4";
		}
	}
	
	/**
	 * 当前用户
	 * @return
	 */
	public static User getUser(){
		User user = (User)getCache(CACHE_USER);
		if(user == null){
			user = SpringSecurityUtils.getCurrentUser();
			putCache(CACHE_USER, user);
		}
		if(null == user){
			user = new User();
		}
		return user;
	}
	/**
	 * 当前用户姓名
	 * @return
	 */
	public static String getName(){
		String name = (String)getCache(CACHE_NAME);
		if(StringUtils.isEmpty(name)){
			User user = getUser();
			name = user.getName();
			putCache(CACHE_NAME, name);
		}
		return name;
	}
//	/**
//	 * 当前用户所在单位单位编码
//	 * @return
//	 */
//	public static String getOrgNum(){
//		String orgNum = (String)getCache(CACHE_ORGNUMBER);
//		if(StringUtils.isEmpty(orgNum)){
//			User user = getUser();
//			orgNum = user.getEmploye().getOrg().getOrgNum();
//			putCache(CACHE_ORGNUMBER, orgNum);
//		}
//		
//		return orgNum;
//	}
	

//	/**
//	 * 当前用户单位类型
//	 * @return
//	 */
//	public static String getOrgType(){
//		String orgtype = (String)getCache(CACHE_ORGTYPE);
//		if(StringUtils.isEmpty(orgtype)){
//			User user = getUser();
//			orgtype = user.getEmploye().getOrg().getType();
//			putCache(CACHE_ORGTYPE, orgtype);
//		}
//		
//		return orgtype;
//	}
	/**
	 * 当前用户单位名称
	 * @return
	 */
	public static String getOrgName(){
		String orgname = (String)getCache(CACHE_ORGNAME);
		if(StringUtils.isEmpty(orgname)){
			User user = getUser();
			orgname = user.getOrgName();
			putCache(CACHE_ORGNAME, orgname);
		}
		
		return orgname;
	}
	
	/**
	 * 当前用户菜单
	 * @return
	 */
	public static List<Sysmenu> getMenuList2(){
		@SuppressWarnings("unchecked")
		List<Sysmenu> menuList = (List<Sysmenu>)getCache(CACHE_MENU_LIST);
		if(menuList == null){
			User user = getUser();
			if (user.isAdmin()){
				menuList = sysmenuManagerService.getAll();
			}else{
				menuList = sysmenuManagerService.findByUserId(user.getId());
			}
			putCache(CACHE_MENU_LIST, menuList);
		}
		return menuList;
	}
	
	
	/**
	 * 当前用户菜单
	 * @return
	 */
	public static List<Sysmenu> getMenuList(){
		@SuppressWarnings("unchecked")
		List<Sysmenu> menuList = (List<Sysmenu>)getCache(CACHE_MENU_LIST);
		if(menuList == null){
			User user = getUser();
			if (user.isAdmin()){
				menuList = sysmenuManagerService.getAll();
			}else{
				menuList = sysmenuManagerService.findByUserId(user.getId());
			}
			putCache(CACHE_MENU_LIST, menuList);
		}
		return menuList;
	}
	
	/**
	 * 当前国省检菜单
	 * @return
	 */
	public static List<Sysmenu> getCheckMenuList(){
		List<Sysmenu> menuList = new ArrayList<Sysmenu>();
		menuList = sysmenuManagerService.getcheckSysmenus();
		return menuList;
	}
	/**
	 * 获取码表
	 * @param category
	 * @return
	 */
	public static List<Code> getDictList(Integer category){
		List<Code> codes = systemManageService.getCodeListByCategory(category);
		return codes;
	}
	/**
	 * 当前用户message条数
	 * @return
	 */
	public static Integer getMessageCount(){
		return 100;
	}
	/**
	 * 判断用户数据范围
	 * @param user
	 * @param dataScope
	 * @return
	 */
	public static boolean hasDataScope(User user,String dataScope){
		List<Role> roles = user.getRoleList();
		for(Role r:roles){
			if (dataScope.contains(r.getCategory())){
				return true;
			}
		}
		return false;
	}
	// ============== User Cache ==============
	
		public static Object getCache(String key) {
			return getCache(key, null);
		}
		
		public static Object getCache(String key, Object defaultValue) {
			Object obj = getCacheMap().get(key);
			return obj==null?defaultValue:obj;
		}

		public static void putCache(String key, Object value) {
			getCacheMap().put(key, value);
		}

		public static void removeCache(String key) {
			getCacheMap().remove(key);
		}
		
		public static Map<String, Object> getCacheMap(){
			Map<String, Object> map = Maps.newHashMap();
			User user = SpringSecurityUtils.getCurrentUser();
			return user!=null?user.getCacheMap():map;
		}
		
		
		//密码加密
		public static String SHA(final String strText, final String strType)
		  {
		    // 返回值
		    String strResult = null;

		    // 是否是有效字符串
		    if (strText != null && strText.length() > 0)
		    {
		      try
		      {
		        // SHA 加密开始
		        // 创建加密对象 并传入加密类型
		        MessageDigest messageDigest = MessageDigest.getInstance(strType);
		        // 传入要加密的字符串
		        messageDigest.update(strText.getBytes());
		        // 得到 byte 类型结果
		        byte byteBuffer[] = messageDigest.digest();

		        // 將 byte 转换为 string
		        StringBuffer strHexString = new StringBuffer();
		        // 遍历 byte buffer
		        for (int i = 0; i < byteBuffer.length; i++)
		        {
		          String hex = Integer.toHexString(0xff & byteBuffer[i]);
		          if (hex.length() == 1)
		          {
		            strHexString.append('0');
		          }
		          strHexString.append(hex);
		        }
		        // 得到返回結果
		        strResult = strHexString.toString();
		      }
		      catch (NoSuchAlgorithmException e)
		      {
		        e.printStackTrace();
		      }
		    }
		    return strResult;
		  }
}
