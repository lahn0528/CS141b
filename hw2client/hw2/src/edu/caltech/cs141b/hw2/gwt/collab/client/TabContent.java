package edu.caltech.cs141b.hw2.gwt.collab.client;

import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import edu.caltech.cs141b.hw2.gwt.collab.shared.LockedDocument;
import edu.caltech.cs141b.hw2.gwt.collab.shared.UnlockedDocument;

public class TabContent {
	
	// Store the state of the document and widget for a particular tab.
	private UnlockedDocument readOnlyDoc;
	private LockedDocument lockedDoc;
	private TextBox title;
	private RichTextArea contents;
	
	// Use boolean value to indicate if the buttons are enabled or not.
	private Boolean refreshDoc;
	private Boolean lockButton;
	private Boolean saveButton;
	
	// Identifier for a tab.
	private String key; 
	
	private VerticalPanel vp;
	
	public TabContent(UnlockedDocument readOnlyDoc, LockedDocument lockedDoc) {
		super();
		this.readOnlyDoc = readOnlyDoc;
		this.lockedDoc = lockedDoc;
		
		if (readOnlyDoc != null)
			key = readOnlyDoc.getKey();
		else if (lockedDoc != null)
			key = lockedDoc.getKey();
		
		createWidgetHelper();
	}

	/*
	 * An helper function to create widget inside of a tab.
	 */
	private void createWidgetHelper() {
		vp = new VerticalPanel();
		vp.setSize("900px", "0cm");
		vp.setSpacing(10);
		title = new TextBox();
		title.setWidth("100%");
		vp.add(title);
		contents = new RichTextArea();
		contents.setWidth("100%");
		vp.add(contents);
	}

	/*
	 * Setter and getter methods
	 */
	
	public Boolean getRefreshDoc() {
		return refreshDoc;
	}
	
	public void setRefreshDoc(Boolean refreshDoc) {
		this.refreshDoc = refreshDoc;
	}

	public Boolean getLockButton() {
		return lockButton;
	}

	public void setLockButton(Boolean lockButton) {
		this.lockButton = lockButton;
	}

	public Boolean getSaveButton() {
		return saveButton;
	}

	public void setSaveButton(Boolean saveButton) {
		this.saveButton = saveButton;
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public VerticalPanel getVp() {
		return vp;
	}

	public void setVp(VerticalPanel vp) {
		this.vp = vp;
	}

	public UnlockedDocument getReadOnlyDoc() {
		return readOnlyDoc;
	}

	public void setReadOnlyDoc(UnlockedDocument readOnlyDoc) {
		this.readOnlyDoc = readOnlyDoc;
	}

	public LockedDocument getLockedDoc() {
		return lockedDoc;
	}

	public void setLockedDoc(LockedDocument lockedDoc) {
		this.lockedDoc = lockedDoc;
	}

	public TextBox getTitle() {
		return title;
	}

	public void setTitle(TextBox title) {
		this.title = title;
	}

	public RichTextArea getContents() {
		return contents;
	}

	public void setContents(RichTextArea contents) {
		this.contents = contents;
	}
	
}
