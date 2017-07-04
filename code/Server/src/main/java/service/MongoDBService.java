package service;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

public interface MongoDBService {
	public String getCommentText(String commentID);
	public void returnComment(String commentID, HttpServletResponse response) throws IOException;
}
