package service;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

public interface UserService {
	public boolean login(JSONObject jsonObj, HttpServletResponse response, HttpSession httpSession) throws IOException;
	public boolean webLogin(String userID, String password);
	public int SignUp(JSONObject jsonObj, HttpSession httpSession);
}
