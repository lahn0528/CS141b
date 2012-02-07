package edu.caltech.cs141b.hw2.gwt.collab.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.caltech.cs141b.hw2.gwt.collab.shared.LockExpired;
import edu.caltech.cs141b.hw2.gwt.collab.shared.LockedDocument;
import edu.caltech.cs141b.hw2.gwt.collab.shared.UnlockedDocument;

public class DocSaver implements AsyncCallback<UnlockedDocument> {
	
	private Collaborator collaborator;
	// Index of a tab to save information from.
	private int tabIndex;
	
	public DocSaver(Collaborator collaborator) {
		this.collaborator = collaborator;
	}
	
	public void saveDocument(LockedDocument lockedDoc) {
		// Find tab index of the locked document.If saving a document for 
		// the first time, key field is null.
		tabIndex = collaborator.findTabIndex(lockedDoc.getKey());
		
		collaborator.statusUpdate("Attemping to save document.");
		collaborator.waitingKey = lockedDoc.getKey();
		collaborator.collabService.saveDocument(lockedDoc, this);
		
		// Set button status, and store these button status information in 
		// corresponding tab object.
		collaborator.saveButton.setEnabled(false);
		collaborator.title.setEnabled(false);
		collaborator.contents.setEnabled(false);
		collaborator.tabDocuments.get(tabIndex).setSaveButton(false);
		
	}

	@Override
	public void onFailure(Throwable caught) {
		if (caught instanceof LockExpired) {
			collaborator.statusUpdate("Lock had already expired; save failed.");
		} else {
			collaborator.statusUpdate("Error saving document"
					+ "; caught exception " + caught.getClass()
					+ " with message: " + caught.getMessage());
			GWT.log("Error saving document.", caught);
			collaborator.releaser.releaseLock(collaborator.lockedDoc);
		}
		if (collaborator.lockedDoc != null) {
			collaborator.reader.gotDoc(collaborator.lockedDoc.unlock());
			collaborator.lockedDoc = null;
		}
	}

	@Override
	public void onSuccess(UnlockedDocument result) {
		collaborator.statusUpdate("Document '" + result.getTitle()
				+ "' successfully saved.");
		if (collaborator.waitingKey == null || 
				result.getKey().equals(collaborator.waitingKey)) {
			
			// If saving a document for the first time, set the key.
			// Also allow people to create new document again.
			if (collaborator.tabDocuments.get(tabIndex).getKey() == null) {
				collaborator.createNew.setEnabled(true);
				collaborator.tabDocuments.get(tabIndex).setKey(result.getKey());
			}
			// Allow people to rename.
			collaborator.tabPanel.getTabBar().setTabHTML(tabIndex, result.getTitle());
			
			
			collaborator.reader.gotDoc(result);
			// Refresh list in case title was changed.
			collaborator.lister.getDocumentList();

		} else {
			GWT.log("Saved document is not the anticipated document.");
		}
	}
	
}

