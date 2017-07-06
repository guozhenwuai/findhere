package dao;

import java.util.List;

import model.Comment;

public interface CommentDao {
	public Comment findOneByID(String id);
	public String insert(Comment comment);
	public List<Comment> getSomeCommentsByTargetID(String targetID, int num);
}
