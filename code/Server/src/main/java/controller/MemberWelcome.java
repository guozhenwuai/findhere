package controller;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import model.User;
import service.ContentService;
import service.UserService;

@Controller
public class MemberWelcome {
	private static final long serialVersionUID = 1L;
	
	@Resource
	private UserService userService;
	
	@Resource
	private ContentService contentService;
	
	@RequestMapping("/MemberWelcome")
	public String execute(HttpServletRequest request, HttpServletResponse response, HttpSession httpSession)
			throws IOException {
		String userID = (String) httpSession.getAttribute("userID");
		User user = userService.findOneByID(userID);
		httpSession.setAttribute("userName", user.getName());
		
		return "contentManager";
	}
	
	@RequestMapping("/MemberUploadContent")
	public String execute2(HttpServletRequest request, HttpServletResponse response, HttpSession httpSession)
			throws IOException {
		System.out.println(":1");
		String userID = (String) httpSession.getAttribute("userID");
		System.out.println(":2");
		List<String> targetIDs = contentService.getUserTargetIDs(userID);
		System.out.println(":3");
		request.setAttribute("targetIDs", targetIDs);
		return "contentManager-upload";
	}
	
	/*GET and SET*/
	public UserService getUserService(){
		return userService;
	}
	
	public void setUserService(UserService s){
		userService = s;
	}
	
	public ContentService getContentService(){
		return contentService;
	}
	
	public void setContentService(ContentService s){
		contentService = s;
	}
}
