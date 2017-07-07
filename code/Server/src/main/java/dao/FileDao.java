package dao;

import java.io.IOException;
import java.io.OutputStream;

import com.mongodb.DBObject;

public interface FileDao {
	public void outputFileToStream(String gfsName, String fileID, OutputStream outStream) throws IOException;
	public String inputFileToDB(String type, byte[] data, DBObject metaData);
	public boolean removeFile(String type, String fileID);
}
