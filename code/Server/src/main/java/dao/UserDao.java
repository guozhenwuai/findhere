package dao;

import model.User;

public interface UserDao {
	public User findOneByID(String id);
	public String insert(User user);
	public void update(User user);
}
