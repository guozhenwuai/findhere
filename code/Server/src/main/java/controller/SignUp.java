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
public class SignUp {
	private static final long serialVersionUID = 1L;
	
	@Resource
	private UserService userService;
	
	@Resource
	private ReadService readService;
	
	@RequestMapping("/SignUp")
	public String execute(HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) 
			throws IOException {
		ServletInputStream inStream = request.getInputStream();
		String jsonString = readService.inputStreamToString(inStream);
		JSONObject jsonObj = new JSONObject(jsonString);
		JSONObject ret = new JSONObject();
		int result = userService.SignUp(jsonObj, httpSession);
		switch(result) {
		case(0):
			ret.put("status", "success");
			response.getWriter().print(ret.toString());
			httpSession.setAttribute("userID", jsonObj.get("email"));
			break;
		case(1):
			ret.put("status", "duplicativeUserID");
			response.getWriter().print(ret.toString());
			break;
		case(2):
			ret.put("status", "unknowGender");
			response.getWriter().print(ret.toString());
			break;
		}
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
