package edu.caltech.cs141b.hw2.gwt.collab.client.channel;

import edu.caltech.cs141b.hw2.gwt.collab.client.channel.SocketError;

/**
 * Listener to receive messages from the server.
 *
 * <p>
 * For more information about the events handled by this listener, see the
 * Channel API JavaScript Reference: {@link
 * "http://code.google.com/appengine/docs/java/channel/javascript.html"}.
 * </p>
 */
public interface SocketListener {
  /** Called when the channel is opened. */
  void onOpen();

  /** Called when the channel receives a message from the server. */
  void onMessage(String message);

  /** Called when the channel receives an error. */
  void onError(SocketError error);

  /** Called when the channel is closed. */
  void onClose();

}

