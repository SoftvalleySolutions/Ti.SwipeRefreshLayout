package com.rkam.swiperefreshlayout;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.RelativeLayout;

/**
 * MySwipeRefreshLayout is a modified SwipeRefreshLayout so that Titanium views
 * are supported. It overrides the canChildScrollUp method used by Android to 
 * determine whether the gesture is for refresh or if the user is just scrolling up
 * the scrollable view.
 */
public class MySwipeRefreshLayout extends SwipeRefreshLayout {

	private View nativeView; // usually the layout wrapping the listview
	private View nativeChildView; // the native android listview
	
	public MySwipeRefreshLayout(Context context) {
		super(context);
	}
	
	public MySwipeRefreshLayout(Context context, AttributeSet attrs) {
	    super(context, attrs);
	}
	
	public View getNativeView() {
		return nativeView;
	}
	
	public void setNativeView(View view) {
		this.nativeView = view;
	}
	
	@Override
	public boolean canChildScrollUp() {
		// ScrollViews are also an instance of FrameLayouts and we do not want to get
		// the ScrollView's child view as it will not work.
		if (nativeView instanceof FrameLayout && !(nativeView instanceof ScrollView)) {
			// Try to get the native Android ListView inside the FrameLayout
        	nativeChildView = ((FrameLayout) nativeView).getChildAt(0);
		} else if(nativeView instanceof RelativeLayout){
			//get the ListView inside the tableView
			nativeChildView = ((RelativeLayout) nativeView).getChildAt(1);
			nativeChildView = ((FrameLayout) nativeChildView).getChildAt(0);
		} else {
			nativeChildView = nativeView;
		}
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (nativeChildView instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) nativeChildView;
                return absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                                .getTop() < absListView.getPaddingTop());
            } else {
                return nativeChildView.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(nativeChildView, -1);
        }
    }
	
}
