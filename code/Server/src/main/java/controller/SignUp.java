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

import service.ApplyService;
import service.ReadService;
import service.UserService;

@Controller
public class SignUp {
	private static final long serialVersionUID = 1L;
	
	@Resource
	private UserService userService;
	
	@Resource
	private ReadService readService;
	
	@Resource
	private ApplyService applyService;
	
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
	
	@RequestMapping("/Apply")
	public String execute1(HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) 
			throws IOException {
		return "register";
	}
	
	@RequestMapping("/webSignUp")
	public String execute2(HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) 
			throws IOException {
		String realName = request.getParameter("realName");
		String nationalID = request.getParameter("nationalID");
		String incorporation = request.getParameter("incorporation");
		String userID = (String)httpSession.getAttribute("userID");
		applyService.addApply(userID, realName, nationalID, incorporation);
		return "waitForVerifying";
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
	
	public ApplyService getApplyService() {
		return applyService;
	}
	
	public void setApplyService(ApplyService s) {
		this.applyService = s;
	}
}
