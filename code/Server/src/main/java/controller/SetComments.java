package controller;

import java.io.FileInputStream;
import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import service.CommentService;
import service.ReadService;

@Controller
@RequestMapping("/SetComments")
public class SetComments {
	private static final long serialVersionUID = 1L;
	
	@Resource
	private CommentService commentService;
	
	@Resource
	private ReadService readService;

	@RequestMapping("/text")
	public String execute(HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) 
			throws IOException{
		ServletInputStream inStream = request.getInputStream();
		String commentJson = readService.inputStreamToString(inStream);
		JSONObject jsonObj = new JSONObject(commentJson);
		JSONObject ret = new JSONObject();
		String userID = (String) httpSession.getAttribute("userID");
		if(userID.length() == 0) {
			ret.put("status", "failure");
			response.getOutputStream().print(ret.toString());
			return null;
		}
		
		String commentID = commentService.saveTextComment(jsonObj, userID);
		if(commentID == null || commentID.length() == 0) {
			ret.put("status", "failure");
			response.getOutputStream().print(ret.toString());
			return null;
		}
		
		ret.put("status", "success");
		ret.put("commentID", commentID);
		response.getOutputStream().print(ret.toString());
		return null;
	}
	
	@RequestMapping("/file")
	public String execute2(HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) 
			throws IOException{
		/*String userID = (String) httpSession.getAttribute("userID");
		ServletInputStream inStream = request.getInputStream();
		String commentJson = readService.inputStreamToString(inStream);*/
		FileInputStream fs = new FileInputStream("D:/01.jpg");
		String userID = "1";
		String content = readService.inputStreamToString(fs);
		String commentJson = "{\"file\":\"" + content + "\", \"type\":\"image\", \"targetID\":\"1\"}";
		fs.close();
		System.out.println(commentJson);
		JSONObject jsonObj = new JSONObject(commentJson);
		JSONObject ret = new JSONObject();
		if(userID.length() == 0) {
			ret.put("status", "failure");
			response.getOutputStream().print(ret.toString());
			return null;
		}
		System.out.println(":1");
		String commentID = commentService.saveFileComment(userID, jsonObj);
		System.out.println(":2");
		if(commentID == null || commentID.length() == 0) {
			ret.put("status", "failure");
			response.getOutputStream().print(ret.toString());
			return null;
		}
		
		ret.put("status", "success");
		ret.put("commentID", commentID);
		response.getOutputStream().print(ret.toString());
		return null;
	}
	
	@RequestMapping("/text/update")
	public String textUpdate(HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) 
			throws IOException{
		ServletInputStream inStream = request.getInputStream();
		String commentJson = readService.inputStreamToString(inStream);
		JSONObject jsonObj = new JSONObject(commentJson);
		JSONObject ret = new JSONObject();
		String userID = (String) httpSession.getAttribute("userID");
		if(userID.length() == 0) {
			ret.put("status", "failure");
			response.getOutputStream().print(ret.toString());
			return null;
		}
		
		commentService.updateTextComment(jsonObj, userID);
		ret.put("status", "success");
		response.getOutputStream().print(ret.toString());
		return null;
	}
	
	/*GET and SET*/
	public CommentService getMongoDBService(){
		return commentService;
	}
	
	public void setMongoDBService(CommentService s){
		commentService = s;
	}
	
	public ReadService getReadService() {
		return readService;
	}
	
	public void setReadService(ReadService s) {
		readService = s;
	}
}
