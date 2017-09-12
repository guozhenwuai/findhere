package controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
	
	@RequestMapping("/MemberContent")
	public String execute2(HttpServletRequest request, HttpServletResponse response, HttpSession httpSession)
			throws IOException {
		System.out.println(":1");
		String userID = (String) httpSession.getAttribute("userID");
		System.out.println(":2");
		List<String> targetIDs = contentService.getUserTargetIDs(userID);
		if(targetIDs == null) {
			targetIDs = new ArrayList<String>();
		}
		System.out.println(":3");
		request.setAttribute("targetIDs", targetIDs);
		return "contentManager-object";
	}
	
	@RequestMapping("/MemberTarget")
	public String execute3(@RequestParam("pageIndex")int pageIndex,
			HttpServletRequest request, HttpServletResponse response, HttpSession httpSession)
			throws IOException {
		int pageNum = 12;
		String userID = (String) httpSession.getAttribute("userID");
		List<String> allTargetIDs = contentService.getUserTargetIDs(userID);
		if(allTargetIDs == null) {
			allTargetIDs = new ArrayList<String>();
		}
		List<String> targetIDs = new ArrayList<String>();
		
		for(int i = pageNum*pageIndex; (i < allTargetIDs.size()) && (i < pageNum*pageIndex + 12); i++) {
			targetIDs.add(allTargetIDs.get(i));
		}
		request.setAttribute("targetIDs", targetIDs);
		request.setAttribute("pageIndex", pageIndex);
		return "contentManager-target";
	}
	
	@RequestMapping("/MemberVerifyTarget")
	public String execute4(@RequestParam("pageIndex")int pageIndex,
			HttpServletRequest request, HttpServletResponse response, HttpSession httpSession)
			throws IOException {
		int pageNum = 12;
		String userID = (String) httpSession.getAttribute("userID");
		List<String> allTempTargetIDs = contentService.getTempTargetIDsByUserID(userID);
		if(allTempTargetIDs == null) {
			allTempTargetIDs = new ArrayList<String>();
		}
		List<String> tempTargetIDs = new ArrayList<String>();
		
		for(int i = pageNum*pageIndex; (i < allTempTargetIDs.size()) && (i < pageNum*pageIndex + 12); i++) {
			tempTargetIDs.add(allTempTargetIDs.get(i));
		}
		request.setAttribute("targetIDs", tempTargetIDs);
		request.setAttribute("pageIndex", pageIndex);
		return "contentManager-verifying";
	}
	
	@RequestMapping("/MemberTarget/DeleteTarget")
	public String execute5(@RequestParam("targetID")String targetID, @RequestParam("pageIndex")int pageIndex,
			HttpServletRequest request, HttpServletResponse response, HttpSession httpSession)
			throws IOException, URISyntaxException {
		String userID = (String) httpSession.getAttribute("userID");
		contentService.deleteTarget(userID, targetID);
		return "redirect:/MemberTarget?pageIndex="+pageIndex;
	}
	
	@RequestMapping("/MemberTarget/deleteTempTarget")
	public String execute6(@RequestParam("tempTargetID")String tempTargetID, @RequestParam("pageIndex")int pageIndex,
			HttpServletRequest request, HttpServletResponse response, HttpSession httpSession)
			throws IOException {
		String userID = (String) httpSession.getAttribute("userID");
		contentService.deleteTempTarget(userID, tempTargetID);
		return "redirect:/MemberVerifyTarget?pageIndex="+pageIndex;
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
