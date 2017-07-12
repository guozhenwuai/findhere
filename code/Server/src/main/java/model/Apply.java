package model;

import java.io.Serializable;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "apply")
public class Apply implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	private ObjectId id;
	@Field("userID")
	private String userID;
	@Field("name")
	private String name;
	@Field("inc")
	private String inc;
	@Field("nationalID")
	private String nationalID;
	
	public Apply() {}
	
	/*GET and SET*/
	public ObjectId getId() {
		return id;
	}
	
	public void setId(ObjectId s) {
		this.id = s;
	}
	
	public String getUserID() {
		return userID;
	}
	
	public void setUserID(String s) {
		this.userID = s;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String s) {
		this.name = s;
	}
	
	public String getInc() {
		return inc;
	}
	
	public void setInc(String s) {
		this.inc = s;
	}
	
	public String getNationalID() {
		return nationalID;
	}
	
	public void setNationalID(String s) {
		this.nationalID = s;
	}
}
