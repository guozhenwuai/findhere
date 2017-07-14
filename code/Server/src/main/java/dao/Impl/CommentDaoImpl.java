package dao.Impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import dao.CommentDao;
import dao.FileDao;
import model.Comment;

@Repository("commentDaoImpl")
public class CommentDaoImpl implements CommentDao {
	
	@Resource
	private MongoTemplate mongoTemplate;
	@Resource
	private FileDao fileDao;
	
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
	
	public String update(String commentID, String text, Date time) {
		mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(commentID)), 
		new Update().set("text", text)
		.set("time", time)
		, Comment.class);
		return null;
	}
	
	public List<Comment> getSomeCommentsByTargetID(String targetID, int skipNum, int num){
		List<Comment> comments = mongoTemplate.find(
				new Query(Criteria.where("targetID").is(targetID))
				.with(new Sort(Direction.DESC, "time"))
				.skip(skipNum)
				.limit(num), Comment.class);
		return comments;
	}
	
	public boolean remove(String commentID, String userID) {
		int influenced = mongoTemplate.remove(new Query(Criteria
				.where("_id").is(commentID)
				.and("userID").is(userID))
				, Comment.class).getN();
		if(influenced == 0 ) return false;
		return true;
	}
	
	public boolean removeCascaded(String commentID, String userID) {
		Comment comment = mongoTemplate.findOne(new Query(Criteria
				.where("_id").is(commentID)
				.and("userID").is(userID))
				, Comment.class);
		if(comment == null ) return false;
		String type = comment.getType();
		String fileID = "";
		switch(type) {
		case("picture"):
			fileID = comment.getPictureID();
			break;
		case("sound"):
			fileID = comment.getSoundID();
			break;
		}
		
		int influenced = mongoTemplate.remove(new Query(Criteria
				.where("_id").is(commentID))
				, Comment.class).getN();
		if(influenced == 0 ) return false;
		
		if(fileID.length() != 0) {
			fileDao.removeFile(type, fileID);
		}
		return true;
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
