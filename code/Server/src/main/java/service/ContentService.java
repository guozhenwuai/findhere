package service;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

public interface ContentService {
	public void returnContentByID(String contentID, String type, HttpServletResponse response) throws IOException;
	public void returnContentIDsBytargetID(String targetID, HttpServletResponse response) throws IOException;
}
