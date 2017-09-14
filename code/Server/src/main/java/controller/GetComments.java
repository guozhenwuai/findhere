package controller;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import service.CommentService;
import service.ReadService;

@Controller
@RequestMapping("/GetComments")
public class GetComments {
	private static final long serialVersionUID = 1L;
	
	@Resource
	private CommentService commentService;
	
	@Resource
	private ReadService readService;

	@RequestMapping("/Test")
	public String execute0(HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) 
			throws IOException{
		File file = new File("D:/star.jpg");
		BufferedImage buf = ImageIO.read(file);
		//Image image = buf.getScaledInstance(256, 256, Image.SCALE_DEFAULT);
		
		BufferedImage img = new BufferedImage(256, 256, BufferedImage.TYPE_INT_RGB);
		//img.getGraphics().drawImage(buf, 256, 256, null);
		img.getGraphics().drawImage(buf.getScaledInstance(256, 256, Image.SCALE_SMOOTH), 0, 0, null);
		
		FileOutputStream out = new FileOutputStream("D:/ss.png");
		ImageIO.write(img, "PNG", out);
		img.getGraphics().dispose();
		out.close();
		return null;
	}
	
	@RequestMapping("/ByID")
	public String execute1(@RequestParam("commentID")String commentID,  
			HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) 
			throws IOException{
		System.out.println("ByID");
		commentService.returnCommentByID(commentID, response);
		return null;
	}
	
	@RequestMapping("/GetIDsByTargetID")
	public String execute2(@RequestParam("targetID")String targetID, 
			@RequestParam("pageNum")int pageNum,
			@RequestParam("pageIndex")int pageIndex,
			HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) 
			throws IOException{
		System.out.println("ByTargetID");
		commentService.returnCommentIDsByTargetID(targetID, pageNum, pageIndex, response);
		return null;
	}
	
	@RequestMapping("/GetAllIDsByTargetID")
	public String execute2(@RequestParam("targetID")String targetID, 
			HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) 
			throws IOException{
		System.out.println("ByTargetID");
		commentService.returnAllCommentIDsByTargetID(targetID, response);
		return null;
	}
	
	@RequestMapping("/GetIDsByUserID")
	public String execute3(HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) 
			throws IOException{
		ServletInputStream inStream = request.getInputStream();
		String commentJson = readService.inputStreamToString(inStream);
		JSONObject jsonObj = new JSONObject(commentJson);
		String userID = jsonObj.getString("userID");
		int pageIndex = jsonObj.getInt("pageIndex");
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
	
	public ReadService getReadService() {
		return readService;
	}
	
	public void setReadService(ReadService s) {
		readService = s;
	}
}
