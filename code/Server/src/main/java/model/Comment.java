package model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "comments")
public class Comment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String _id;
	@Field("userID")
	private String userID;
	@Field("type")
	private String type;
	@Field("targetID")
	private String targetID;
	@Field("contentID")
	private String contentID;
	@Field("text")
	private String text;
	@Field("soundID")
	private String soundID;
	
	public Comment(){}
	
	/*GET and SET*/
	public String get_id() {
		return _id;
	}
	
	public void set_id(String s) {
		this._id = s;
	}
	
	public String getUserID() {
		return userID;
	}
	
	public void setUserID(String s) {
		this.userID = s;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String s) {
		this.type = s;
	}
	
	public String getTargetID() {
		return targetID;
	}
	
	public void setTargetID(String s) {
		this.targetID = s;
	}
	
	public String getContentID() {
		return contentID;
	}
	
	public void setContentID(String s) {
		this.contentID = s;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String s) {
		this.text = s;
	}
	
	public String getSoundID() {
		return soundID;
	}
	
	public void setSoundID(String s) {
		this.soundID = s;
	}
}
