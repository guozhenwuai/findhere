package controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import service.CommentService;

@Controller
@RequestMapping("/GetComments")
public class GetComments {
	private static final long serialVersionUID = 1L;
	
	@Resource
	private CommentService commentService;

	@RequestMapping("/ByID")
	public String execute1(@RequestParam("commentID")String commentID,  
			HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) 
			throws IOException{
		commentService.returnCommentByID(commentID, response);
		return null;
	}
	
	@RequestMapping("/GetIDsByTargetID")
	public String execute2(@RequestParam("targetID")String targetID, 
			@RequestParam("pageNum")int pageNum,
			@RequestParam("pageIndex")int pageIndex,
			HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) 
			throws IOException{
		commentService.returnCommentIDsByTargetID(targetID, pageNum, pageIndex, response);
		return null;
	}
	
	@RequestMapping("/GetIDsByUserID")
	public String execute3(@RequestParam("userID")String userID, 
			@RequestParam("pageIndex")int pageIndex,
			HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) 
			throws IOException{
		commentService.returnCommentIDsByUserID(userID, pageIndex, response);
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
