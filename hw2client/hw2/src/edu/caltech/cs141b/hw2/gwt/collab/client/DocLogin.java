package edu.caltech.cs141b.hw2.gwt.collab.client;

import edu.caltech.cs141b.hw2.gwt.collab.client.channel.Channel;
import edu.caltech.cs141b.hw2.gwt.collab.client.channel.ChannelFactory;
import edu.caltech.cs141b.hw2.gwt.collab.client.channel.SocketError;
import edu.caltech.cs141b.hw2.gwt.collab.client.channel.SocketListener;
import edu.caltech.cs141b.hw2.gwt.collab.client.channel.ChannelFactory.ChannelCreatedCallback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.caltech.cs141b.hw2.gwt.collab.shared.LoginResults;

/**
 * Used in conjunction with <code>CollaboratorService.login()</code>.
 */
public class DocLogin implements AsyncCallback<LoginResults> {
	
	private Collaborator collaborator;
	//private SerializationStreamFactory pushServiceStreamFactory =(SerializationStreamFactory) PushService.App.getInstance();;
	
	public DocLogin(Collaborator collaborator) {
		this.collaborator = collaborator;
	}
	
	public void login(String name) {
		collaborator.statusUpdate("Registering user " + name + ".");
		collaborator.collabService.login(name, this);
	}

	@Override
	public void onFailure(Throwable caught) {
		collaborator.statusUpdate("Error login in new user"
				+ "; caught exception " + caught.getClass()
				+ " with message: " + caught.getMessage());
		GWT.log("Error login in new user.", caught);
	}

	@Override
	public void onSuccess(LoginResults results) {	
		ChannelFactory.createChannel(results.getChannelId(), new ChannelCreatedCallback() {
			  @Override
			  public void onChannelCreated(Channel channel) {
			    channel.open(new SocketListener() {
			      @Override
			      public void onOpen() {
			    	collaborator.channelOpen();	
			      }
			      @Override
			      public void onMessage(String message) {
			        String[] values = message.split("/");
			        collaborator.receiveMsg(values);
			      }
			      @Override
			      public void onError(SocketError error) {
			        Window.alert("Error: " + error.getDescription());
			      }
			      @Override
			      public void onClose() {
			        Window.alert("Channel closed!");
			      }
			    });
			  }
			});
		
		
		collaborator.btnLogin.setEnabled(false);
		collaborator.loginName.setEnabled(false);
		collaborator.alreadyLogin = true;
		collaborator.userKey = results.getUserId();
		collaborator.statusUpdate("Successfully logged in new user. ");
	}
}
