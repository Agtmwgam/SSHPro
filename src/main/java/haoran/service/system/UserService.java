package haoran.service.system;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import haoran.dao.base.Page;
import haoran.dao.base.QueryFilter;
import haoran.entity.app.User;

/** 
 * @author zl 
 * @version 创建时间：2017年2月5日 下午5:10:54 
 * 类说明 
 */
public interface UserService {

	public User getUserByUsername(String username);
	
	public User getUserByName(String username);
	
	public void saveUser(User user);
	
	public User getUserById(String id);
	
	public void deleteUser(String id);
	
	public User findUserByEmail(String email);
	
	public String getNameByUsername(String username);
	
	public List<User> getUsers();
	
	public List<User> findUsers(QueryFilter filter);
	
	public Page<User> findUsers(Page<User> page, QueryFilter filter);
	
	public boolean isLoginNameUnique(String newLoginName, String oldLoginName);
	
	public boolean isregisterEmailUnique(String newEmail, String oldEmail);
	
	public Collection<GrantedAuthority> loadUserAuthoritiesByName(String username);
	
	public String getPasswordByUsername(String username);
	
	public List<User> getUserByRoleName(String roleName);
	
	public void merge(User user);
}
