package controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
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
		Double Px = 0.0;
		Double Py = 0.0;
		Double Pz = 0.0;
		Double Rx = 0.0;
		Double Ry = 0.0;
		Double Rz = 0.0;
		Double Sx = 0.0;
		Double Sy = 0.0;
		Double Sz = 0.0;
		try {
			Px = Double.parseDouble(request.getParameter("Px"));
			Py = Double.parseDouble(request.getParameter("Py"));
			Pz = Double.parseDouble(request.getParameter("Pz"));
			Rx = Double.parseDouble(request.getParameter("Rx"));
			Ry = Double.parseDouble(request.getParameter("Ry"));
			Rz = Double.parseDouble(request.getParameter("Rz"));
			Sx = Double.parseDouble(request.getParameter("Sx"));
			Sy = Double.parseDouble(request.getParameter("Sy"));
			Sz = Double.parseDouble(request.getParameter("Sz"));
		}catch(Exception e) {
			return "contentManager-object";
		}
		
		JSONObject position = new JSONObject();
		position.put("Px", Px);
		position.put("Py", Py);
		position.put("Pz", Pz);
		position.put("Rx", Rx);
		position.put("Ry", Ry);
		position.put("Rz", Rz);
		position.put("Sx", Sx);
		position.put("Sy", Sy);
		position.put("Sz", Sz);
		
		Map<String, MultipartFile> files = multiRequest.getFileMap();
		MultipartFile object = files.remove("objectFile");
		MultipartFile MTL = files.remove("MTLFile");
		String name = object.getOriginalFilename();
		String MtlName = MTL.getOriginalFilename();
		if(!name.substring(name.lastIndexOf('.'), name.length()).equals(".obj")) {
			return "contentManager-object";
		}else if(!MtlName.substring(MtlName.lastIndexOf('.'), MtlName.length()).equals(".mtl")) {
			return "contentManager-object";
		}
		contentService.addARObject(targetID, object, MTL, files, position);
		return "uploadSuccess";
	}
	
	@RequestMapping("/addTarget")
	public String execute3(@RequestParam("newTarget") MultipartFile newTarget,
			HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) 
			throws IOException{
		String userID = (String) httpSession.getAttribute("userID");
		String filename = newTarget.getName();
		InputStream inStream = newTarget.getInputStream();
		contentService.addTarget(userID, filename, inStream);
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
