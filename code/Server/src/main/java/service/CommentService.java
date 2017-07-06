package service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

public interface CommentService {
	public String getCommentText(String commentID);
	public void returnCommentByID(String commentID, HttpServletResponse response) throws IOException;
	public void returnCommentIDsBytargetID(String targetID, HttpServletResponse response) throws IOException;
	public String saveTextComment(JSONObject jsonObj, String userID);
	public String saveFileComment(String userID, JSONObject jsonObj);
}
