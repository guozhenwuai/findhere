package controller;

import java.io.IOException;
import java.io.InputStream;
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
import tool.Wrapper;

@Controller
@RequestMapping("/VerifyTarget")
public class VerifyTarget {
	private static final long serialVersionUID = 1L;
	
	@Resource
	private ContentService contentService;
	
	@RequestMapping("/Verify")
	public String execute(@RequestParam("pageIndex")int pageIndex,
			HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) 
			throws IOException{
		int pageNum = 12;
		List<Wrapper> allTempTarget = contentService.getAllTempTarget();
		List<Wrapper> tempTargetIDs = new ArrayList<Wrapper>();
		
		for(int i = pageNum*pageIndex; (i < allTempTarget.size()) && (i < pageNum*pageIndex + 12); i++) {
			tempTargetIDs.add(allTempTarget.get(i));
		}
		request.setAttribute("targetIDs", tempTargetIDs);
		request.setAttribute("pageIndex", pageIndex);
		return "verifyTarget";
	}
	
	@RequestMapping("/RatifyTarget")
	public String execute2(@RequestParam("userID") String userID,
			@RequestParam("tempTargetID") String tempTargetID,
			HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) 
			throws IOException{
		contentService.ratifyTarget(userID, tempTargetID);
		return null;
	}
	
	/*GET and SET*/
	public ContentService getMongoDBService(){
		return contentService;
	}
	
	public void setMongoDBService(ContentService s){
		contentService = s;
	}
}
