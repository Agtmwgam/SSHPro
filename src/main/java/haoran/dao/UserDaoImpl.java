package haoran.dao;



import haoran.entity.Userinfo;

import java.io.Serializable;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository("dao")
public class UserDaoImpl implements IUserDao{
	
	private SessionFactory sessionfactory;
	@Autowired
	public void setSessionfactory(SessionFactory sessionfactory) {
		this.sessionfactory = sessionfactory;
	}

	@Override
	public Serializable savedao(Userinfo u) {
		return sessionfactory.getCurrentSession().save(u);
	}

}
