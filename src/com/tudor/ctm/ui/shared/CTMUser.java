package com.tudor.ctm.ui.shared;

import java.io.Serializable;

public class CTMUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6908509448262318196L;
	
	private String email;
	private String name;
	
	public CTMUser(String email, String name) {
		this.email = email;
		this.name = name;
	}
	
	public CTMUser() { }
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "CTMUser [email=" + email + ", name=" + name + "]";
	}
	
}
