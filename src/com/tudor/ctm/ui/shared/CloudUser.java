package com.tudor.ctm.ui.shared;

import java.io.Serializable;
import java.util.List;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Unique;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class CloudUser implements Serializable {

	private static final long serialVersionUID = 2943022501209232585L;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	private String email;
	private String name;
	@NotPersistent
	private String logoutURL;
	@NotPersistent
	private Boolean isLoggedIn;
	@Persistent
	@Extension(vendorName="datanucleus", key="gae.unindexed", value="true")
	private Boolean isAdmin;
	@Persistent
	@Extension(vendorName="datanucleus", key="gae.unindexed", value="true")
	private List<String> deviceKeys;

	public CloudUser() {
	}

	public CloudUser(String email, String logoutURL, Boolean isAdmin) {
		this.email = email;
		this.logoutURL = logoutURL;
		this.isLoggedIn = true;
		this.isAdmin = isAdmin;
	}

	public CloudUser(String email, String logoutURL, Boolean isLoggedIn,
			Boolean isAdmin) {
		this.email = email;
		this.logoutURL = logoutURL;
		this.isLoggedIn = isLoggedIn;
		this.isAdmin = isAdmin;
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

	public List<String> getDeviceKeys() {
		return deviceKeys;
	}

	public void setDeviceKeys(List<String> deviceKeys) {
		this.deviceKeys = deviceKeys;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
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

	
	
	@Override
	public String toString() {
		return "CloudUser [id=" + id + ", email=" + email + ", name=" + name
				+ ", logoutURL=" + logoutURL + ", isLoggedIn=" + isLoggedIn
				+ ", isAdmin=" + isAdmin + ", deviceKeys=" + deviceKeys + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		CloudUser other = (CloudUser) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public static class Builder {
		private Long id;
		private String email;
		private String name;
		private String logoutURL;
		private Boolean isLoggedIn;
		private Boolean isAdmin;
		private List<String> deviceKeys;

		public Builder id(Long id) {
			this.id = id;
			return this;
		}

		public Builder email(String email) {
			this.email = email;
			return this;
		}

		public Builder name(String name) {
			this.name = name;
			return this;
		}

		public Builder logoutURL(String logoutURL) {
			this.logoutURL = logoutURL;
			return this;
		}

		public Builder isLoggedIn(Boolean isLoggedIn) {
			this.isLoggedIn = isLoggedIn;
			return this;
		}

		public Builder isAdmin(Boolean isAdmin) {
			this.isAdmin = isAdmin;
			return this;
		}

		public Builder deviceKeys(List<String> deviceKeys) {
			this.deviceKeys = deviceKeys;
			return this;
		}

		public CloudUser build() {
			return new CloudUser(this);
		}
	}

	private CloudUser(Builder builder) {
		this.id = builder.id;
		this.email = builder.email;
		this.name = builder.name;
		this.logoutURL = builder.logoutURL;
		this.isLoggedIn = builder.isLoggedIn;
		this.isAdmin = builder.isAdmin;
		this.deviceKeys = builder.deviceKeys;
	}
}
