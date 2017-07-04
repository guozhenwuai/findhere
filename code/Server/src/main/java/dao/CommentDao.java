package dao;

import model.Comment;

public interface CommentDao {
	public Comment findOneByID(String id);
}
