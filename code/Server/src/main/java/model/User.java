package model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection="users")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	private String userID;//email
	@Field("name")
	private String name;
	@Field("password")
	private String password;
	@Field("gender")
	private String gender;
	@Field("headPortraitID")
	private String headPortraitID;
	@Field("qq")
	private String qq;
	@Field("weixin")
	private String weixin;
	@Field("isMember")
	private int isMember;
	@Field("Admin")
	private int Admin;
	
	public User() {
		isMember = 0;
		Admin = 0;
		name = "";
		gender = "";
		headPortraitID = "";
		qq = "";
		weixin = "";
	}
	
	/*GET and SET*/
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

	public String getPassword() {
		return password;
	}
	
	public void setPassword(String s) {
		this.password = s;
	}
	
	public String getGender() {
		return gender;
	}
	
	public void setGender(String s) {
		this.gender = s;
	}
	
	public String getHeadPortraitID() {
		return headPortraitID;
	}
	
	public void setHeadPortraitID(String s) {
		this.headPortraitID = s;
	}

	public String getQq() {
		return qq;
	}
	
	public void setQq(String s) {
		this.qq = s;
	}

	public String getWeixin() {
		return weixin;
	}
	
	public void setWeixin(String s) {
		this.weixin = s;
	}
	
	public int getIsMember() {
		return isMember;
	}
	
	public void setIsMember(int s) {
		this.isMember = s;
	}
	
	public int getAdmin() {
		return Admin;
	}
	
	public void setAdmin(int s) {
		this.Admin = s;
	}
}
