package edu.caltech.cs141b.hw2.gwt.collab.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.caltech.cs141b.hw2.gwt.collab.shared.LockExpired;
import edu.caltech.cs141b.hw2.gwt.collab.shared.LockedDocument;
import edu.caltech.cs141b.hw2.gwt.collab.shared.UnlockedDocument;

public class DocSaver implements AsyncCallback<UnlockedDocument> {
	
	private Collaborator collaborator;
	private int i;
	
	public DocSaver(Collaborator collaborator) {
		this.collaborator = collaborator;
	}
	
	public void saveDocument(LockedDocument lockedDoc) {
		collaborator.statusUpdate("Attemping to save document.");
		collaborator.waitingKey = lockedDoc.getKey();
		collaborator.collabService.saveDocument(lockedDoc, this);
		collaborator.saveButton.setEnabled(false);
		collaborator.title.setEnabled(false);
		collaborator.contents.setEnabled(false);
		
		
		for (i = 0; i <collaborator.tabDocuments.size(); i++) {
			String key = collaborator.tabDocuments.get(i).getKey();
			if (key != null && key.equals(lockedDoc.getKey())) {
				break;
			}
		}
		if (i == collaborator.tabDocuments.size())
			i--;
		collaborator.tabDocuments.get(i).setSaveButton(false);
		
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
			
			collaborator.tabDocuments.get(i).setKey(result.getKey());
			collaborator.tabPanel.getTabBar().setTabHTML(i, result.getTitle());
			
			collaborator.reader.gotDoc(result);
			// Refresh list in case title was changed.
			collaborator.lister.getDocumentList();
		} else {
			GWT.log("Saved document is not the anticipated document.");
		}
	}
	
}

