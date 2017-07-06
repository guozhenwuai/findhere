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
		commentService.saveTextComment(jsonObj);
		response.getWriter().println("OK");
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
