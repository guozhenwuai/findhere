package service.Impl;

import org.apache.commons.codec.binary.Base64;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dao.CommentDao;
import dao.FileDao;
import dao.UserDao;
import model.Comment;
import model.User;
import service.CommentService;

public class CommentServiceImpl implements CommentService {
	
	@Resource
	private CommentDao commentDao;
	@Resource
	private UserDao userDao;
	@Resource
	private FileDao fileDao;
	
	public String getCommentText(String commentID) {
		Comment comment = commentDao.findOneByID(commentID);
		String s = comment.getText();
		return s;
	}
	
	public void returnCommentByID(String commentID, HttpServletResponse response) 
			throws IOException {
		Comment comment = commentDao.findOneByID(commentID);
		switch(comment.getType()) {
		case("text"):
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("userID", comment.getUserID());
			jsonObj.put("text", comment.getText());
			jsonObj.put("time", comment.getTime());
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().print(jsonObj.toString());
			break;
		case("sound"):
			fileDao.outputFileToStream("sound", comment.getSoundID(), response.getOutputStream());
			break;
		case("picture"):
			fileDao.outputFileToStream("picture", comment.getPictureID(), response.getOutputStream());
			break;
		}
	}
	
	public void returnCommentIDsByTargetID(String targetID, int pageNum, int pageIndex, HttpServletResponse response)
			throws IOException{
		List<Comment> comments = commentDao.getSomeCommentsByTargetID(targetID, pageNum*pageIndex, pageNum);
		List<JSONObject> ret = new ArrayList<JSONObject>();
		for(int i = 0; i < comments.size(); i++) {
			User user = userDao.findOneByID(comments.get(i).getUserID());
			byte[] headPortrait = fileDao.outputFileToByte("headPortrait", user.getHeadPortraitID());
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("type", comments.get(i).getType());
			jsonObj.put("commentID", comments.get(i).get_id());
			jsonObj.put("time", comments.get(i).getTime());
			jsonObj.put("userID", comments.get(i).getUserID());
			jsonObj.put("userName", user.getName());
			jsonObj.put("headPortrait", Base64.encodeBase64String(headPortrait));
			jsonObj.put("text", comments.get(i).getText());
			jsonObj.put("targetID", comments.get(i).getTargetID());
			ret.add(jsonObj);
		}
		JSONArray jsonArray = new JSONArray(ret);
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().print(jsonArray.toString());
	}
	
	public void returnCommentIDsByUserID(String userID, int pageIndex, HttpServletResponse response)
			throws IOException{
		int pageNum = 10;
		List<Comment> comments = commentDao.getSomeCommentsByUserID(userID, pageNum*pageIndex, pageNum);
		List<JSONObject> ret = new ArrayList<JSONObject>();
		for(int i = 0; i < comments.size(); i++) {
			JSONObject jsonObj = new JSONObject();
			
			byte[] image = fileDao.outputFileToByteByFileName("target", comments.get(i).getTargetID());
			jsonObj.put("type", comments.get(i).getType());
			jsonObj.put("commentID", comments.get(i).get_id());
			jsonObj.put("text", comments.get(i).getText());
			jsonObj.put("time", comments.get(i).getTime());
			jsonObj.put("userID", comments.get(i).getUserID());
			jsonObj.put("targetID", comments.get(i).getTargetID());
			jsonObj.put("image", Base64.encodeBase64String(image));
			ret.add(jsonObj);
		}
		JSONArray jsonArray = new JSONArray(ret);
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().print(jsonArray.toString());
	}
	
	public String saveTextComment(JSONObject jsonObj, String userID) throws UnsupportedEncodingException, JSONException {
		String type = jsonObj.getString("type");
		String text = jsonObj.getString("text");
		text = URLDecoder.decode(text, "UTF-8");
		String targetID = jsonObj.getString("targetID");
		System.out.println(jsonObj.toString());
		Date time = new Date();
		if( type.equals("text") && text != null) {
			Comment comment = new Comment();
			comment.setUserID(userID);
			comment.setType(type);
			comment.setText(text);
			System.out.println("text = "+text);
			comment.setTargetID(targetID);
			comment.setTime(time);
			String id = commentDao.insert(comment);
			return id;
		}
		return null;
	}
	
	public String updateTextComment(JSONObject jsonObj, String userID) {
		String type = jsonObj.getString("type");
		String text = jsonObj.getString("text");
		String commentID = jsonObj.getString("commentID");
		Date time = new Date();
		if( type.equals("text") && text != null) {
			String id = commentDao.update(commentID, text, time);
			return id;
		}
		return null;
	}
	
	public String saveFileComment(String userID, JSONObject jsonObj, ServletInputStream inStream) {
		String targetID = jsonObj.getString("targetID");
		String type = jsonObj.getString("type");
		switch(type) {
		case("image"):
			type = "picture";
			break;
		case("sound"):
			break;
		default:
			return null;
		}
		
		String fileID = fileDao.inputFileToDB(type, inStream);
		Date time = new Date();
		Comment comment = new Comment();
		comment.setUserID(userID);
		comment.setType(type);
		comment.setTargetID(targetID);
		comment.setTime(time);
		switch(type) {
		case("picture"):
			comment.setPictureID(fileID);
			break;
		case("sound"):
			comment.setSoundID(fileID);
			break;
		}
		String id = commentDao.insert(comment);
		return id;
	}
	
	public boolean deleteComment(String commentID, String userID) {
		return commentDao.removeCascaded(commentID, userID);
	}
	
	/*GET and SET*/
	public CommentDao getCommentDao() {
		return commentDao;
	}
	
	public void setCommentDao(CommentDao c) {
		commentDao = c;
	}
	
	public UserDao getUserDao() {
		return userDao;
	}
	
	public void setUserDao(UserDao dao) {
		userDao = dao;
	}
	
	public FileDao getFileDao() {
		return fileDao;
	}
	
	public void setFileDao(FileDao dao) {
		fileDao = dao;
	}
}
