package service;

import java.io.IOException;
import java.io.InputStream;

public interface ReadService {
	public String inputStreamToString(InputStream inStream) throws IOException;
}
