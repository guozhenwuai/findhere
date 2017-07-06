package service.Impl;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

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
	
	public void returnComment(String commentID, HttpServletResponse response) 
			throws IOException {
		Comment comment = commentDao.findOneByID(commentID);
		ServletOutputStream outStream = response.getOutputStream();
		switch(comment.getType()) {
		case("text"):
			outStream.print(comment.getText());
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
	
	public boolean saveTextComment(JSONObject jsonObj) {
		String type = jsonObj.getString("type");
		String text = jsonObj.getString("text");
		if( type == "text" && text != null) {
			Comment comment = new Comment();
			comment.setType(type);
			comment.setText(text);
		}
		return false;
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
