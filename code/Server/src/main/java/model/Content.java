package model;

import java.io.Serializable;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "contents")
public class Content implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	private ObjectId id;
	@Field("targetID")
	private String targetID;
	@Field("imageID")
	private String imageID;
	@Field("audioID")
	private String audioID;
	@Field("text")
	private String text;
	@Field("ARObjectID")
	private String ARManagerID;
	@Field("type")
	private String type;
	
	public Content() {}
	
	/*GET and SET*/
	public ObjectId getId() {
		return id;
	}
	
	public void setId(ObjectId s) {
		this.id = s;
	}
	
	public String getTargetID() {
		return targetID;
	}
	
	public void setTargetID(String s) {
		this.targetID = s;
	}
	
	public String getImageID() {
		return imageID;
	}
	
	public void setImageID(String s) {
		this.imageID = s;
	}
	
	public String getAudioID() {
		return audioID;
	}
	
	public void setAudioID(String s) {
		audioID = s;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String s) {
		text = s;
	}
	
	public String getARManagerID() {
		return ARManagerID;
	}
	
	public void setARManagerID(String s) {
		ARManagerID = s;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String s) {
		type = s;
	}
}
