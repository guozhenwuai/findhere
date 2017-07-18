package dao.Impl;

import java.util.List;

import javax.annotation.Resource;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import dao.UserTargetDao;
import model.UserTarget;

public class UserTargetDaoImpl implements UserTargetDao {
	
	@Resource
	private MongoTemplate mongoTemplate;
	
	public List<String> findUserTargetIDs(String userID){
		UserTarget userTarget = mongoTemplate.findOne(
				new Query(Criteria.where("_id").is(userID))
				, UserTarget.class);
		if(userTarget == null) {
			return null;
		}
		return userTarget.getTargetIDs();
	}
	
	public List<String> findAllUserTargetIDs(String userID){
		UserTarget userTarget = mongoTemplate.findOne(
				new Query(Criteria.where("_id").is(userID)), UserTarget.class);
		if(userTarget == null) {
			return null;
		}
		return userTarget.getTargetIDs();
	}
	
	public UserTarget findOneUserTarget(String userID){
		UserTarget userTarget = mongoTemplate.findOne(
				new Query(Criteria.where("_id").is(userID)), UserTarget.class);
		return userTarget;
	}
	
	public void update(UserTarget userTarget) {
		mongoTemplate.save(userTarget);
	}
	
	public List<String> findAllTempTargetIDs(String userID){
		UserTarget userTarget = mongoTemplate.findOne(
				new Query(Criteria.where("_id").is(userID)), UserTarget.class);
		if(userTarget == null) {
			return null;
		}
		return userTarget.getTempTargetIDs();
	}
	
	public List<UserTarget> findHasTempTargetUsers(){
		List<UserTarget> allUserTarget = mongoTemplate.find(new Query(Criteria
				.where("hasTempTarget").is(true))
				, UserTarget.class);
		return allUserTarget;
	}
	
	/*GET and SET*/
	public MongoTemplate getMongoTemplate() {
		return mongoTemplate;
	}
	
	public void setMongoTemplate(MongoTemplate mongo) {
		mongoTemplate = mongo;
	}
}
