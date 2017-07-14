package model;

import java.io.Serializable;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "ARManager")
public class ARManager implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	private ObjectId id;
	@Field("ARObjectID")
	private String ARObjectID;
	@Field("texture")
	private String texture;
	@Field("MTLID")
	private String MTLID;
	
	public ARManager(){}
	
	/*GET and SET*/
	public ObjectId getId() {
		return id;
	}
	
	public void setId(ObjectId s) {
		this.id = s;
	}
	
	public String getARObectID() {
		return ARObjectID;
	}
	
	public void setARObjectID(String s) {
		ARObjectID = s;
	}
	
	public String getTexture() {
		return texture;
	}
	
	public void setTexture(String s) {
		texture = s;
	}
	
	public String getMTLID() {
		return MTLID;
	}
	
	public void setMTLID(String s) {
		MTLID = s;
	}
}
