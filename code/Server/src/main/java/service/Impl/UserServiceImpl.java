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
		if(user == null) return false; //No user match
		if(!user.getPassword().equals(password)) return false;
		
		//success
		httpSession.setAttribute("userID", userID);
		return true;
	}
	
	public int SignUp(JSONObject jsonObj, HttpSession httpSession) {
		String userID = jsonObj.getString("email");
		String password = jsonObj.getString("password");
		String name = jsonObj.getString("userName");
		String gender = jsonObj.getString("gender");
		
		if( !gender.equals("male") && !gender.equals("female") && gender != null ) return 2;
		User userOld = userDao.findOneByID(userID);
		if(userOld != null) return 1;
		
		User user = new User();
		user.setUserID(userID);
		user.setPassword(password);
		user.setName(name);
		user.setGender(gender);
		userDao.insert(user);
		return 0;
	}
	
	/*GET and SET*/
	public UserDao getUserDao() {
		return userDao;
	}
	
	public void setUserDao(UserDao dao) {
		userDao = dao;
	}
}
