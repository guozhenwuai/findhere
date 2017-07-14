package dao;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface FileDao {
	public void outputFileToStream(String gfsName, String fileID, OutputStream outStream) throws IOException;
	public String inputFileToDB(String gfsName, InputStream inStream);
	public boolean removeFile(String gfsName, String fileID);
}
