package service;

import javax.servlet.http.HttpSession;

import org.json.JSONObject;

public interface UserService {
	public boolean login(JSONObject jsonObj, HttpSession httpSession);
	public int SignUp(JSONObject jsonObj, HttpSession httpSession);
}
