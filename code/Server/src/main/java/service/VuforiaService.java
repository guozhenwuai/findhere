package service;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONObject;

import tool.ByteFileWithName;

public interface VuforiaService {
	public String uploadImage(ByteFileWithName target) throws URISyntaxException, ParseException, IOException;
	public JSONObject getTarget(String targetID) throws URISyntaxException, ClientProtocolException, IOException;
	public boolean deleteTarget(String targetId) throws URISyntaxException, ClientProtocolException, IOException;
}
