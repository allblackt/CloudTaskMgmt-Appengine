package com.tudor.ctm.ui.shared;

import java.io.Serializable;

public class UserData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2943022501209232585L;
	
	private String email;
	private String logoutURL;
	private Boolean isLoggedIn;
	private Boolean isAdmin;

	public UserData(String email, String logoutURL, Boolean isAdmin) {
		this.email = email;
		this.logoutURL = logoutURL;
		this.isLoggedIn = true;
		this.isAdmin = isAdmin;
	}
	
	public UserData(String email, String logoutURL, Boolean isLoggedIn, Boolean isAdmin) {
		this.email = email;
		this.logoutURL = logoutURL;
		this.isLoggedIn = isLoggedIn;
		this.isAdmin = isAdmin;
	}
	
	public UserData() {
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getLogoutURL() {
		return logoutURL;
	}
	public void setLogoutURL(String logoutURL) {
		this.logoutURL = logoutURL;
	}

	public Boolean getIsLoggedIn() {
		return isLoggedIn;
	}

	public void setIsLoggedIn(Boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}

	public Boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
}
