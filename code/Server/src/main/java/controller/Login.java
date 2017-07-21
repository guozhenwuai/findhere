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
		System.out.println("perparing login");
		ServletInputStream inStream = request.getInputStream();
		String jsonString = readService.inputStreamToString(inStream);
		JSONObject jsonObj = new JSONObject(jsonString);
		JSONObject ret = new JSONObject(jsonString);
		userService.login(jsonObj, response, httpSession);
		System.out.println(httpSession.getId());
		return null;
	}
	
	@RequestMapping("/webLogin")
	public String execute2(HttpServletRequest request, HttpServletResponse response, HttpSession httpSession)
			throws IOException {
		String userID = request.getParameter("userID");
		String password = request.getParameter("password");
		int result = userService.webLogin(userID, password);
		
		if(result == 1) {//Administrator
			httpSession.setAttribute("isAdmin", "true");
			return "redirect:/ConfirmMember?pageIndex=0";
		}else if(result == 2) {//Member
			httpSession.setAttribute("isMember", "true");
			httpSession.setAttribute("userID", userID);
			return "redirect:/MemberWelcome";
		}else if(result == 3) {//ordinary user
			httpSession.setAttribute("userID", userID);
			return "register";
		}
		return "login";
	}
	
	@RequestMapping("/LogOut")
	public String execute3(HttpServletRequest request, HttpServletResponse response, HttpSession httpSession)
			throws IOException {
		httpSession.invalidate();
		return "login";
	}
	
	/*GET and SET*/
	public UserService getUserService(){
		return userService;
	}
	
	public void setUserService(UserService s){
		userService = s;
	}
	
	public ReadService getReadService() {
		return readService;
	}
	
	public void setReadService(ReadService s) {
		readService = s;
	}
}
