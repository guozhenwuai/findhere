package dao;

import java.util.List;

import model.UserTarget;

public interface UserTargetDao {
	public List<String> findUserTargetIDs(String userID);
	public List<String> findAllUserTargetIDs(String userID);
	public UserTarget findOneUserTarget(String userID);
	public void update(UserTarget userTarget);
	public List<String> findAllTempTargetIDs(String userID);
	public List<UserTarget> findHasTempTargetUsers();
	public UserTarget getUserTargetByuserID(String userID);
}
