package dao;

import java.util.Date;
import java.util.List;

import model.Comment;

public interface CommentDao {
	public Comment findOneByID(String id);
	public String insert(Comment comment);
	public String update(String commentID, String text, Date time);
	public List<Comment> getSomeCommentsByTargetID(String targetID, int skipNum, int num);
	public List<Comment> getAllCommentsByTargetID(String targetID);
	public List<Comment> getSomeCommentsByUserID(String userID, int skipNum, int num);
	public boolean remove(String commentID, String userID);
	public boolean removeCascaded(String commentID, String userID);
	public boolean removeCascadedByTargetID(String targetID);
}
