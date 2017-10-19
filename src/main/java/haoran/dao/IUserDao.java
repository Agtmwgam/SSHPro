package haoran.dao;

import java.io.Serializable;

import haoran.entity.Userinfo;


public interface IUserDao {
	public Serializable savedao(Userinfo u);
}
