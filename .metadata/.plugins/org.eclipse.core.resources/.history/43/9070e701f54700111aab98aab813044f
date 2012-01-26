package edu.caltech.cs141b.hw2.gwt.collab.server;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import java.util.Date;
import java.util.logging.Logger;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;
import javax.management.timer.Timer;

import edu.caltech.cs141b.hw2.gwt.collab.shared.DocumentMetadata;
import edu.caltech.cs141b.hw2.gwt.collab.shared.LockedDocument;
import edu.caltech.cs141b.hw2.gwt.collab.shared.UnlockedDocument;

@PersistenceCapable
public class DocumentJDO {
	private static final Logger log = Logger.getLogger(CollaboratorServiceImpl.class.toString());
	
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
	
	

	public DocumentJDO(String title, String contents,
			String lockedBy, Date lockedUntil) {
		super();
		this.title = title;
		this.contents = contents;
		this.lockedBy = lockedBy;
		this.lockedUntil = lockedUntil;
	}

	public UnlockedDocument unlock() {
		lockedBy = null;
		lockedUntil = null;
		return new UnlockedDocument(getKey(), title, contents);
	}
	
	public LockedDocument lock(String client) {
		lockedBy = client;
		Date now = new Date();
		lockedUntil = new Date(now.getTime() + 10000);
		return new LockedDocument(lockedBy, lockedUntil, getKey(), title, contents);
		
	}
	
	public DocumentMetadata getDocumentMetdataObject() {
		return new DocumentMetadata(getKey(), title);
	}
	
	public String getKey() {
		System.out.println("key: " + key);
		return KeyFactory.keyToString(key);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public String getLockedBy() {
		return lockedBy;
	}

	public void setLockedBy(String lockedBy) {
		this.lockedBy = lockedBy;
	}

	public Date getLockedUntil() {
		return lockedUntil;
	}

	public void setLockedUntil(Date lockedUntil) {
		this.lockedUntil = lockedUntil;
	}

}
