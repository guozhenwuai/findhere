package service.Impl;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import dao.UserDao;
import model.User;
import service.UserService;

public class UserServiceImpl implements UserService {
	
	@Resource
	private UserDao userDao;
	
	public boolean login(JSONObject jsonObj, HttpSession httpSession) {
		String userID = jsonObj.getString("email");
		String password = jsonObj.getString("password");
		User user = userDao.findOneByID(userID);
		if(user.getUserID() == null) return false; //No user match
		if(user.getPassword() != password) return false;
		
		//success
		httpSession.setAttribute("userID", userID);
		return true;
	}
	
	/*GET and SET*/
	public UserDao getUserDao() {
		return userDao;
	}
	
	public void setUserDao(UserDao dao) {
		userDao = dao;
	}
}
