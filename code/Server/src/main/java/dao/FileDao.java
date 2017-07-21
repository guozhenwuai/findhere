package dao;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import tool.ByteFileWithName;

public interface FileDao {
	public void outputFileToStream(String gfsName, String fileID, OutputStream outStream) throws IOException;
	public void outputFileToStreamByStringID(String gfsName, String fileID, OutputStream outStream) throws IOException;
	public void outputFileToStreamByFileName(String gfsName, String fileID, OutputStream outStream) throws IOException;
	public String inputFileToDB(String gfsName, InputStream inStream);
	public String inputFileToDBWithName(String gfsName, String filename, InputStream inStream);
	public String inputFileToDBWithName(String gfsName, String filename, byte[] data);
	public boolean removeFile(String gfsName, String fileID);
	public boolean removeFileByName(String gfsName, String fileName);
	public byte[] outputFileToByte(String gfsName, String fileID) throws IOException;
	public byte[] outputFileToByteByStringID(String gfsName, String fileID) throws IOException;
	public byte[] outputFileToByteByFileName(String gfsName, String fileID) throws IOException;
	public ByteFileWithName outputFileToByteWithName(String gfsName, String fileID) throws IOException;
}
