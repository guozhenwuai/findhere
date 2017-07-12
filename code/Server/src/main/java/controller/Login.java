package controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import service.ReadService;
import service.UserService;

@Controller
public class Login {
	private static final long serialVersionUID = 1L;
	
	@Resource
	private UserService userService;
	
	@Resource
	private ReadService readService;
	
	@RequestMapping("/Login")
	public String execute(HttpServletRequest request, HttpServletResponse response, HttpSession httpSession)
			throws IOException {
		ServletInputStream inStream = request.getInputStream();
		String jsonString = readService.inputStreamToString(inStream);
		JSONObject jsonObj = new JSONObject(jsonString);
		JSONObject ret = new JSONObject(jsonString);
		userService.login(jsonObj, response, httpSession);
		return null;
	}
	
	@RequestMapping("/webLogin")
	public String execute2(HttpServletRequest request, HttpServletResponse response, HttpSession httpSession)
			throws IOException {
		String userID = request.getParameter("userID");
		String password = request.getParameter("password");
		boolean success = userService.webLogin(userID, password);
		if(success) {
			httpSession.setAttribute("isAdmin", "true");
		}
		return "redirect:/ConfirmMember?pageIndex=0";
	}
	
	@RequestMapping("/LogOut")
	public String execute3(HttpServletRequest request, HttpServletResponse response, HttpSession httpSession)
			throws IOException {
		httpSession.invalidate();
		return "login";
	}
	
	/*GET and SET*/
	public UserService getMongoDBService(){
		return userService;
	}
	
	public void setMongoDBService(UserService s){
		userService = s;
	}
	
	public ReadService getReadService() {
		return readService;
	}
	
	public void setReadService(ReadService s) {
		readService = s;
	}
}
