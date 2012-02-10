package edu.caltech.cs141b.hw2.gwt.collab.client;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Image;

public class ImageAnchor extends Anchor {
    public ImageAnchor() {
    }

    public void setResource(ImageResource imageResource) {
        Image img = new Image(imageResource);
        img.setStyleName("navbarimg");
        DOM.insertBefore(getElement(), img.getElement(), DOM
                .getFirstChild(getElement()));
    }
}
