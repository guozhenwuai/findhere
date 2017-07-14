package service.Impl;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import dao.FileDao;
import dao.UserDao;
import model.User;
import service.UserService;

public class UserServiceImpl implements UserService {
	
	@Resource
	private UserDao userDao;
	
	@Resource
	private FileDao fileDao;
	
	public boolean login(JSONObject jsonObj, HttpServletResponse response, HttpSession httpSession)
			throws IOException {
		System.out.println(":1.2");
		JSONObject ret = new JSONObject();
		ServletOutputStream outStream = response.getOutputStream();
		String userID = jsonObj.getString("email");
		String password = jsonObj.getString("password");
		System.out.println(":1.3");
		User user = userDao.findOneByID(userID);
		if(user == null) {
			ret.put("status", "failure");
			outStream.print(ret.toString());
			return false; //No user match
		}
		else if(!user.getPassword().equals(password)) {
			ret.put("status", "failure");
			outStream.print(ret.toString());
			return false;
		}
		
		System.out.println(":1.4");
		//success
		httpSession.setAttribute("userID", userID);
		ret.put("status", "success");
		ret.put("userName", user.getName());
		ret.put("gender", user.getGender());
		ret.put("isMember", user.getIsMember());
		outStream.print(ret.toString());
		
		System.out.println(":1.5");
		
		//headPortrait
		if(user.getHeadPortraitID() != null) {
			fileDao.outputFileToStream("headPortrait", user.getHeadPortraitID(), outStream);
		}
		return false;
	}
	
	public int webLogin(String userID, String password) {
		User user = userDao.findOneByID(userID);
		if(user == null) return 0; //No user match
		if(!user.getPassword().equals(password)) return 0;
		
		if(user.getAdmin() == 1) return 1;//Administrator
		else if(user.getIsMember() == 1 )return 2;//Member
		return 3;//ordinary user
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
	
	public void addHeadPortrait(String userID, HttpServletRequest request) throws IOException {
		String headPortraitID = fileDao.inputFileToDB("headPortrait", request.getInputStream());
		User user = userDao.findOneByID(userID);
		fileDao.removeFile("headPortrait", user.getHeadPortraitID());
		user.setHeadPortraitID(headPortraitID);
		userDao.update(user);
	}
	
	public User findOneByID(String userID) {
		return userDao.findOneByID(userID);
	}
	
	/*GET and SET*/
	public UserDao getUserDao() {
		return userDao;
	}
	
	public void setUserDao(UserDao dao) {
		userDao = dao;
	}
	
	public FileDao getFileDao() {
		return fileDao;
	}
	
	public void setFileDao(FileDao dao) {
		fileDao = dao;
	}
}
