package dao.Impl;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

import dao.FileDao;

public class FileDaoImpl implements FileDao{
	
	@Resource
	private MongoTemplate mongoTemplate;
	
	private DB db;
	private GridFSInputFile gfsInput;
	private GridFSDBFile gfsFile;
	
	public void outputFileToStream(String gfsName, String fileID, OutputStream outStream) 
			throws IOException {
		db = mongoTemplate.getDb();
		gfsFile = new GridFS(db, gfsName).findOne(new ObjectId(fileID));
		if(gfsFile == null) return;
		gfsFile.writeTo(outStream);
	}
	
	public void outputFileToStreamByFileName(String gfsName, String fileID, OutputStream outStream) 
			throws IOException {
		db = mongoTemplate.getDb();
		gfsFile = new GridFS(db, gfsName).findOne(fileID);
		if(gfsFile == null) return;
		gfsFile.writeTo(outStream);
	}
	
	public void outputFileToStreamByStringID(String gfsName, String fileID, OutputStream outStream) 
			throws IOException {
		db = mongoTemplate.getDb();
		DBObject query = new BasicDBObject("_id", fileID);
		gfsFile = new GridFS(db, gfsName).findOne(query);
		if(gfsFile == null) return;
		gfsFile.writeTo(outStream);
	}
	
	public String inputFileToDB(String gfsName, InputStream inStream) {
		db = mongoTemplate.getDb();
		gfsInput = new GridFS(db, gfsName).createFile(inStream);
		gfsInput.save();
		String id = gfsInput.getId().toString();
		return id;
	}
	
	public boolean removeFile(String gfsName, String fileID) {
		db = mongoTemplate.getDb();
		new GridFS(db, gfsName).remove(new ObjectId(fileID));
		return true;
	}
	
	public byte[] outputFileToByteByStringID(String gfsName, String fileID) throws IOException {
		db = mongoTemplate.getDb();
		DBObject query = new BasicDBObject("_id", fileID);
		gfsFile = new GridFS(db, gfsName).findOne(query);
		if(gfsFile == null) return null;
		InputStream inStream = gfsFile.getInputStream();
		ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
		byte[] buff = new byte[100]; 
		int rc = 0;
		while ((rc = inStream.read(buff, 0, 100)) > 0) {
			swapStream.write(buff, 0, rc);
		}
		byte[] file = swapStream.toByteArray(); 
		return file;
	}
	
	public byte[] outputFileToByte(String gfsName, String fileID) throws IOException {
		db = mongoTemplate.getDb();
		gfsFile = new GridFS(db, gfsName).findOne(new ObjectId(fileID));
		if(gfsFile == null) return null;
		InputStream inStream = gfsFile.getInputStream();
		ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
		byte[] buff = new byte[100]; 
		int rc = 0;
		while ((rc = inStream.read(buff, 0, 100)) > 0) {
			swapStream.write(buff, 0, rc);
		}
		byte[] file = swapStream.toByteArray(); 
		return file;
	}
	
	public byte[] outputFileToByteByFileName(String gfsName, String fileID) throws IOException {
		db = mongoTemplate.getDb();
		gfsFile = new GridFS(db, gfsName).findOne(fileID);
		if(gfsFile == null) return null;
		InputStream inStream = gfsFile.getInputStream();
		ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
		byte[] buff = new byte[100]; 
		int rc = 0;
		while ((rc = inStream.read(buff, 0, 100)) > 0) {
			swapStream.write(buff, 0, rc);
		}
		byte[] file = swapStream.toByteArray(); 
		
		return file;
	}
	
	/*GET and SET*/
	public MongoTemplate getMongoTemplate() {
		return mongoTemplate;
	}
	
	public void setMongoTemplate(MongoTemplate mongo) {
		mongoTemplate = mongo;
	}
	
	public GridFSInputFile getGfsInput() {
		return gfsInput;
	}
	
	public void setGfsInput(GridFSInputFile gfs) {
		gfsInput = gfs;
	}
	
	public GridFSDBFile getGfsFile() {
		return gfsFile;
	}
	
	public void setGfsFile(GridFSDBFile gfs) {
		gfsFile = gfs;
	}
}
