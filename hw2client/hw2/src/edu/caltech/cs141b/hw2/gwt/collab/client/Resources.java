package edu.caltech.cs141b.hw2.gwt.collab.client;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface Resources extends ClientBundle {
    @Source("close_button.png")
    ImageResource close();
    
    @Source("leftArrow.png")
    ImageResource leftArrow();
    
    @Source("rightArrow.png")
    ImageResource rightArrow();
}
