package edu.caltech.cs141b.hw2.gwt.collab.server;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;

import java.util.ArrayList;
import java.util.Date;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

import edu.caltech.cs141b.hw2.gwt.collab.shared.DocumentMetadata;
import edu.caltech.cs141b.hw2.gwt.collab.shared.LockedDocument;
import edu.caltech.cs141b.hw2.gwt.collab.shared.UnlockedDocument;

/**
 * Used to contain the entire document as a JDO data object to be stored in a datastore
 */
@PersistenceCapable
public class DocumentInfo {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	@Persistent
	private String title = null;
	
	@Persistent
	private Text contents = null;
	
	@Persistent
	private String lockedBy = null;
	
	@Persistent
	private Date lockedUntil = null;
	
	private ArrayList<String> queue = null; 
	
	/**
	 * Constructor for DocumentJDO object
	 * 
	 * @param  title : name of document
	 * 		   contents : contents of document
	 * 	       lockedBy : owner of document (Currently an User object)
	 * 		   lockedUntil : Date containing date of when document expires
	 */
	public DocumentInfo(String title, String contents,
			String lockedBy, Date lockedUntil) {
		super();
		// Set parameters to arguments
		this.title = title;
		this.contents = new Text(contents);
		this.lockedBy = lockedBy;
		this.lockedUntil = lockedUntil;
		queue = new ArrayList<String>();
	}

	/**
	 * Create an UnlockedDocument object. This is just an unlocked version of itself
	 * 
	 * @param  None
	 * @return UnlockedDocument which is the read-only version of the document
	 */
	public UnlockedDocument getUnlockedDocumentVersion() {
		return new UnlockedDocument(getKey(), title, contents.getValue());
	}
	
	/**
	 * Unlock the document.
	 */
	public void unlock() {
		// Reset ownership and expiration date to null
		lockedBy = null;
		lockedUntil = null;
	}
	
	/**
	 * Lock the document.
	 * 
	 * @param  client : current owner of document
	 * @return LockedDocument object that is a locked version of itself
	 */
	public LockedDocument lock(String client) {
		// Set owner to the client that calls this function
		lockedBy = client;
		
		return new LockedDocument(lockedBy, lockedUntil, getKey(), title, contents.getValue());
	}
	
	/**
	 * Get the serialized document metadata.
	 * 
	 * @return DocumentMetadata object containing the metadata
	 */
	public DocumentMetadata getDocumentMetdataObject() {
		return new DocumentMetadata(getKey(), title);
	}
	
	/**
	 * Access the document's key
	 * 
	 * @return String containing the key
	 */
	public String getKey() {
		System.out.println("key: " + key);
		return KeyFactory.keyToString(key);
	}

	/**
	 * Access the document's title
	 * 
	 * @return String containing the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Modify the document's title
	 * 
	 * @param  title : contains new title of document
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Access the document's contents
	 * 
	 * @return String containing the contents
	 */
	public String getContents() {
		return contents.getValue();
	}

	/**
	 * Modify the document's contents
	 * 
	 * @param  contents : contains new contents of document
	 */
	public void setContents(String contents) {
		this.contents =  new Text(contents);
	}

	/**
	 * Access the document's owner
	 * 
	 * @return String containing the owner
	 */
	public String getLockedBy() {
		return lockedBy;
	}

	/**
	 * Modify the document's owner
	 * 
	 * @param  lockedBy : contains new owner document
	 */
	public void setLockedBy(String lockedBy) {
		this.lockedBy = lockedBy;
	}

	/**
	 * Used to access the document's expiration date
	 * 
	 * @return Date containing the expiration date
	 */
	public Date getLockedUntil() {
		return lockedUntil;
	}

	/**
	 * Modify the document's expiration date
	 * 
	 * @param  lockedUntil : contains new expiration date of document
	 */
	public void setLockedUntil(Date lockedUntil) {
		this.lockedUntil = lockedUntil;
	}

	/**
	 * Get next user from document's queue
	 * 
	 * @return User containing the next owner
	 */
	public String getNextUser() {
		if (queue.size() != 0)
			return queue.get(0);
		return null;
	}

	/**
	 * Add user to document's queue
	 * 
	 * @param  user : contains info about new user
	 */
	public void addUser(String user) {
		queue.add(user);
	}
	
	/**
	 * Remove current user from front of document's queue
	 */
	public void removeFirstUser() {
		if (queue.size() >= 1)
			queue.remove(0);
	}
	
	/**
	 * Remove specified user from document's queue
	 * 
	 * @param  user : contains info about desired user
	 */
	public void removeUser(String user) {
		queue.remove(user);
	}
	
	/**
	 * Checks if document's queue contains specified user
	 * 
	 * @param  user : contains info about desired user
	 * @return boolean value
	 */
	public boolean containsUser(String user) {
		return queue.contains(user);
	}

	/**
	 * Checks if document's queue is empty
	 * 
	 * @return boolean value
	 */
	public boolean isQueueEmpty() {
		return queue.isEmpty();
	}
	
	/**
	 * Find size of document queue
	 * 
	 * @return int containing size of queue
	 */
	public int getQueueSize() {
		return queue.size();
	}
}