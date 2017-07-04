package service.Impl;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.mongodb.DB;
import com.mongodb.gridfs.GridFSInputFile;

import dao.CommentDao;
import dao.FileDao;
import model.Comment;
import service.MongoDBService;

public class MongoDBServiceImpl implements MongoDBService {
	
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
