package com.tudor.ctm.ui.shared;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;


@PersistenceCapable (identityType=IdentityType.APPLICATION)
public class CTMUser implements Serializable {

	private static final long serialVersionUID = 6908509448262318196L;
	
	@PrimaryKey
	@Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
	private Key id;
	
	private String email;
	private String name;
	
	public CTMUser(String email, String name) {
		this.email = email;
		this.name = name;
	}
	
	public CTMUser() { }
	
	public Key getId() {
		return id;
	}

	public void setId(Key id) {
		this.id = id;
	}

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
