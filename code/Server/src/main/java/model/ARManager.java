package model;

import java.io.Serializable;
import java.util.Map;

import org.bson.types.ObjectId;
import org.json.JSONObject;
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
	private Map<String, String> texture;
	@Field("MTLID")
	private Map<String, String> MTLID;
	@Field("position")
	private JSONObject position;
	
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
	
	public Map<String, String> getTexture() {
		return texture;
	}
	
	public void setTexture(Map<String, String> s) {
		texture = s;
	}
	
	public Map<String, String> getMTLID() {
		return MTLID;
	}
	
	public void setMTLID(Map<String, String> s) {
		MTLID = s;
	}
	
	public JSONObject getPosition() {
		return position;
	}
	
	public void setPosition(JSONObject s) {
		position = s;
	}
}
