package dao.Impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import dao.ARManagerDao;
import dao.FileDao;
import model.ARManager;
import model.Content;

public class ARManagerDaoImpl implements ARManagerDao {
	
	@Resource
	private MongoTemplate mongoTemplate;
	
	@Resource
	private FileDao fileDao;
	
	public ARManager findOneByID(String ARManagerID) {
		ARManager arManager = mongoTemplate.findOne(new Query(Criteria.where("_id").is(ARManagerID)), ARManager.class);
		return arManager;
	}
	
	public String insertOne(ARManager arManager) {
		mongoTemplate.insert(arManager);
		return arManager.getId().toString();
	}
	
	public void deleteOne(String ARManagerID) {
		ARManager arManager = mongoTemplate.findOne(
				new Query(Criteria.where("_id").is(ARManagerID)), ARManager.class);
		fileDao.removeFile("ARObject", arManager.getARObectID());
		Map<String, String> mtl = arManager.getMTLID();
		Map<String, String> texture = arManager.getTexture();
		for(Map.Entry<String, String> entry : mtl.entrySet()) {
			fileDao.removeFile("MTL", entry.getValue());
		}
		for(Map.Entry<String, String> entry : texture.entrySet()) {
			fileDao.removeFile("texture", entry.getValue());
		}
		mongoTemplate.remove(
				new Query(Criteria.where("_id").is(ARManagerID)), ARManager.class);
	}
	
	/*GET and SET*/
	public MongoTemplate getMongoTemplate() {
		return mongoTemplate;
	}
	
	public void setMongoTemplate(MongoTemplate mongo) {
		mongoTemplate = mongo;
	}
	
	public FileDao getFileDao() {
		return fileDao;
	}
	
	public void setFileDao(FileDao dao) {
		fileDao = dao;
	}
}
