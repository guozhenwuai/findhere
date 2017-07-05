package dao;

import model.User;

public interface UserDao {
	public User findOneByID(String id);
}
