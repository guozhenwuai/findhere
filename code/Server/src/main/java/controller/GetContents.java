package controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import service.ContentService;

@Controller
@RequestMapping("/GetContents")
public class GetContents {
	private static final long serialVersionUID = 1L;
	
	@Resource
	private ContentService contentService;
	
	@RequestMapping("/ByID")
	public String execute1(@RequestParam("contentID")String contentID, @RequestParam("type")String type,
			HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) 
			throws IOException{
		contentService.returnContentByID(contentID, type, response);
		return null;
	}
	
	@RequestMapping("/GetIDsByTargetID")
	public String execute2(@RequestParam("targetID")String targetID, 
			HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) 
			throws IOException{
		contentService.returnContentIDsBytargetID(targetID, response);
		return null;
	}
	
	/*GET and SET*/
	public ContentService getContentService(){
		return contentService;
	}
	
	public void setContentService(ContentService s){
		contentService = s;
	}
}
