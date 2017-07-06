package dao;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface FileDao {
	public void outputFileToStream(String gfsName, String fileID, OutputStream outStream) throws IOException;
	public String inputFileToDB(String type, byte[] data);
}
