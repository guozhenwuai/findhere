package dao.Impl;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import dao.UserDao;
import model.User;

@Repository("userDaoImpl")
public class UserDaoImpl implements UserDao {
	
	@Resource
	private MongoTemplate mongoTemplate;
	
	public User findOneByID(String id) {
		User user = mongoTemplate.findOne(new Query(Criteria.where("_id").is(id)), User.class);
		return user;
	}
	
	/*GET and SET*/
	public MongoTemplate getMongoTemplate() {
		return mongoTemplate;
	}
	
	public void setMongoTemplate(MongoTemplate mongo) {
		mongoTemplate = mongo;
	}
}
