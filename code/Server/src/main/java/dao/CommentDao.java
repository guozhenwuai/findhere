package dao;

import model.Comment;

public interface CommentDao {
	public Comment findOneByID(String id);
	public void insert(Comment comment);
}
