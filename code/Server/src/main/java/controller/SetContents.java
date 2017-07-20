package controller;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import service.ContentService;

@Controller
@RequestMapping("/SetContents")
public class SetContents {
	private static final long serialVersionUID = 1L;
	
	@Resource
	private ContentService contentService;
	
	@RequestMapping("/Object")
	public String execute2(@RequestParam("targetID") String targetID,
			MultipartHttpServletRequest multiRequest,
			HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) 
			throws IOException{
		InputStream objectInStream = multiRequest.getFile("objectFile").getInputStream();
		InputStream textureInStream = multiRequest.getFile("textureFile").getInputStream();
		InputStream MTLInStream = multiRequest.getFile("MTLFile").getInputStream();
		contentService.addARObject(targetID, objectInStream, textureInStream, MTLInStream);
		return null;
	}
	
	@RequestMapping("/addTarget")
	public String execute3(@RequestParam("newTarget") MultipartFile newTarget,
			HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) 
			throws IOException{
		String userID = (String) httpSession.getAttribute("userID");
		InputStream inStream = newTarget.getInputStream();
		contentService.addTarget(userID, inStream);
		return "redirect:/MemberVerifyTarget?pageIndex=0";
	}
	
	/*GET and SET*/
	public ContentService getMongoDBService(){
		return contentService;
	}
	
	public void setMongoDBService(ContentService s){
		contentService = s;
	}
}
