package service.Impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;

import dao.CommentDao;
import dao.FileDao;
import model.Comment;
import service.CommentService;

public class CommentServiceImpl implements CommentService {
	
	@Resource
	private CommentDao commentDao;
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
		ServletOutputStream outStream = response.getOutputStream();
		switch(comment.getType()) {
		case("text"):
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("userID", comment.getUserID());
			jsonObj.put("text", comment.getText());
			jsonObj.put("time", comment.getTime());
			outStream.print(jsonObj.toString());
			break;
		case("sound"):
			fileDao.outputFileToStream("sound", comment.getSoundID(), outStream);
			break;
		case("picture"):
			fileDao.outputFileToStream("picture", comment.getPictureID(), outStream);
			break;
		}
		outStream.close();
	}
	
	public void returnCommentIDsBytargetID(String targetID, HttpServletResponse response)
			throws IOException{
		ServletOutputStream outStream = response.getOutputStream();
		List<Comment> comments = commentDao.getSomeCommentsByTargetID(targetID, 20);
		JSONArray jsonArray = new JSONArray(comments);
		outStream.print(jsonArray.toString());
		outStream.close();
	}
	
	public String saveTextComment(JSONObject jsonObj, String userID) {
		String type = jsonObj.getString("type");
		String text = jsonObj.getString("text");
		String targetID = jsonObj.getString("targetID");
		Date time = new Date();
		if( type.equals("text") && text != null) {
			Comment comment = new Comment();
			comment.setUserID(userID);
			comment.setType(type);
			comment.setText(text);
			comment.setTargetID(targetID);
			comment.setTime(time);
			String id = commentDao.insert(comment);
			return id;
		}
		return null;
	}
	
	public String saveFileComment(String userID, JSONObject jsonObj) {
		byte[] data = (byte[]) jsonObj.get("file");
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
		
		String fileID = fileDao.inputFileToDB(type, data);
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
	
	/*GET and SET*/
	public CommentDao getCommentDao() {
		return commentDao;
	}
	
	public void setCommentDao(CommentDao c) {
		commentDao = c;
	}
	
	public FileDao getFileDao() {
		return fileDao;
	}
	
	public void setFileDao(FileDao dao) {
		fileDao = dao;
	}
}
