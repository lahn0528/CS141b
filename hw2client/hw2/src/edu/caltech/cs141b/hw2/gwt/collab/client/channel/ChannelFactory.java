package edu.caltech.cs141b.hw2.gwt.collab.client.channel;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.ScriptElement;
import com.google.gwt.user.client.Timer;

/** Manages creating {@link Channel}s to receive messages from the server. */
public class ChannelFactory {

  private static final String CHANNEL_SRC = "/_ah/channel/jsapi";
  private static Channel channel;

  /**
   * Creates a {@link Channel} with the given client ID, passing it to the given
   * {@link ChannelCreatedCallback}.
   *
   * <p>
   * If a Channel has already been created, this will immediately be passed to
   * the callback, so only one Channel will ever be created by this method.
   * </p>
   */
  public static void createChannel(final String clientId, final ChannelCreatedCallback callback) {
    if (channel == null) {
    	ScriptElement script = Document.get().createScriptElement();
    	script.setSrc(CHANNEL_SRC);
    	Document.get().getElementsByTagName("head").getItem(0).appendChild(script);

    	new Timer() {
    		@Override
    		public void run() {
    			if (scriptLoaded()) {
    				channel = createChannelImpl(clientId);
    				callback.onChannelCreated(channel);
    				this.cancel();
    			}
    		}
    	}.scheduleRepeating(100);
    } else {
    	callback.onChannelCreated(channel);
    }
  }

  private static native boolean scriptLoaded() /*-{
    return !!$wnd.goog && !!$wnd.goog.appengine && !!$wnd.goog.appengine.Channel;
  }-*/;

  private static final native Channel createChannelImpl(String clientId) /*-{
    return new $wnd.goog.appengine.Channel(clientId);
  }-*/;

  public interface ChannelCreatedCallback {
    public void onChannelCreated(Channel channel);
  }
}

