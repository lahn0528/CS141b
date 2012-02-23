package edu.caltech.cs141b.hw2.gwt.collab.client.channel;

import edu.caltech.cs141b.hw2.gwt.collab.client.channel.Socket;
import edu.caltech.cs141b.hw2.gwt.collab.client.channel.SocketListener;
import com.google.gwt.core.client.JavaScriptObject;

/**
 * An error encountered by a {@link Socket}, passed to the
 * {@link SocketListener}.
 */
public final class SocketError extends JavaScriptObject {

  protected SocketError() {
  }

  /** Returns a short description of the error encountered. */
  public final native String getDescription() /*-{
    return this.description;
  }-*/;

  /** Returns the HTTP error code corresponding to the error encountered. */
  public final native int getCode() /*-{
    return this.code;
  }-*/;


}

