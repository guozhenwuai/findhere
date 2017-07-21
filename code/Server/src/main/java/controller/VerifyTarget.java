package controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import model.User;
import service.ContentService;
import service.VuforiaService;
import tool.Wrapper;

@Controller
@RequestMapping("/VerifyTarget")
public class VerifyTarget {
	private static final long serialVersionUID = 1L;
	
	@Resource
	private ContentService contentService;
	
	@Resource
	private VuforiaService vuforiaService;
	
	@RequestMapping("/Test")
	public String executeT(HttpServletRequest request, HttpServletResponse response, HttpSession httpSession)
			throws IOException, URISyntaxException {
		String s = "f29837c000614152ad7db17e552d855e";
		vuforiaService.getTarget(s);
		return null;
	}
	
	@RequestMapping("/Verify")
	public String execute(@RequestParam("pageIndex")int pageIndex,
			HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) 
			throws IOException{
		int pageNum = 12;
		List<Wrapper> allTempTarget = contentService.getAllTempTarget();
		List<Wrapper> tempTargetIDs = new ArrayList<Wrapper>();
		
		for(int i = pageNum*pageIndex; (i < allTempTarget.size()) && (i < pageNum*pageIndex + 12); i++) {
			tempTargetIDs.add(allTempTarget.get(i));
		}
		request.setAttribute("targetIDs", tempTargetIDs);
		request.setAttribute("pageIndex", pageIndex);
		return "verifyTarget";
	}
	
	@RequestMapping("/RatifyTarget")
	public String execute2(@RequestParam("userID") String userID,
			@RequestParam("tempTargetID") String tempTargetID,
			HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) 
			throws IOException, URISyntaxException{
		contentService.ratifyTarget(userID, tempTargetID);
		return null;
	}
	
	/*GET and SET*/
	public ContentService getMongoDBService(){
		return contentService;
	}
	
	public void setMongoDBService(ContentService s){
		contentService = s;
	}
	
	public VuforiaService getVuforiaService() {
		return vuforiaService;
	}
	
	public void setVuforiaService(VuforiaService s) {
		vuforiaService = s;
	}
}
