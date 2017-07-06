package service.Impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
}
