package controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import service.CommentService;

@Controller
@RequestMapping("/DeleteComments")
public class DeleteComments {
	private static final long serialVersionUID = 1L;
	
	@Resource
	private CommentService commentService;
	
	@RequestMapping("/ByID")
	public String execute(@RequestParam("commentID")String commentID, 
			HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) 
			throws IOException{
		JSONObject ret = new JSONObject();
		String userID = (String) httpSession.getAttribute("userID");
		if(userID == null) {
			ret.put("status", "failure");
			response.getOutputStream().print(ret.toString());
			return null;
		}
		
		if(!commentService.deleteComment(commentID, userID)) {
			ret.put("status", "failure");
			response.getOutputStream().print(ret.toString());
			return null;
		}
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
}
