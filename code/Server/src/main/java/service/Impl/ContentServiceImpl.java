package service.Impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import dao.ARManagerDao;
import dao.ContentDao;
import dao.FileDao;
import dao.UserTargetDao;
import model.ARManager;
import model.Content;
import service.ContentService;

public class ContentServiceImpl implements ContentService {
	
	@Resource
	private ContentDao contentDao;
	@Resource
	private ARManagerDao arManagerDao;
	@Resource
	private FileDao fileDao;
	@Resource
	private UserTargetDao userTargetDao;
	
	public void returnContentByID(String contentID, String type, HttpServletResponse response) throws IOException {
		Content content = contentDao.findOneByID(contentID);
		switch(content.getType()) {
		case("text"):
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("text", content.getText());
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().print(jsonObj.toString());
			break;
		case("image"):
			fileDao.outputFileToStream("image", content.getImageID(), response.getOutputStream());
			break;
		case("audio"):
			fileDao.outputFileToStream("audio", content.getAudioID(), response.getOutputStream());
			break;
		case("ARObject"):
			ARManager arManager = arManagerDao.findOneByID(content.getARManagerID());
			if(type.equals("object")) {
				fileDao.outputFileToStream("ARObject", arManager.getARObectID(), response.getOutputStream());
			}else if(type.equals("texture")) {
				fileDao.outputFileToStream("texture", arManager.getTexture(), response.getOutputStream());
			}else if(type.equals("MTL")) {
				fileDao.outputFileToStream("MTL", content.getAudioID(), response.getOutputStream());
			}
			break;
		}
	}
	
	public void returnContentIDsBytargetID(String targetID, HttpServletResponse response) throws IOException {
		List<Content> contents = contentDao.getContentsByTargetID(targetID);
		List<JSONObject> ret = new ArrayList<JSONObject>();
		for(int i = 0; i < contents.size(); i++) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("type", contents.get(i).getType());
			jsonObj.put("contentID", contents.get(i).getId());
			ret.add(jsonObj);
		}
		JSONArray jsonArray = new JSONArray(ret);
		response.getWriter().print(jsonArray.toString());
	}
	
	public void addARObject(String targetID, InputStream objectFile, InputStream textureFile, InputStream MLTFile) 
			throws IOException{
		System.out.println(":2.1");
		String ARObjectID = fileDao.inputFileToDB("ARObject", objectFile);
		String texture = fileDao.inputFileToDB("texture", textureFile);
		String MLTID = fileDao.inputFileToDB("MLT", MLTFile);
		
		System.out.println(":2.2");
		ARManager arManager = new ARManager();
		arManager.setARObjectID(ARObjectID);
		arManager.setTexture(texture);
		arManager.setMTLID(MLTID);
		String ARManagerID = arManagerDao.insertOne(arManager);
		
		System.out.println(":2.3");
		Content content = new Content();
		content.setTargetID(targetID);
		content.setARManagerID(ARManagerID);
		content.setType("ARObject");
		String contentID = contentDao.insertOne(content);
	}
	
	public List<String> getUserTargetIDs(String userID){
		return userTargetDao.findUserTargetIDs(userID);
	}
	
	/*GET and SET*/
	public ContentDao getContentDao() {
		return contentDao;
	}
	
	public void setContentDao(ContentDao s) {
		contentDao = s;
	}
	
	public ARManagerDao getARManagerDao() {
		return arManagerDao;
	}
	
	public void setARManagerDao(ARManagerDao s) {
		arManagerDao = s;
	}
	
	public FileDao getFileDao() {
		return fileDao;
	}
	
	public void setFileDao(FileDao dao) {
		fileDao = dao;
	}
	
	public UserTargetDao getUserTargetDao() {
		return userTargetDao;
	}
	
	public void setUserTargetDao(UserTargetDao dao) {
		userTargetDao = dao;
	}
}
