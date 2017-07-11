package service.Impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletResponse;
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
	
	public void returnCommentIDsBytargetID(String targetID, int pageNum, int pageIndex, HttpServletResponse response)
			throws IOException{
		List<Comment> comments = commentDao.getSomeCommentsByTargetID(targetID, pageNum*pageIndex, pageNum);
		List<JSONObject> ret = new ArrayList<JSONObject>();
		for(int i = 0; i < comments.size(); i++) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("type", comments.get(i).getType());
			jsonObj.put("commentID", comments.get(i).get_id());
			ret.add(jsonObj);
		}
		JSONArray jsonArray = new JSONArray(ret);
		response.getWriter().print(jsonArray.toString());
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
	
	public FileDao getFileDao() {
		return fileDao;
	}
	
	public void setFileDao(FileDao dao) {
		fileDao = dao;
	}
}
