package service.Impl;

import javax.annotation.Resource;

import dao.CommentDao;
import model.Comment;
import service.MongoDBService;

public class MongoDBServiceImpl implements MongoDBService {
	
	@Resource
	private CommentDao commentDao;
	
	public String getCommentText(String commentID) {
		Comment comment = commentDao.findOneByID(commentID);
		String s = comment.getText();
		return s;
	}
	
	/*GET and SET*/
	public CommentDao getCommentDao() {
		return commentDao;
	}
	
	public void setCommentDao(CommentDao c) {
		commentDao = c;
	}
}
