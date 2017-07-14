package model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "userTarget")
public class UserTarget {
	private static final long serialVersionUID = 1L;
	
	@Id
	private String userID;
	@Field("targetIDs")
	private List<String> targetIDs;
	
	public UserTarget(){}
	
	/*GET and SET*/
	public String getUserID() {
		return userID;
	}
	
	public void setUserID(String s) {
		userID = s;
	}
	
	public List<String> getTargetIDs(){
		return targetIDs;
	}
	
	public void setTargetIDs(List<String> s) {
		targetIDs = s;
	}
}
