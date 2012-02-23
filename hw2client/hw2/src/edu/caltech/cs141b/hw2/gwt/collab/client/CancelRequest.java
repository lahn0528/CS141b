package edu.caltech.cs141b.hw2.gwt.collab.client;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Used in conjunction with <code>CollaboratorService.cancelRequest()</code>.
 */
public class CancelRequest implements AsyncCallback<Void> {
	
private Collaborator collaborator;
	
	public CancelRequest(Collaborator collaborator) {
		this.collaborator = collaborator;
	}
	
	public void cancelRequest(String key) {
		collaborator.statusUpdate("Canceling document request" + key + ".");
		collaborator.waitingKey = key;
		collaborator.collabService.cancelRequest(key, collaborator.userKey, this);
	}

	@Override
	public void onFailure(Throwable caught) {
		collaborator.statusUpdate("Error canceling document request"
				+ "; caught exception " + caught.getClass()
				+ " with message: " + caught.getMessage());
		GWT.log("Error canceling document request.", caught);		
	}

	@Override
	public void onSuccess(Void result) {
		collaborator.statusUpdate("Document request canceled.");
	}

}