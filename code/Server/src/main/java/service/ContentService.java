package service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import tool.Wrapper;

public interface ContentService {
	public void returnContentByID(String contentID, String type, HttpServletResponse response) throws IOException;
	public void returnContentIDsBytargetID(String targetID, HttpServletResponse response) throws IOException;
	public void returnContentObjectByARID(String ARManagerID, HttpServletResponse response) throws IOException;
	public void returnContentMTL(String ARManagerID, String name, HttpServletResponse response) throws IOException;
	public void returnContentTexture(String ARManagerID, String name, HttpServletResponse response) throws IOException;
	public void returnContentObjectPosition(String ARManagerID, HttpServletResponse response) throws IOException;
	public void addARObject(String targetID, String textName, MultipartFile objectFile, MultipartFile MLTFile, Map<String, MultipartFile> textureFiles, JSONObject position) throws IOException;
	public List<String> getUserTargetIDs(String userID);
	public void addTarget(String userID, String filename, InputStream inStream);
	public void deleteTarget(String userID, String targetID) throws ClientProtocolException, URISyntaxException, IOException;
	public void deleteTempTarget(String userID, String tempTargetID);
	public List<String> getTempTargetIDsByUserID(String userID);
	public List<Wrapper> getAllTempTarget();
	public void ratifyTarget(String userID, String tempTargetID) throws IOException, URISyntaxException;
	public void getTarget(String targetID, HttpServletResponse response) throws IOException;
	public void getTempTarget(String tempTargetID, HttpServletResponse response) throws IOException;
	public List<Wrapper> getmodelsByUserID(String userID);
	public void deleteContent(String contentID);
}
