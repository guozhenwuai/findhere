package service.Impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import dao.ARManagerDao;
import dao.ContentDao;
import dao.FileDao;
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
}
