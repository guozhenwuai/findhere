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
		System.out.println(jsonString);
		JSONObject jsonObj = new JSONObject(jsonString);
		boolean success = userService.login(jsonObj, httpSession);
		if(success) {
			response.getOutputStream().print("true");
			System.out.println("true");
		}else {
			response.getOutputStream().print("false");
			System.out.println("false");
		}
		System.out.println(httpSession.getId());
		System.out.println("one login");
		return null;
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
