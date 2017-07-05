package service;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

public interface CommentService {
	public String getCommentText(String commentID);
	public void returnComment(String commentID, HttpServletResponse response) throws IOException;
	public boolean saveTextComment(JSONObject jsonObj);
}
