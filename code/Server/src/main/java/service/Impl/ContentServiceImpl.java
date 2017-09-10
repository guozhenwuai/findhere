package service.Impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import dao.ARManagerDao;
import dao.ContentDao;
import dao.FileDao;
import dao.UserTargetDao;
import model.ARManager;
import model.Content;
import model.UserTarget;
import service.ContentService;
import service.VuforiaService;
import tool.ByteFileWithName;
import tool.Wrapper;

public class ContentServiceImpl implements ContentService {
	
	@Resource
	private ContentDao contentDao;
	@Resource
	private ARManagerDao arManagerDao;
	@Resource
	private FileDao fileDao;
	@Resource
	private UserTargetDao userTargetDao;
	
	@Resource
	private VuforiaService vuforiaService;
	
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
				//fileDao.outputFileToStream("texture", arManager.getTexture(), response.getOutputStream());
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
			switch(contents.get(i).getType()) {
			case("text"):
				jsonObj.put("contentID", contents.get(i).getId());
				jsonObj.put("text", contents.get(i).getText());
				break;
			case("image"):
				jsonObj.put("contentID", contents.get(i).getImageID());
				break;
			case("audio"):
				jsonObj.put("contentID", contents.get(i).getAudioID());
				break;
			case("ARObject"):
				jsonObj.put("contentID", contents.get(i).getARManagerID());
				break;
			}
			ret.add(jsonObj);
		}
		JSONArray jsonArray = new JSONArray(ret);
		response.getWriter().print(jsonArray.toString());
	}
	
	public void returnContentObjectByARID(String ARManagerID, HttpServletResponse response) throws IOException {
		ARManager arManager = arManagerDao.findOneByID(ARManagerID);
		fileDao.outputFileToStream("ARObject", arManager.getARObectID(), response.getOutputStream());
	}
	
	public void returnContentMTL(String ARManagerID, String name, HttpServletResponse response)
			throws IOException{
		ARManager arManager = arManagerDao.findOneByID(ARManagerID);
		String id = arManager.getMTLID().get(name.replace('.', '_'));
		fileDao.outputFileToStream("MTL", id, response.getOutputStream());
	}
	
	public void returnContentTexture(String ARManagerID, String name, HttpServletResponse response)
			throws IOException{
		ARManager arManager = arManagerDao.findOneByID(ARManagerID);
		String id = arManager.getTexture().get(name.replace('.', '_'));
		fileDao.outputFileToStream("texture", id, response.getOutputStream());
	}
	
	public void returnContentObjectPosition(String ARManagerID, HttpServletResponse response)
			throws IOException{
		ARManager arManager = arManagerDao.findOneByID(ARManagerID);
		response.getOutputStream().print(arManager.getPosition().toString());
	}
	
	public void addARObject(String targetID, String textName, MultipartFile objectFile, MultipartFile MTLFile, Map<String, MultipartFile> textureFiles, JSONObject position)
			throws IOException{
		System.out.println(":2.1");
		String ARObjectID = fileDao.inputFileToDBWithName("ARObject", objectFile.getOriginalFilename(), objectFile.getInputStream());
		String MTLID = fileDao.inputFileToDBWithName("MTL", MTLFile.getOriginalFilename(), MTLFile.getInputStream());
		
		System.out.println(":2.2");
		Map<String, String> MTLMap = new HashMap<String, String>();
		Map<String, String> textureMap = new HashMap<String, String>();
		MTLMap.put(MTLFile.getOriginalFilename().replace('.', '_'), MTLID);
		System.out.println(":2.2.1");
		
		for (Map.Entry<String, MultipartFile> entry : textureFiles.entrySet()) {
			String textureID = fileDao.inputFileToDBWithName("texture", entry.getValue().getOriginalFilename(), entry.getValue().getInputStream());
			textureMap.put(entry.getValue().getOriginalFilename().replace('.', '_'), textureID);
		}
		System.out.println(":2.2.2");
		ARManager arManager = new ARManager();
		arManager.setARObjectID(ARObjectID);
		arManager.setTexture(textureMap);
		arManager.setMTLID(MTLMap);
		arManager.setPosition(position);
		System.out.println(":2.2.3");
		String ARManagerID = arManagerDao.insertOne(arManager);
		
		System.out.println(":2.3");
		Content content = new Content();
		content.setTargetID(targetID);
		content.setARManagerID(ARManagerID);
		content.setText(textName);
		content.setType("ARObject");
		String contentID = contentDao.insertOne(content);
	}
	
	public void addTarget(String userID, String filename, InputStream inStream) {
		String fileID = fileDao.inputFileToDBWithName("tempTarget", filename, inStream);
		UserTarget userTarget = userTargetDao.findOneUserTarget(userID);
		if(userTarget == null) {
			userTarget = new UserTarget();
			userTarget.setUserID(userID);
		}
		List<String> tempTargetIDs = userTarget.getTempTargetIDs();
		tempTargetIDs.add(fileID);
		userTarget.setTempTargetIDs(tempTargetIDs);
		userTarget.setHasTempTarget(true);
		userTargetDao.update(userTarget);
	}
	
	public void deleteTarget(String userID, String targetID) {
		fileDao.removeFileByName("target", targetID);
		UserTarget userTarget = userTargetDao.findOneUserTarget(userID);
		List<String> targetIDs = userTarget.getTargetIDs();
		for(int i = 0; i < targetIDs.size(); i++) {
			if(targetIDs.get(i).equals(targetID)) {
				targetIDs.remove(i);
			}
		}
		userTarget.setTargetIDs(targetIDs);
		userTargetDao.update(userTarget);
	}
	
	public void deleteTempTarget(String userID, String tempTargetID) {
		fileDao.removeFile("tempTarget", tempTargetID);
		UserTarget userTarget = userTargetDao.findOneUserTarget(userID);
		List<String> targetIDs = userTarget.getTempTargetIDs();
		for(int i = 0; i < targetIDs.size(); i++) {
			if(targetIDs.get(i).equals(tempTargetID)) {
				targetIDs.remove(i);
			}
		}
		userTarget.setTempTargetIDs(targetIDs);
		userTargetDao.update(userTarget);
	}
	
	public List<String> getUserTargetIDs(String userID){
		return userTargetDao.findUserTargetIDs(userID);
	}
	
	public List<String> getTempTargetIDsByUserID(String userID){
		return userTargetDao.findAllTempTargetIDs(userID);
	}
	
	public List<Wrapper> getAllTempTarget(){
		List<Wrapper> allTempTargetIDs = new ArrayList<Wrapper>();
		List<UserTarget> aimUserTarget = userTargetDao.findHasTempTargetUsers();
		for(int i = 0; i < aimUserTarget.size(); i++) {
			List<String> tempIDs = aimUserTarget.get(i).getTempTargetIDs();
			for(int j = 0; j < tempIDs.size(); j++) {
				Wrapper pair = new Wrapper();
				pair.setFirst(aimUserTarget.get(i).getUserID());
				pair.setSecond(tempIDs.get(j));
				allTempTargetIDs.add(pair);
			}
		}
		return allTempTargetIDs;
	}
	
	public void ratifyTarget(String userID, String tempTargetID) throws IOException, URISyntaxException {
		UserTarget userTarget = userTargetDao.getUserTargetByuserID(userID);
		List<String> tempIDs = userTarget.getTempTargetIDs();
		
		ByteFileWithName target = fileDao.outputFileToByteWithName("tempTarget", tempTargetID);
		String newTargetID = "";
		//do {
			newTargetID = vuforiaService.uploadImage(target);
		//}while(newTargetID.length() == 0);
		
		//set new target to userTarget and target files
		List<String> targetIDs = userTarget.getTargetIDs();
		targetIDs.add(newTargetID);
		
		fileDao.inputFileToDBWithName("target", newTargetID, target.getByteElement());
		
		//clear
		for(int i = 0; i < tempIDs.size(); i++) {
			if(tempIDs.get(i).equals(tempTargetID)) {
				tempIDs.remove(i);
			}
		}
		userTarget.setTempTargetIDs(tempIDs);
		if(tempIDs.size() == 0) {
			userTarget.setHasTempTarget(false);
		}
		userTargetDao.update(userTarget);
		
		fileDao.removeFile("tempTarget", tempTargetID);
	}
	
	public void getTarget(String targetID, HttpServletResponse response) throws IOException {
		fileDao.outputFileToStreamByFileName("target", targetID, response.getOutputStream());
	}
	
	public void getTempTarget(String tempTargetID, HttpServletResponse response)
			throws IOException{
		fileDao.outputFileToStream("tempTarget", tempTargetID, response.getOutputStream());
	}
	
	public List<Wrapper> getmodelsByUserID(String userID){
		UserTarget userTarget = userTargetDao.findOneUserTarget(userID);
		List<String> targetIDs = userTarget.getTargetIDs();
		List<Wrapper> modelIDs = new ArrayList<Wrapper>();
		for(int i = 0; i < targetIDs.size(); i++) {
			List<Content> contents = contentDao.getContentsByTargetID(targetIDs.get(i));
			for(int j = 0; j < contents.size(); j++) {
				if(contents.get(j).getType().equals("ARObject")) {
					Wrapper bag = new Wrapper();
					bag.setFirst(contents.get(j).getText());
					bag.setSecond(targetIDs.get(i));
					bag.setThird(contents.get(j).getId().toString());
					modelIDs.add(bag);
				}
			}
		}
		return modelIDs;
	}
	
	public void deleteContent(String contentID) {
		Content aim = contentDao.findOneByID(contentID);
		switch(aim.getType()) {
		case("ARObject"):
			arManagerDao.deleteOne(aim.getARManagerID());
			break;
		}
		contentDao.deleteOne(contentID);
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
	
	public VuforiaService getVuforiaService() {
		return vuforiaService;
	}
	
	public void setVuforiaService(VuforiaService s) {
		vuforiaService = s;
	}
}
