package dao;

import java.io.IOException;
import java.io.OutputStream;

public interface FileDao {
	public void outputFileToStream(String gfsName, String fileID, OutputStream outStream) throws IOException;
}
