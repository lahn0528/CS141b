package edu.caltech.cs141b.hw2.gwt.collab.client.channel;

import edu.caltech.cs141b.hw2.gwt.collab.client.channel.Channel;
import edu.caltech.cs141b.hw2.gwt.collab.client.channel.SocketListener;
import com.google.gwt.core.client.JavaScriptObject;

/** Returned by calls to {@link Channel#open(SocketListener)}. */
public class Socket extends JavaScriptObject {
  protected Socket() {
  }

  /**
   * Closes this socket to incoming messages from the server. The socket cannot
   * be used again after calling close; the server must create a new socket.
   */
  public final native void close() /*-{
    this.close();
  }-*/;
}
