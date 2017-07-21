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
@RequestMapping("/GetContent")
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
	
	@RequestMapping("/ByTargetID")
	public String execute2(@RequestParam("targetID")String targetID, 
			HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) 
			throws IOException{
		contentService.returnContentIDsBytargetID(targetID, response);
		return null;
	}
	
	@RequestMapping("/Object/ByID")
	public String execute3(@RequestParam("ARManagerID")String ARManagerID,
			HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) 
			throws IOException{
		contentService.returnContentObjectByARID(ARManagerID, response);
		return null;
	}
	
	@RequestMapping("/MTL")
	public String execute4(@RequestParam("ARManagerID")String ARManagerID, @RequestParam("name")String name,
			HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) 
			throws IOException{
		System.out.println(":1");
		contentService.returnContentMTL(ARManagerID, name, response);
		System.out.println(":2");
		return null;
	}
	
	@RequestMapping("/Texture")
	public String execute5(@RequestParam("ARManagerID")String ARManagerID, @RequestParam("name")String name,
			HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) 
			throws IOException{
		contentService.returnContentTexture(ARManagerID, name, response);
		return null;
	}
	
	@RequestMapping("/Options")
	public String execute6(@RequestParam("ARManagerID")String ARManagerID,
			HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) 
			throws IOException{
		contentService.returnContentObjectPosition(ARManagerID, response);
		return null;
	}
	
	@RequestMapping("/GetTarget")
	public String execute7(@RequestParam("targetID")String targetID,
			HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) 
			throws IOException{
		contentService.getTarget(targetID, response);
		return null;
	}
	
	@RequestMapping("/GetTempTarget")
	public String execute8(@RequestParam("tempTargetID")String tempTargetID,
			HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) 
			throws IOException{
		contentService.getTempTarget(tempTargetID, response);
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
