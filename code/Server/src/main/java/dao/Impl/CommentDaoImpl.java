package dao.Impl;

import java.util.List;

import javax.annotation.Resource;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import dao.CommentDao;
import model.Comment;

@Repository("commentDaoImpl")
public class CommentDaoImpl implements CommentDao {
	
	@Resource
	private MongoTemplate mongoTemplate;
	
	public Comment findOneByID(String id) {
		Comment comment = mongoTemplate.findOne(
				new Query(Criteria.where("_id").is(new ObjectId(id))), Comment.class);
		if (comment == null) comment = new Comment();
		return comment;
	}
	
	public String insert(Comment comment) {
		mongoTemplate.insert(comment);
		return comment.get_id().toString();
	}
	
	public List<Comment> getSomeCommentsByTargetID(String targetID, int num){
		List<Comment> comments = mongoTemplate.find(
				new Query(Criteria.where("targetID").is(targetID))
				.with(new Sort(Direction.DESC, "time"))
				.limit(num), Comment.class);
		return comments;
	}
	
	/*GET and SET*/
	public MongoTemplate getMongoTemplate() {
		return mongoTemplate;
	}
	
	public void setMongoTemplate(MongoTemplate mongo) {
		mongoTemplate = mongo;
	}
}
