package edu.caltech.cs141b.hw2.gwt.collab.shared;

import java.io.Serializable;

/**
 * Abstract class for each type of message
 */
public abstract class Message implements Serializable {

  public enum Type {
	DOC_KEY,
	STATUS_MSG,
	LOGIN_MSG
  }

  private Type type;

  protected Message(Type type) {
    this.type = type;
  }

  public Type getType() {
    return type;
  }
}