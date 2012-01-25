package edu.caltech.cs141b.hw2.gwt.collab.server;

import java.util.Date;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;

import edu.caltech.cs141b.hw2.gwt.collab.shared.DocumentMetadata;
import edu.caltech.cs141b.hw2.gwt.collab.shared.UnlockedDocument;

public class DocumentJDO {
	
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private String key;
	
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
	
	public DocumentJDO(String key, String title, String contents,
			String lockedBy, Date lockedUntil) {
		super();
		this.key = key;
		this.title = title;
		this.contents = contents;
		this.lockedBy = lockedBy;
		this.lockedUntil = lockedUntil;
	}

	public UnlockedDocument unlock() {
		lockedBy = null;
		lockedUntil = null;
		return new UnlockedDocument(key, title, contents);
	}
	
	public DocumentMetadata getDocumentMetdataObject() {
		return new DocumentMetadata(key, title);
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
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
