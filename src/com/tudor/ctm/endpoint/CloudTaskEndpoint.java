package com.tudor.ctm.endpoint;

import java.util.Date;
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
import com.tudor.ctm.ui.shared.CloudTask;
import com.tudor.ctm.util.PMF;

@Api(name = "cloudtaskendpoint", namespace = @ApiNamespace(ownerDomain = "tudor.com", ownerName = "tudor.com", packagePath = "ctm.ui.shared"))
public class CloudTaskEndpoint {

	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method and paging support.
	 *
	 * @return A CollectionResponse class containing the list of all entities
	 * persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listCloudTask")
	public CollectionResponse<CloudTask> listCloudTask(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		PersistenceManager mgr = null;
		Cursor cursor = null;
		List<CloudTask> execute = null;

		try {
			mgr = getPersistenceManager();
			Query query = mgr.newQuery(CloudTask.class);
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				HashMap<String, Object> extensionMap = new HashMap<String, Object>();
				extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
				query.setExtensions(extensionMap);
			}

			if (limit != null) {
				query.setRange(0, limit);
			}

			execute = (List<CloudTask>) query.execute();
			cursor = JDOCursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (CloudTask obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<CloudTask> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getCloudTask")
	public CloudTask getCloudTask(@Named("id") Long id) {
		PersistenceManager mgr = getPersistenceManager();
		CloudTask cloudtask = null;
		try {
			cloudtask = mgr.getObjectById(CloudTask.class, id);
		} finally {
			mgr.close();
		}
		return cloudtask;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param cloudtask the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertCloudTask")
	public CloudTask insertCloudTask(CloudTask cloudtask) {
		CloudTask ct = null;
		PersistenceManager mgr = getPersistenceManager();
		Transaction tx = mgr.currentTransaction();
		try {
			if (cloudtask.getId()!=null && containsCloudTask(cloudtask)) {
				throw new EntityExistsException("Object already exists");
			}
			tx.begin();
			mgr.setDetachAllOnCommit(true);
			mgr.makePersistent(cloudtask);
			tx.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} 
		finally {
			if (tx.isActive())
		    {
		        tx.rollback();
		    }
		}
		return cloudtask;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param cloudtask the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateCloudTask")
	public CloudTask updateCloudTask(CloudTask cloudtask) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (!containsCloudTask(cloudtask)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.makePersistent(cloudtask);
		} finally {
			mgr.close();
		}
		return cloudtask;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 * @return The deleted entity.
	 */
	@ApiMethod(name = "removeCloudTask")
	public CloudTask removeCloudTask(@Named("id") Long id) {
		PersistenceManager mgr = getPersistenceManager();
		CloudTask cloudtask = null;
		try {
			cloudtask = mgr.getObjectById(CloudTask.class, id);
			mgr.deletePersistent(cloudtask);
		} finally {
			mgr.close();
		}
		return cloudtask;
	}

	private boolean containsCloudTask(CloudTask cloudtask) {
		PersistenceManager mgr = getPersistenceManager();
		boolean contains = true;
		try {
			mgr.getObjectById(CloudTask.class, cloudtask.getId());
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
