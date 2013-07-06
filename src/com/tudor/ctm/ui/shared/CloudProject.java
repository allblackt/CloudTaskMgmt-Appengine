package com.tudor.ctm.ui.shared;

import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.datanucleus.annotations.Unowned;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class CloudProject {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	private String name;
	@Unowned
	private UserData owner;
	@Persistent
	@Unowned
	private List<UserData> members;

	public CloudProject(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public UserData getOwner() {
		return owner;
	}

	public void setOwner(UserData owner) {
		this.owner = owner;
	}

	public List<UserData> getMembers() {
		return members;
	}

	public void setMembers(List<UserData> members) {
		this.members = members;
	}

	//Builder pattern
	public static class Builder {
		private Long id;
		private String name;
		private UserData owner;
		private List<UserData> members;

		public Builder id(Long id) {
			this.id = id;
			return this;
		}

		public Builder name(String name) {
			this.name = name;
			return this;
		}

		public Builder owner(UserData owner) {
			this.owner = owner;
			return this;
		}

		public Builder members(List<UserData> members) {
			this.members = members;
			return this;
		}

		public CloudProject build() {
			return new CloudProject(this);
		}
	}

	private CloudProject(Builder builder) {
		this.id = builder.id;
		this.name = builder.name;
		this.owner = builder.owner;
		this.members = builder.members;
	}
}
