package edu.caltech.cs141b.hw2.gwt.collab.server;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;
import javax.management.timer.Timer;

import edu.caltech.cs141b.hw2.gwt.collab.shared.DocumentMetadata;
import edu.caltech.cs141b.hw2.gwt.collab.shared.LockExpired;
import edu.caltech.cs141b.hw2.gwt.collab.shared.LockedDocument;
import edu.caltech.cs141b.hw2.gwt.collab.shared.UnlockedDocument;

/**
 * Used to contain the entire document as a JDO data object to be stored in a datastore
 */
@PersistenceCapable
public class DocumentJDO {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	@Persistent
	private String title = null;
	
	@Persistent
	private String contents = null;
	
	@Persistent
	private String lockedBy = null;
	
	@Persistent
	private Date lockedUntil = null;
	
	/**
	 * Constructor for DocumentJDO object
	 * 
	 * @param  title : name of document
	 * 		   contents : contents of document
	 * 	       lockedBy : owner of document (Currently an IP address)
	 * 		   lockedUntil : Date containing date of when document expires
	 */
	public DocumentJDO(String title, String contents,
			String lockedBy, Date lockedUntil) {
		super();
		// Set parameters to arguments
		this.title = title;
		this.contents = contents;
		this.lockedBy = lockedBy;
		this.lockedUntil = lockedUntil;
	}

	/**
	 * Used to create an UnlockedDocument object. This is just an unlocked version of itself
	 * 
	 * @param  None
	 * @return UnlockedDocument which is the read-only version of the document
	 */
	public UnlockedDocument getUnlockedDocumentVersion() {
		return new UnlockedDocument(getKey(), title, contents);
	}
	
	/**
	 * Used to unlock the document.
	 * 
	 * @param  None
	 * @return None
	 */
	public void unlock() {
		// Reset ownership and expiration date to null
		lockedBy = null;
		lockedUntil = null;
	}
	
	/**
	 * Used to lock the document.
	 * 
	 * @param  client : current owner of document
	 * @return LockedDocument object that is a locked version of itself
	 */
	public LockedDocument lock(String client) {
		// Set owner to the client that calls this function
		lockedBy = client;
		
		// Set expiration to be current date + 1 minute
		Date now = new Date();
		lockedUntil = new Date(now.getTime() + 60000);
		return new LockedDocument(lockedBy, lockedUntil, getKey(), title, contents);
	}
	
	/**
	 * Used to get the serialized document metadata.
	 * 
	 * @param  None
	 * @return DocumentMetadata object containing the metadata
	 */
	public DocumentMetadata getDocumentMetdataObject() {
		return new DocumentMetadata(getKey(), title);
	}
	
	/**
	 * Used to access the document's key
	 * 
	 * @param  None
	 * @return String containing the key
	 */
	public String getKey() {
		return KeyFactory.keyToString(key);
	}

	/**
	 * Used to access the document's title
	 * 
	 * @param  None
	 * @return String containing the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Used to modify the document's title
	 * 
	 * @param  title : contains new title of document
	 * @return None
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Used to access the document's contents
	 * 
	 * @param  None
	 * @return String containing the contents
	 */
	public String getContents() {
		return contents;
	}

	/**
	 * Used to modify the document's contents
	 * 
	 * @param  contents : contains new contents of document
	 * @return None
	 */
	public void setContents(String contents) {
		this.contents = contents;
	}

	/**
	 * Used to access the document's owner
	 * 
	 * @param  None
	 * @return String containing the owner
	 */
	public String getLockedBy() {
		return lockedBy;
	}

	/**
	 * Used to modify the document's owner
	 * 
	 * @param  lockedBy : contains new owner document
	 * @return None
	 */
	public void setLockedBy(String lockedBy) {
		this.lockedBy = lockedBy;
	}

	/**
	 * Used to access the document's expiration date
	 * 
	 * @param  None
	 * @return Date containing the expiration date
	 */
	public Date getLockedUntil() {
		return lockedUntil;
	}

	/**
	 * Used to modify the document's expiration date
	 * 
	 * @param  lockedUntil : contains new expiration date of document
	 * @return None
	 */
	public void setLockedUntil(Date lockedUntil) {
		this.lockedUntil = lockedUntil;
	}
}
