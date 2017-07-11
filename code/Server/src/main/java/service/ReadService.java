package service;

import java.io.IOException;
import java.io.InputStream;

import org.json.JSONObject;

public interface ReadService {
	public String inputStreamToString(InputStream inStream) throws IOException;
	public JSONObject inputStreamReadJson(InputStream inStream) throws IOException;
}
