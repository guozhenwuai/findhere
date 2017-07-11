package dao.Impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.annotation.Resource;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;

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
	
	public String inputFileToDB(String type, InputStream inStream, DBObject metaData) {
		db = mongoTemplate.getDb();
		gfsInput = new GridFS(db, type).createFile(inStream);
		gfsInput.setFilename("ee.jpg");
		gfsInput.setContentType("image/jpeg");
		gfsInput.setMetaData(metaData);
		gfsInput.save();
		String id = gfsInput.getId().toString();
		return id;
	}
	
	public boolean removeFile(String type, String fileID) {
		db = mongoTemplate.getDb();
		new GridFS(db, type).remove(new ObjectId(fileID));
		return true;
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
