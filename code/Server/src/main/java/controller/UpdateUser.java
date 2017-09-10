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
public class UpdateUser {
	private static final long serialVersionUID = 1L;
	
	@Resource
	private UserService userService;
	
	@Resource
	private ReadService readService;
	
	@RequestMapping("/UpdateUser")
	public String execute(HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) 
			throws IOException{
		System.out.println(":updateUser");
		System.out.println(":1");
		ServletInputStream inStream = request.getInputStream();
		JSONObject jsonObj = readService.inputStreamReadJson(inStream);
		System.out.println(":2");
		JSONObject ret = new JSONObject();
		String userID = (String) httpSession.getAttribute("userID");
		System.out.println(":3");
		if(userID == null) {
			ret.put("status", "failure");
			response.getOutputStream().print(ret.toString());
			return null;
		}
		System.out.println(":4");
		
		userService.updateUser(userID, jsonObj, inStream);
		System.out.println(":5");
		
		ret.put("status", "success");
		response.getOutputStream().print(ret.toString());
		System.out.println(":6");
		return null;
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
