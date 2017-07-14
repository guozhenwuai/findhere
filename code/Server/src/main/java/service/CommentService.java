package service;

import java.io.IOException;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

public interface CommentService {
	public String getCommentText(String commentID);
	public void returnCommentByID(String commentID, HttpServletResponse response) throws IOException;
	public void returnCommentIDsBytargetID(String targetID, int pageNum, int pageIndex, HttpServletResponse response) throws IOException;
	public String saveTextComment(JSONObject jsonObj, String userID);
	public String updateTextComment(JSONObject jsonObj, String userID);
	public String saveFileComment(String userID, JSONObject jsonObj, ServletInputStream inStream);
	public boolean deleteComment(String commentID, String userID);
}
