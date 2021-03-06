package edu.caltech.cs141b.hw2.gwt.collab.client; 

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

/**
 * A {@link TabLayoutPanel} that shows scroll buttons if necessary
 */
public class ScrolledTabLayoutPanel extends TabLayoutPanel {

	private LayoutPanel panel;
	private FlowPanel tabBar;
	private Image scrollLeftButton;
	private Image scrollRightButton;
	private HandlerRegistration windowResizeHandler;

	private ImageResource leftArrowImage;
	private ImageResource rightArrowImage;
	private static final int SCROLL_INTERVAL = 60;
	private HandlerRegistration selectionHandler;
	private int lastScroll = 0;

	public ScrolledTabLayoutPanel(double barHeight, Unit barUnit,
                                ImageResource leftArrowImage, ImageResource rightArrowImage) {
    super(barHeight, barUnit);

    this.leftArrowImage = leftArrowImage;
    this.rightArrowImage = rightArrowImage;

    // The main widget wrapped by this composite, which is a LayoutPanel with the tab bar & the tab content
    panel = (LayoutPanel) getWidget();

    // Find the tab bar, which is the first flow panel in the LayoutPanel
    for (int i = 0; i < panel.getWidgetCount(); ++i) {
    	Widget widget = panel.getWidget(i);
    	if (widget instanceof FlowPanel) {
    		tabBar = (FlowPanel) widget;
    		break; // tab bar found
    	}
    }

    initScrollButtons();
	}

	@Override
	public void add(Widget w) {
		super.add(w);
		checkIfScrollButtonsNecessary();
	}

	@Override
	public void add(Widget child, String text) {
		super.add(child, text);
		checkIfScrollButtonsNecessary();
	}

	@Override
	public void add(Widget child, String text, boolean asHtml) {
		super.add(child, text, asHtml);
		checkIfScrollButtonsNecessary();
	}

	@Override
	public void add(Widget child, Widget tab) {
		super.add(child, tab);
		checkIfScrollButtonsNecessary();
	}

	@Override
	public boolean remove(Widget w) {
		boolean b = super.remove(w);
		checkIfScrollButtonsNecessary();
		return b;
	}

	@Override
 	protected void onLoad() {
		super.onLoad();

		if (windowResizeHandler == null) {
			windowResizeHandler = Window.addResizeHandler(new ResizeHandler() {
				//@Override
				public void onResize(ResizeEvent event) {
					checkIfScrollButtonsNecessary();
				}
			});
		}

		if (selectionHandler == null) {
			selectionHandler = addSelectionHandler(new SelectionHandler<Integer>() {
				public void onSelection(SelectionEvent<Integer> selectedEvent) {
					doOnSelectTab(selectedEvent.getSelectedItem());
				}
			});
		}
	}

	@Override
	protected void onUnload() {
		super.onUnload();

		if (windowResizeHandler != null) {
			windowResizeHandler.removeHandler();
			windowResizeHandler = null;
		}

		if (selectionHandler != null) {
			selectionHandler.removeHandler();
			selectionHandler = null;
		}
	}

	private void doOnSelectTab(int selected) {
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			public void execute() {
				scrollTo(lastScroll);
			}
		});

	}

	private ClickHandler createScrollClickHandler(final int diff) {
		return new ClickHandler() {
			//@Override
			public void onClick(ClickEvent event) {
				Widget lastTab = getLastTab();
				if (lastTab == null)
					return;

				int newLeft = parsePosition(tabBar.getElement().getStyle().getLeft()) + diff;
				int rightOfLastTab = getRightOfWidget(lastTab);

				// Prevent scrolling the last tab too far away form the right border,
				// or the first tab further than the left border position
				if (newLeft <= 0 && (getTabBarWidth() - newLeft < (rightOfLastTab - diff / 2))) {
					scrollTo(newLeft);
				}
			}
		};
	}

  /**
   * Create and attach the scroll button images with a click handler
   */
	private void initScrollButtons() {
		scrollLeftButton = new Image(leftArrowImage);
		int leftImageWidth = scrollLeftButton.getWidth();
		panel.insert(scrollLeftButton, 0);
		panel.setWidgetLeftWidth(scrollLeftButton, 0, Unit.PX, leftImageWidth, Unit.PX);
		panel.setWidgetTopHeight(scrollLeftButton, -20, Unit.PX, scrollLeftButton.getWidth(), Unit.PX);

		scrollLeftButton.addClickHandler(createScrollClickHandler(+SCROLL_INTERVAL));
		scrollLeftButton.setVisible(false);

		scrollRightButton = new Image(rightArrowImage);
		panel.insert(scrollRightButton, 0);
		panel.setWidgetRightWidth(scrollRightButton, 0, Unit.PX, scrollRightButton.getWidth(), Unit.PX);
		panel.setWidgetTopHeight(scrollRightButton, -20, Unit.PX, scrollRightButton.getHeight(), Unit.PX);

		scrollRightButton.addClickHandler(createScrollClickHandler(-1 * SCROLL_INTERVAL));
		scrollRightButton.setVisible(false);
	}

	private void checkIfScrollButtonsNecessary() {
		// Defer size calculations until sizes are available, when calculating immediately after
		// add(), all size methods return zero
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			public void execute() {
				//@Override
				boolean isScrolling = isScrollingNecessary();
				// When the scroll buttons are being hidden, reset the scroll position to zero to
				// make sure no tabs are still out of sight
				if (scrollRightButton.isVisible() && !isScrolling) {
					resetScrollPosition();
				}
				scrollRightButton.setVisible(isScrolling);
				scrollLeftButton.setVisible(isScrolling);
			}
		});
	}

	private void resetScrollPosition() {
		scrollTo(0);
	}

	private void scrollTo(int pos) {
		tabBar.getElement().getStyle().setLeft(pos, Unit.PX);
		lastScroll = pos;
	}

	private boolean isScrollingNecessary() {
		Widget lastTab = getLastTab();
		return lastTab != null && getRightOfWidget(lastTab) > getTabBarWidth();

	}

	private int getRightOfWidget(Widget widget) {
		return widget.getElement().getOffsetLeft() + widget.getElement().getOffsetWidth();
	}

	private int getTabBarWidth() {
		return tabBar.getElement().getParentElement().getClientWidth();
	}

	private Widget getLastTab() {
		if (tabBar.getWidgetCount() == 0)
			return null;

		return tabBar.getWidget(tabBar.getWidgetCount() - 1);
	}

	private static int parsePosition(String positionString) {
		int position;
		try {
			for (int i = 0; i < positionString.length(); i++) {
				char c = positionString.charAt(i);
				if (c != '-' && !(c >= '0' && c <= '9')) {
					positionString = positionString.substring(0, i);
				}
			}

			position = Integer.parseInt(positionString);
		} catch (NumberFormatException ex) {
			position = 0;
		}
		return position;
	}
}