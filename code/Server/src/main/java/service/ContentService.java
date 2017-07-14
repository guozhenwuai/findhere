package service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

public interface ContentService {
	public void returnContentByID(String contentID, String type, HttpServletResponse response) throws IOException;
	public void returnContentIDsBytargetID(String targetID, HttpServletResponse response) throws IOException;
	public void addARObject(String targetID, InputStream objectFile, InputStream textureFile, InputStream MLTFile) throws IOException;
	public List<String> getUserTargetIDs(String userID);
}
