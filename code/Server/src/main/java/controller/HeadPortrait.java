package controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import service.UserService;

@Controller
@RequestMapping("/HeadPortrait")
public class HeadPortrait {
	private static final long serialVersionUID = 1L;
	
	@Resource
	private UserService userService;
	
	@RequestMapping("/add")
	public String execute1(HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) 
			throws IOException{
		String userID = (String) httpSession.getAttribute("userID");
		userService.addHeadPortrait(userID, request);
		response.getOutputStream().print("OK");
		return null;
	}
	
	/*GET and SET*/
	public UserService getUserService(){
		return userService;
	}
	
	public void setUserService(UserService s){
		userService = s;
	}
}
