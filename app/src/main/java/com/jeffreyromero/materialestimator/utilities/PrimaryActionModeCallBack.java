package com.jeffreyromero.materialestimator.utilities;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.jeffreyromero.materialestimator.R;

public class PrimaryActionModeCallBack implements ActionMode.Callback {

    private OnActionItemClickListener mListener;
    private int position;

    public PrimaryActionModeCallBack(OnActionItemClickListener mListener, int position) {
        this.mListener = mListener;
        this.position = position;
    }

    // Use interface to make this CAB reusable
    public interface OnActionItemClickListener {
        void deleteItem(int position);
        // Used to destroy the actionMode instance held by the implementing fragment
        void destroyActionMode();
    }

    // Called when the action mode is created; startActionMode() was called
    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        // Inflate a menu resource providing context menu items
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
        return true;
    }

    // Called each time the action mode is shown. Always called after onCreateActionMode, but
    // may be called multiple times if the mode is invalidated.
    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false; // Return false if nothing is done
    }

    // Called when the user selects a contextual menu item
    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.context_delete:
                mListener.deleteItem(position);
                // Action performed, so close the CAB
                mode.finish();
                return true;
            default:
                return false;
        }
    }

    // Called when the user exits the action mode
    @Override
    public void onDestroyActionMode(ActionMode mode) {
        mListener.destroyActionMode();
        mListener = null;
    }
}
