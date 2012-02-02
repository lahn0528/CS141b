package edu.caltech.cs141b.hw2.gwt.collab.client;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.caltech.cs141b.hw2.gwt.collab.shared.LockedDocument;
import edu.caltech.cs141b.hw2.gwt.collab.shared.UnlockedDocument;
import com.google.gwt.user.client.ui.TabPanel;

/**
 * Main class for a single Collaborator widget.
 */
public class Collaborator extends Composite implements ClickHandler, ChangeHandler {
	
	protected CollaboratorServiceAsync collabService;
	
	// Track document information.
	protected UnlockedDocument readOnlyDoc = null;
	protected LockedDocument lockedDoc = null;
	protected ArrayList<TabContent> tabDocuments = new ArrayList<TabContent>();
	
	// Managing available documents.
	protected ListBox documentList = new ListBox();
	private Button refreshList = new Button("Refresh Document List");
	private Button createNew = new Button("Create New Document");
	
	// For displaying document information and editing document content.
	protected TextBox title = new TextBox();
	protected RichTextArea contents = new RichTextArea();
	protected Button refreshDoc = new Button("Refresh Document");
	protected Button lockButton = new Button("Get Document Lock");
	protected Button saveButton = new Button("Save Document");
	
	// Callback objects.
	protected DocLister lister = new DocLister(this);
	protected DocReader reader = new DocReader(this);
	private DocLocker locker = new DocLocker(this);
	protected DocReleaser releaser = new DocReleaser(this);
	private DocSaver saver = new DocSaver(this);
	protected String waitingKey = null;
	
	protected TabPanel tabPanel = new TabPanel();
	
	// Status tracking.
	private VerticalPanel statusArea = new VerticalPanel();
	private VerticalPanel vp_1;
	private HorizontalPanel hp_1;
	private VerticalPanel outerVp_1;
	
	/**
	 * UI initialization.
	 * 
	 * @param collabService
	 */
	public Collaborator(CollaboratorServiceAsync collabService) {
		this.collabService = collabService;
		HorizontalPanel outerHp = new HorizontalPanel();
		outerHp.setWidth("100%");
		VerticalPanel outerVp;
		outerVp_1 = new VerticalPanel();
		outerVp_1.setSpacing(20);
		
		VerticalPanel vp = new VerticalPanel();
		vp.setSpacing(10);
		vp.add(new HTML("<h2>Available Documents</h2>"));
		documentList.setWidth("100%");
		vp.add(documentList);
		HorizontalPanel hp = new HorizontalPanel();
		hp.setSpacing(10);
		hp.add(refreshList);
		hp.add(createNew);
		vp.add(hp);
		DecoratorPanel dp = new DecoratorPanel();
		dp.setWidth("100%");
		dp.add(vp);
		outerVp_1.add(dp);
		hp_1 = new HorizontalPanel();
		outerVp_1.add(hp_1);
		hp_1.setSpacing(10);
		hp_1.add(refreshDoc);
		hp_1.add(lockButton);
		hp_1.add(saveButton);
		refreshDoc.addClickHandler(this);
		lockButton.addClickHandler(this);
		saveButton.addClickHandler(this);
		
		outerVp_1.add(tabPanel);
		tabPanel.setSize("438px", "109px");
		tabPanel.addSelectionHandler(new SelectionHandler<Integer>() 
		        {
					@Override
					public void onSelection(SelectionEvent<Integer> event) {
						int tabId = event.getSelectedItem();
						
						TabContent current = tabDocuments.get(tabId);
						readOnlyDoc = current.getReadOnlyDoc();
						lockedDoc = current.getLockedDoc();
						title = current.getTitle();
						contents = current.getContents();
						refreshDoc.setEnabled(current.getRefreshDoc());
						lockButton.setEnabled(current.getLockButton());
						saveButton.setEnabled(current.getSaveButton());

						System.out.println("tabID: " + tabId + "title: " + title.getValue());
					}
		        });
		
		/*
		TabContent newTab = new TabContent(null, null, true);
		tabPanel.add(newTab.getVp(), "New tab", false);
		tabDocuments.add(newTab);
		tabPanel.selectTab(0); */
		
		outerHp.add(outerVp_1);
		outerVp = new VerticalPanel();
		outerVp.setSpacing(20);
		dp = new DecoratorPanel();
		dp.setWidth("100%");
		statusArea.setSpacing(10);
		statusArea.add(new HTML("<h2>Console</h2>"));
		dp.add(statusArea);
		outerVp.add(dp);
		outerHp.add(outerVp);
		
		refreshList.addClickHandler(this);
		createNew.addClickHandler(this);
		
		documentList.addChangeHandler(this);
		documentList.setVisibleItemCount(10);
		
		setDefaultButtons();
		initWidget(outerHp);
		
		lister.getDocumentList();
	}
	protected int setTabWidget(String myKey, String myTitle) {
		/*
		VerticalPanel newVP = new VerticalPanel();
		newVP.setSize("421px", "3cm");
		newVP.setSpacing(10);
		newVP.add(new HTML("<h2>Selected Document</h2>"));
		TextBox t = new TextBox();
		t.setWidth("100%");
		newVP.add(t);
		RichTextArea c = new RichTextArea();
		c.setWidth("100%");
		newVP.add(c);
		tabPanel.add(newVP, result.getTitle());
		t.setValue(result.getTitle());
		c.setHTML(result.getContents()); */
		
		Boolean open = false;
		int i;
		for (i = 0; i < tabDocuments.size(); i++) {
			String key = tabDocuments.get(i).getKey();
			if (key != null && key.equals(myKey)) {
				open = true;
				break;
			}
		}
		
		TabContent tab = null;
		int tabIndex = i;
		if (!open) {
			tab = new TabContent(null, null);
			tabDocuments.add(tab);
			tabPanel.add(tab.getVp(), myTitle);
			tabIndex = tabDocuments.size() - 1;

			
		} else {
			tab = tabDocuments.get(i);

		}
		
		if (readOnlyDoc == null) {
			tab.setReadOnlyDoc(null);
		} else {
			tab.setReadOnlyDoc(readOnlyDoc.getCopy());
		}
		
		if (lockedDoc == null) {
			tab.setLockedDoc(null);
		} else {
			tab.setLockedDoc(lockedDoc.getCopy());
		}
		title = tab.getTitle();
		contents = tab.getContents();
		tab.setKey(myKey);
		return tabIndex;
		
	}
	/**
	 * Resets the state of the buttons and edit objects to their default.
	 * 
	 * The state of these objects is modified by requesting or obtaining locks
	 * and trying to or successfully saving.
	 */
	protected void setDefaultButtons() {
		refreshDoc.setEnabled(true);
		lockButton.setEnabled(true);
		saveButton.setEnabled(false);
		// TODO: create a new tab
		title.setEnabled(false);
		contents.setEnabled(false);
	}
	
	/**
	 * Behaves similarly to locking a document, except without a key/lock obj.
	 */
	private void createNewDocument() {
		discardExisting(null);
		lockedDoc = new LockedDocument(null, null, null,
				"Enter the document title.",
				"Enter the document contents.");
		locker.gotDoc(lockedDoc);
		History.newItem("new");
	}
	
	/**
	 * Returns the currently active token.
	 * 
	 * @return history token which describes the current state
	 */
	protected String getToken() {
		if (lockedDoc != null) {
			if (lockedDoc.getKey() == null) {
				return "new";
			}
			return lockedDoc.getKey();
		} else if (readOnlyDoc != null) {
			return readOnlyDoc.getKey();
		} else {
			return "list";
		}
	}
	
	/**
	 * Modifies the current state to reflect the supplied token.
	 * 
	 * @param args history token received
	 */
	protected void receiveArgs(String args) {
		if (args.equals("list")) {
			//readOnlyDoc = null;
			//lockedDoc = null;
			//title.setValue("");
			//contents.setHTML("");
			//setDefaultButtons();
		} else if (args.equals("new")) {
			createNewDocument();
		} else {
			reader.getDocument(args);
		}
	}
	
	/**
	 * Adds status lines to the console window to enable transparency of the
	 * underlying processes.
	 * 
	 * @param status the status to add to the console window
	 */
	protected void statusUpdate(String status) {
		while (statusArea.getWidgetCount() > 22) {
			statusArea.remove(1);
		}
		final HTML statusUpd = new HTML(status);
		statusArea.add(statusUpd);
	}

	/* (non-Javadoc)
	 * Receives button events.
	 * @see com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt.event.dom.client.ClickEvent)
	 */
	@Override
	public void onClick(ClickEvent event) {
		if (event.getSource().equals(refreshList)) {
			History.newItem("list");
			lister.getDocumentList();
		} else if (event.getSource().equals(createNew)) {
			createNewDocument();
		} else if (event.getSource().equals(refreshDoc)) {
			if (readOnlyDoc != null) {
				reader.getDocument(readOnlyDoc.getKey());
			}
		} else if (event.getSource().equals(lockButton)) {
			System.out.println("Trying to lock");
			if (readOnlyDoc != null) {
				System.out.println("Closer to lock");
				locker.lockDocument(readOnlyDoc.getKey());
			}
		} else if (event.getSource().equals(saveButton)) {
			System.out.println("Saving step 1");
			if (lockedDoc != null) {
				System.out.println("Saving step 2");
				if (lockedDoc.getTitle().equals(title.getValue()) &&
						lockedDoc.getContents().equals(contents.getHTML())) {
					statusUpdate("No document changes; not saving.");
				}
				else {
					lockedDoc.setTitle(title.getValue());
					lockedDoc.setContents(contents.getHTML());
					saver.saveDocument(lockedDoc);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * Intercepts events from the list box.
	 * @see com.google.gwt.event.dom.client.ChangeHandler#onChange(com.google.gwt.event.dom.client.ChangeEvent)
	 */
	@Override
	public void onChange(ChangeEvent event) {
		if (event.getSource().equals(documentList)) {
			String key = documentList.getValue(documentList.getSelectedIndex());
			discardExisting(key);
			reader.getDocument(key);
		}
	}
	
	/**
	 * Used to release existing locks when the active document changes.
	 * 
	 * @param key the key of the new active document or null for a new document
	 */
	private void discardExisting(String key) {
		if (lockedDoc != null) {
			if (lockedDoc.getKey() == null) {
				statusUpdate("Discarding new document.");
			}
			else if (!lockedDoc.getKey().equals(key)) {
				releaser.releaseLock(lockedDoc);
			}
			else {
				// Newly active item is the currently locked item.
				return;
			}
			lockedDoc = null;
			setDefaultButtons();
		} else if (readOnlyDoc != null) {
			if (readOnlyDoc.getKey().equals(key)) return;
			readOnlyDoc = null;
		}
	}
}
