package com.tudor.ctm.endpoint;

import com.tudor.ctm.ui.shared.CloudProject;
import com.tudor.ctm.ui.shared.CloudUser;
import com.tudor.ctm.util.PMF;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.datanucleus.query.JDOCursorHelper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

@Api(name = "cloudprojectendpoint" )
public class CloudProjectEndpoint {

	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method and paging support.
	 *
	 * @return A CollectionResponse class containing the list of all entities
	 * persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listCloudProject")
	public CollectionResponse<CloudProject> listCloudProject(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		PersistenceManager mgr = null;
		Cursor cursor = null;
		List<CloudProject> execute = null;

		try {
			mgr = getPersistenceManager();
			Query query = mgr.newQuery(CloudProject.class);
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				HashMap<String, Object> extensionMap = new HashMap<String, Object>();
				extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
				query.setExtensions(extensionMap);
			}

			if (limit != null) {
				query.setRange(0, limit);
			}

			execute = (List<CloudProject>) query.execute();
			execute = (List<CloudProject>) mgr.detachCopyAll(execute);
			cursor = JDOCursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (CloudProject obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<CloudProject> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getCloudProject", path="getcloudproject", httpMethod=HttpMethod.GET)
	public CloudProject getCloudProject(@Named("id") Long id) {
		PersistenceManager mgr = getPersistenceManager();
		CloudProject cloudproject = null;
		try {
			cloudproject = mgr.getObjectById(CloudProject.class, id);
		} finally {
			mgr.close();
		}
		return cloudproject;
	}
	
	@SuppressWarnings("unchecked")
	@ApiMethod(name="getuserprojects", path="getuserprojects", httpMethod=HttpMethod.GET)
	public List<CloudProject> getUserProjects(CloudUser user) {
		List<CloudProject> projects = null;
		PersistenceManager mgr = null;
		try {
			System.out.println(user.toString());
			mgr = getPersistenceManager();
			Query q = mgr.newQuery(CloudProject.class);
			q.setFilter("members.contains(usr) && usr.id==:userId");
			q.declareVariables(CloudUser.class.getName() +  " usr");
			projects = (List<CloudProject>) q.execute(user.getId());
			projects = (List<CloudProject>) mgr.detachCopyAll(projects);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			mgr.close();
		}
		return projects;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param cloudproject the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertCloudProject", path="insertcloudproject", httpMethod=HttpMethod.POST)
	public CloudProject insertCloudProject(CloudProject cloudproject) {
		PersistenceManager mgr = getPersistenceManager();
		Transaction tx = mgr.currentTransaction();
		try {
			tx.begin();
			mgr.setDetachAllOnCommit(true);
			if (cloudproject.getId() != null && containsCloudProject(cloudproject)) {
				throw new EntityExistsException("Object already exists");
			}
			mgr.makePersistent(cloudproject);
			tx.commit();
		} finally {
			if(tx.isActive()) {
				tx.rollback();
			}
			mgr.close();
		}
		return cloudproject;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param cloudproject the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateCloudProject", path="updatecloudproject", httpMethod=HttpMethod.POST)
	public CloudProject updateCloudProject(CloudProject cloudproject) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (!containsCloudProject(cloudproject)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.makePersistent(cloudproject);
		} finally {
			mgr.close();
		}
		return cloudproject;
	}

	private boolean containsCloudProject(CloudProject cloudproject) {
		PersistenceManager mgr = getPersistenceManager();
		boolean contains = true;
		try {
			mgr.getObjectById(CloudProject.class, cloudproject.getId());
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
