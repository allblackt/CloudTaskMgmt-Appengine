package com.tudor.ctm.endpoint;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.datanucleus.query.JDOCursorHelper;
import com.tudor.ctm.ui.shared.CloudUser;
import com.tudor.ctm.util.PMF;

@Api(name = "clouduserendpoint", namespace = @ApiNamespace(ownerDomain = "tudor.com", ownerName = "tudor.com", packagePath = "ctm.ui.shared"))
public class CloudUserEndpoint {

	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method and paging support.
	 *
	 * @return A CollectionResponse class containing the list of all entities
	 * persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listCloudUser")
	public CollectionResponse<CloudUser> listCloudUser(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		PersistenceManager mgr = null;
		Cursor cursor = null;
		List<CloudUser> execute = null;

		try {
			mgr = getPersistenceManager();
			Query query = mgr.newQuery(CloudUser.class);
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				HashMap<String, Object> extensionMap = new HashMap<String, Object>();
				extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
				query.setExtensions(extensionMap);
			}

			if (limit != null) {
				query.setRange(0, limit);
			}

			execute = (List<CloudUser>) query.execute();
			cursor = JDOCursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (CloudUser obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<CloudUser> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}
	
	@SuppressWarnings("unchecked")
	public List<CloudUser> getAllUsers() {
		PersistenceManager mgr = null;
		Cursor cursor = null;
		List<CloudUser> execute = null;
		try {
			mgr = getPersistenceManager();
			Query q = mgr.newQuery(CloudUser.class);
			execute = (List<CloudUser>) q.execute();
			execute = (List<CloudUser>) mgr.detachCopyAll(execute);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			mgr.close();
		}
		return execute;
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getCloudUser")
	public CloudUser getCloudUser(@Named("id") Long id) {
		PersistenceManager mgr = getPersistenceManager();
		CloudUser clouduser = null;
		try {
			clouduser = mgr.getObjectById(CloudUser.class, id);
		} finally {
			mgr.close();
		}
		return clouduser;
	}
	
	@SuppressWarnings("unchecked")
	public CloudUser getCloudUserByEmail(@Named("email") String email) {
		CloudUser clouduser = null;
		PersistenceManager mgr = getPersistenceManager();
		
		try {
			Query q = mgr.newQuery(CloudUser.class);
			q.setFilter("email == emailParam");
			q.declareParameters("String emailParam");
			List<CloudUser> results = (List<CloudUser>) q.execute(email);
			if(results.size() > 0 ) {
				results = (List<CloudUser>) mgr.detachCopyAll(results);
				clouduser = (CloudUser) results.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			mgr.close();
		}
		return clouduser;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param clouduser the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertCloudUser")
	public CloudUser insertCloudUser(CloudUser clouduser) {
		PersistenceManager mgr = getPersistenceManager();
		Transaction tx = mgr.currentTransaction();
		try {
			tx.begin();
			mgr.setDetachAllOnCommit(true);
			if ( clouduser.getId() != null && containsCloudUser(clouduser)) {
				throw new EntityExistsException("Object already exists");
			}
			mgr.makePersistent(clouduser);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			if(tx.isActive()) {
				tx.rollback();
			}
			mgr.close();
		}
		return clouduser;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param clouduser the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateCloudUser")
	public CloudUser updateCloudUser(CloudUser clouduser) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (!containsCloudUser(clouduser)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.makePersistent(clouduser);
		} finally {
			mgr.close();
		}
		return clouduser;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 * @return The deleted entity.
	 */
	@ApiMethod(name = "removeCloudUser")
	public CloudUser removeCloudUser(@Named("id") Long id) {
		PersistenceManager mgr = getPersistenceManager();
		CloudUser clouduser = null;
		try {
			clouduser = mgr.getObjectById(CloudUser.class, id);
			mgr.deletePersistent(clouduser);
		} finally {
			mgr.close();
		}
		return clouduser;
	}

	private boolean containsCloudUser(CloudUser clouduser) {
		PersistenceManager mgr = getPersistenceManager();
		boolean contains = true;
		try {
			mgr.getObjectById(CloudUser.class, clouduser.getId());
		} catch (javax.jdo.JDOObjectNotFoundException ex) {
			contains = false;
		} finally {
			mgr.close();
		}
		return contains;
	}

	private static PersistenceManager getPersistenceManager() {
		return PMF.get().getPersistenceManager();
	}

}
