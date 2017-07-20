package model;

import java.util.ArrayList;
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
	@Field("tempTargetIDs")
	private List<String> tempTargetIDs;
	@Field("hasTempTarget")
	private boolean hasTempTarget;
	
	public UserTarget(){
		targetIDs = new ArrayList<String>();
		tempTargetIDs = new ArrayList<String>();
		hasTempTarget = false;
	}
	
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
	
	public List<String> getTempTargetIDs(){
		return tempTargetIDs;
	}
	
	public void setTempTargetIDs(List<String> s) {
		tempTargetIDs = s;
	}
	
	public boolean getHasTempTarget(){
		return hasTempTarget;
	}
	
	public void setHasTempTarget(boolean s) {
		hasTempTarget = s;
	}
}
