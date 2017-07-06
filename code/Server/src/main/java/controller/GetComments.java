package controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import service.CommentService;

@Controller
public class GetComments {
	private static final long serialVersionUID = 1L;
	
	@Resource
	private CommentService commentService;

	@RequestMapping("/GetComments")
	public String execute(@RequestParam("commentID")String commentID, 
			HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) 
			throws IOException{
		commentService.returnComment(commentID, response);
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
