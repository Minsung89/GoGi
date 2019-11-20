package com.example.front_village.gogi.Entity;

public class UserInfo {


	private String userId;

	private String userPw;

	private String userNm;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserPw() {
		return userPw;
	}

	public void setUserPw(String userPw) {
		this.userPw = userPw;
	}

	public String getUserNm() {
		return userNm;
	}

	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}

	@Override
	public String toString() {
		return "UserInfo [userId=" + userId + ", userPw=" + userPw + ", userNm=" + userNm + "]";
	}
	
}
