package haoran.service;



import haoran.dao.IUserDao;
import haoran.entity.Userinfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service("userService")
@Transactional
public class UserServiceImpl implements IUserService {
	private IUserDao dao;
	@Autowired
	public void setDao(IUserDao dao) {
		this.dao = dao;
	}
	@Override
	public void saveUser(Userinfo u) {
		 dao.savedao(u);
	}

}
