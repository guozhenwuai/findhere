package dao.Impl;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import dao.ARManagerDao;
import model.ARManager;

public class ARManagerDaoImpl implements ARManagerDao {
	
	@Resource
	private MongoTemplate mongoTemplate;
	
	public ARManager findOneByID(String ARManagerID) {
		ARManager arManager = mongoTemplate.findOne(new Query(Criteria.where("_id").is(ARManagerID)), ARManager.class);
		return arManager;
	}
	
	public String insertOne(ARManager arManager) {
		mongoTemplate.insert(arManager);
		return arManager.getId().toString();
	}
	
	/*GET and SET*/
	public MongoTemplate getMongoTemplate() {
		return mongoTemplate;
	}
	
	public void setMongoTemplate(MongoTemplate mongo) {
		mongoTemplate = mongo;
	}
}
