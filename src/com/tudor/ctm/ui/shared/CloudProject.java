package com.tudor.ctm.ui.shared;

import java.io.Serializable;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Unique;

import com.google.appengine.datanucleus.annotations.Unowned;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")
public class CloudProject implements Serializable {

	private static final long serialVersionUID = 3316395777640173979L;
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	private String name;
	@Persistent(defaultFetchGroup = "true")
	@Unowned
	private CloudUser owner;
	@Persistent(defaultFetchGroup = "true")
	@Unowned
	private List<CloudUser> members;

	public CloudProject() {
	}

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

	public CloudUser getOwner() {
		return owner;
	}

	public void setOwner(CloudUser owner) {
		this.owner = owner;
	}

	public List<CloudUser> getMembers() {
		return members;
	}

	public void setMembers(List<CloudUser> members) {
		this.members = members;
	}
	
	@Override
	public String toString() {
		try {
		return "CloudProject [id=" + id + ", name=" + name + ", owner=" + owner.toString()
				+ ", members=" + members.toString() + "]";
		} catch (Exception e) {
			e.printStackTrace();
			return "CloudProject has an error...";
		}
	}
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CloudProject other = (CloudProject) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}



	//Builder pattern
	public static class Builder {
		private Long id;
		private String name;
		private CloudUser owner;
		private List<CloudUser> members;

		public Builder id(Long id) {
			this.id = id;
			return this;
		}

		public Builder name(String name) {
			this.name = name;
			return this;
		}

		public Builder owner(CloudUser owner) {
			this.owner = owner;
			return this;
		}

		public Builder members(List<CloudUser> members) {
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
