package service.Impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONObject;

import service.ReadService;

public class ReadServiceImpl implements ReadService {
	public String inputStreamToString(InputStream inStream) throws IOException {
		String ret = "";
		
		InputStreamReader inputStreamReader = new InputStreamReader(inStream);
		BufferedReader reader = new BufferedReader(inputStreamReader);
		String temp = null;
		
		while((temp = reader.readLine()) != null) {
			ret = ret + temp;
		}
		reader.close();
		return ret;
	}
	
	public JSONObject inputStreamReadJson(InputStream inStream) 
			throws IOException{
		String rawJson = "";
		int ch = 0;
		if((char)(ch = inStream.read()) != '{') {
			return null;
		}
		rawJson += (char)ch;//'{'
		
		while((ch = inStream.read()) != -1 && (char)ch != '}') {
			rawJson += (char)ch;
		}
		rawJson += (char)ch;//'}'
		System.out.println(rawJson);
		
		JSONObject jsonObj = new JSONObject(rawJson);
		return jsonObj;
	}
}
