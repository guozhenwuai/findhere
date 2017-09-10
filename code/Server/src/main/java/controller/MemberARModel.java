package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import service.ContentService;
import service.UserService;
import tool.Wrapper;

@Controller
@RequestMapping("/MemberARModel")
public class MemberARModel {
	private static final long serialVersionUID = 1L;
	
	@Resource
	private UserService userService;
	
	@Resource
	private ContentService contentService;
	
	@RequestMapping("/View")
	public String execute(@RequestParam("pageIndex")int pageIndex,
			HttpServletRequest request, HttpServletResponse response, HttpSession httpSession)
			throws IOException {
		int pageNum = 12;
		String userID = (String) httpSession.getAttribute("userID");
		List<Wrapper> modelIDAndTargetRaw = contentService.getmodelsByUserID(userID);
		List<Wrapper> modelIDAndTarget = new ArrayList<Wrapper>();
		for(int i = pageIndex*pageNum; i<modelIDAndTargetRaw.size() && i<pageIndex*pageNum + 12; i++) {
			modelIDAndTarget.add(modelIDAndTargetRaw.get(i));
		}
		request.setAttribute("modelIDAndTarget", modelIDAndTarget);
		request.setAttribute("pageIndex", pageIndex);
		return "contentManager-model";
	}
	
	@RequestMapping("/deleteModel")
	public String execute2(@RequestParam("contentID")String contentID, @RequestParam("pageIndex")int pageIndex,
			HttpServletRequest request, HttpServletResponse response, HttpSession httpSession)
			throws IOException {
		contentService.deleteContent(contentID);
		return "redirect:/MemberARModel/View?pageIndex="+pageIndex;
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
