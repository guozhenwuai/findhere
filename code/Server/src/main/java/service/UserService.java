package service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import model.User;

public interface UserService {
	public boolean login(JSONObject jsonObj, HttpServletResponse response, HttpSession httpSession) throws IOException;
	public int webLogin(String userID, String password);
	public int SignUp(JSONObject jsonObj, HttpSession httpSession);
	public User findOneByID(String userID);
	public void addHeadPortrait(String userID, HttpServletRequest request) throws IOException;
}
