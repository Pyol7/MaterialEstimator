package com.jeffreyromero.materialestimator.utilities;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class CustomRecyclerView extends RecyclerView {
    private View mEmptyListView;

    public CustomRecyclerView(Context context) {
        super(context);
    }

    public CustomRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * Designate a view as the empty view. When the backing adapter has no
     * data this view will be made visible and the recycler view hidden.
     */
    public void setEmptyView(View emptyListView) {
        mEmptyListView = emptyListView;
    }

    /**
     * Observer base class for watching changes to an RecyclerView.ItemTypesListAdapter
     */
    private AdapterDataObserver mDataObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();

            showEmptyViewIfListIsEmpty();
        }
    };

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        //If there is an existing adapter unregister this observer form it and
        //register it to the new adapter.
        if (getAdapter() != null) {
            getAdapter().unregisterAdapterDataObserver(mDataObserver);
        }
        if (adapter != null) {
            adapter.registerAdapterDataObserver(mDataObserver);
        }
        super.setAdapter(adapter);

        showEmptyViewIfListIsEmpty();
    }

    private void showEmptyViewIfListIsEmpty() {
        if (mEmptyListView != null && getAdapter() != null) {
            boolean listIsEmpty = getAdapter().getItemCount() == 0;
            mEmptyListView.setVisibility(listIsEmpty ? VISIBLE : GONE);
            setVisibility(listIsEmpty ? GONE : VISIBLE);
        }
    }
}
