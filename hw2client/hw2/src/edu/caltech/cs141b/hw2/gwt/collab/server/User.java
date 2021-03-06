package edu.caltech.cs141b.hw2.gwt.collab.server;

import javax.jdo.annotations.*;
import edu.caltech.cs141b.hw2.gwt.collab.shared.UserValue;

import java.io.Serializable;

@PersistenceCapable(identityType= IdentityType.APPLICATION)
public class User implements Serializable {

  @PrimaryKey
  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
  @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
  private String key;

  @Persistent
  private String name;

  /**
   * Setter and getter methods
   */
  
  public String getKey() {
	  return key;
  }
  
  public String getName() {
	  return name;
  }

  public void setName(String name) {
	  this.name = name;
  }

  public UserValue toValue() {
	  return new UserValue(key, name);   
  }
  
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    User user = (User) o;

    if (key != null ? !key.equals(user.key) : user.key != null) return false;
    if (name != null ? !name.equals(user.name) : user.name != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = key != null ? key.hashCode() : 0;
    result = 31 * result + (name != null ? name.hashCode() : 0);
    return result;
  }
}

