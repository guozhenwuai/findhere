package dao.Impl;

import java.util.List;

import javax.annotation.Resource;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import dao.ApplyDao;
import model.Apply;

@Repository("applyDaoImpl")
public class ApplyDaoImpl implements ApplyDao {
	
	@Resource
	private MongoTemplate mongoTemplate;
	
	private int pageNum = 12;
	
	public List<Apply> getAPageApply(int pageIndex){
		List<Apply> applyList = mongoTemplate.find(new Query()
				.skip(pageNum*pageIndex)
				.limit(pageNum)
				, Apply.class);
		return applyList;
	}
	
	public void removeApply(String applyID) {
		ObjectId id = new ObjectId(applyID);
		mongoTemplate.remove(new Query(Criteria.where("_id").is(id)), Apply.class);
	}
	
	public void addApply(Apply apply) {
		mongoTemplate.save(apply);
	}
	
	/*GET and SET*/
	public MongoTemplate getMongoTemplate() {
		return mongoTemplate;
	}
	
	public void setMongoTemplate(MongoTemplate mongo) {
		mongoTemplate = mongo;
	}
}
