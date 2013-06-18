package com.tudor.ctm.endpoint;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.datanucleus.query.JDOCursorHelper;
import com.tudor.ctm.ui.shared.CTMUser;
import com.tudor.ctm.util.PMF;
 
@Api(name = "ctmuserendpoint")
public class CTMUserEndpoint {
	
	private static final Logger log = Logger.getLogger(CTMUserEndpoint.class.getName());

	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method and paging support.
	 *
	 * @return A CollectionResponse class containing the list of all entities
	 * persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listCTMUser", path="ctmuser/all")
	public CollectionResponse<CTMUser> listCTMUser(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		PersistenceManager mgr = null;
		Cursor cursor = null;
		List<CTMUser> execute = null;

		try {
			mgr = getPersistenceManager();
			Query query = mgr.newQuery(CTMUser.class);
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				HashMap<String, Object> extensionMap = new HashMap<String, Object>();
				extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
				query.setExtensions(extensionMap);
			}

			if (limit != null) {
				query.setRange(0, limit);
			}

			execute = (List<CTMUser>) query.execute();
			cursor = JDOCursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (CTMUser obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<CTMUser> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}

	@SuppressWarnings("unchecked")
	@ApiMethod(name="getCTMUserByEmail", httpMethod="GET", path="ctmuser/email/{email}")
	public CTMUser getCTMUserByEmail(@Named("email") String email){
		
		log.info("Entered.");
		
		PersistenceManager mgr = getPersistenceManager();
		CTMUser ctmuser = null;
		try {
			Query q = mgr.newQuery(CTMUser.class);
			q.setFilter("email == emailParam");
			q.declareParameters("String emailParam");
			ctmuser = ((List<CTMUser>) q.execute(email)).get(0);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			mgr.close();
			log.info("Exited.");
		}
		return ctmuser;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param ctmuser the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertCTMUser")
	public CTMUser insertCTMUser(CTMUser ctmuser) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (containsCTMUser(ctmuser)) {
				throw new EntityExistsException("Object already exists");
			}
			mgr.makePersistent(ctmuser);
		} finally {
			mgr.close();
		}
		return ctmuser;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param ctmuser the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateCTMUser")
	public CTMUser updateCTMUser(CTMUser ctmuser) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (!containsCTMUser(ctmuser)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.makePersistent(ctmuser);
		} finally {
			mgr.close();
		}
		return ctmuser;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 * @return The deleted entity.
	 */
	@ApiMethod(name = "removeCTMUser")
	public CTMUser removeCTMUser(@Named("id") Long id) {
		PersistenceManager mgr = getPersistenceManager();
		CTMUser ctmuser = null;
		try {
			ctmuser = mgr.getObjectById(CTMUser.class, id);
			mgr.deletePersistent(ctmuser);
		} finally {
			mgr.close();
		}
		return ctmuser;
	}

	private boolean containsCTMUser(CTMUser ctmuser) {
		PersistenceManager mgr = getPersistenceManager();
		boolean contains = true;
		try {
			mgr.getObjectById(CTMUser.class, ctmuser.getId());
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
