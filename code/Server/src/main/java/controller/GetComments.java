package controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import service.MongoDBService;

@Controller
public class GetComments {
	private static final long serialVersionUID = 1L;
	private MongoDBService mongoDBService;
	
	@RequestMapping("/GetComments")
	public String execute(@RequestParam("commentID")String commentID, HttpServletRequest request, HttpServletResponse response){
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
