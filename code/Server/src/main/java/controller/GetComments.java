package controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import service.MongoDBService;

@Controller
public class GetComments {
	private static final long serialVersionUID = 1L;
	
	@Resource
	private MongoDBService mongoDBService;
	
	@RequestMapping("/GetComments")
	public String execute(@RequestParam("commentID")String commentID, HttpServletRequest request, HttpServletResponse response) 
			throws IOException{
		String text = mongoDBService.getCommentText(commentID);
		ServletOutputStream outStream = response.getOutputStream();
		outStream.print(text);
		outStream.close();
		return null;
	}
	
	/*GET and SET*/
	public MongoDBService getMongoDBService(){
		return mongoDBService;
	}
	
	public void setMongoDBService(MongoDBService ms){
		mongoDBService = ms;
	}
}
