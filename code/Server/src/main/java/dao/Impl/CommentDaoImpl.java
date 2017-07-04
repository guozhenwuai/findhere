package dao.Impl;

import javax.annotation.Resource;

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
		Comment comment = mongoTemplate.findOne(new Query(Criteria.where("_id").is(id)), Comment.class);
		if (comment == null) comment = new Comment();
		return comment;
	}
}
