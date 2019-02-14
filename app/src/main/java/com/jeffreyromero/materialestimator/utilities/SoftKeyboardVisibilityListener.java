package com.jeffreyromero.materialestimator.utilities;

import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

public class SoftKeyboardVisibilityListener implements ViewTreeObserver.OnGlobalLayoutListener {

    private View rootView;

    public SoftKeyboardVisibilityListener(View rootView) {

        this.rootView = rootView;
    }

    /**
     * Callback method to be invoked when the global layout state or the visibility of views
     * within the view tree changes
     */
    @Override
    public void onGlobalLayout() {
        Rect rectangle = new Rect();
        //rectangle will be populated with the coordinates of the visible view area.
        rootView.getWindowVisibleDisplayFrame(rectangle);

        int rootViewHeight = rootView.getRootView().getHeight();
        int rectangleHeight = rectangle.bottom - rectangle.top;

        int heightDiff = rootViewHeight - rectangleHeight;
        // If the difference is more than a quarter of the screen height then its probably a keyboard...
        if (heightDiff > rootViewHeight/4) {
            onOpen();
        } else if (heightDiff < rootViewHeight/4){
            onClose();
        }
    }

    public void onOpen(){
        Log.i("TEST", "OPENED");
    }

    public void onClose(){
        Log.i("TEST", "CLOSED");
    }
}
