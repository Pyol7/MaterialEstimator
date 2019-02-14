package com.jeffreyromero.materialestimator.Item;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeffreyromero.materialestimator.R;
import com.jeffreyromero.materialestimator.data.ItemTypesSharedPreference;
import com.jeffreyromero.materialestimator.models.BaseItem;

import java.util.ArrayList;

/**
 * Displays a list of all the stored ItemTypes.
 * Returns the clicked ItemType to the Listener.
 */
public class ItemTypesFragment extends Fragment {

    private static final String TAG = "ItemTypesFragment";
    private OnItemClickListener mListener;
    private RecyclerView.Adapter adapter;
    private ArrayList<BaseItem> items;
    private ItemTypesSharedPreference itemsSP;
    private Context context;

    public ItemTypesFragment() {
        // Required empty public constructor
    }

    public interface OnItemClickListener {
        void onItemTypesFragmentItemClick(BaseItem itemType);
    }

    public static ItemTypesFragment newInstance() {
        return new ItemTypesFragment();
    }

    public static String getTAG() {
        return TAG;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        // Ensure that there is a listener and it implements the callback(s).
        // In this case MainActivity is the listener so we can cast from context.
        if (context instanceof OnItemClickListener) {
            mListener = (OnItemClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ItemTypesFragment.OnItemClickListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Init item shared preferences
        itemsSP = new ItemTypesSharedPreference(context, getString(R.string.item_types_sp_file_name));
        items = itemsSP.getAll();
        adapter = new Adapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Show options menu.
        setHasOptionsMenu(true);

        // Inflate the fragment layout
        final View view = inflater.inflate(
                R.layout.item_types_fragment, container, false);

        // Set title
        getActivity().setTitle(R.string.item_types_fragment_title);

        // Get the recyclerView view
        RecyclerView rv = view.findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(adapter);

        // Swipe and Drag functionality.
        SwipeAndDragHelper swipeAndDragHelper = new SwipeAndDragHelper();
        ItemTouchHelper touchHelper = new ItemTouchHelper(swipeAndDragHelper);
        touchHelper.attachToRecyclerView(rv);

        // Enable drawer navigation for this fragment
//        ((MainActivity)getActivity()).enableDrawerNavigation();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // todo - setup saved instance state
        items = itemsSP.getAll();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //Programmatically create the add (+) menu item
//        MenuItem item = menu.add(Menu.NONE, R.id.action_add, 10, R.string.action_add);
//        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
//        item.setIcon(R.drawable.ic_add_white_24dp);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {

            // Respond to the add (+) menu item with adding a new list.
            // This is done by the listener (ItemTypesActivity).
//            mListener.onAddNewListButtonClick();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //---------------------------------------- adapter ------------------------------------------//

    public class Adapter extends RecyclerView.Adapter {

        @Override
        public int getItemCount() {
            return items == null ? 0 : items.size();
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Get the inflater
            LayoutInflater inflater = LayoutInflater.from(context);
            // Inflate the item view layout
            View itemView = inflater.inflate(R.layout.list_item_textview_textview, parent, false);
            return new itemViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            BaseItem baseItem = items.get(position);
            itemViewHolder viewHolder = (itemViewHolder) holder;
            viewHolder.columnLeftTV.setText(baseItem.getSubType());
            viewHolder.columnRightTV.setText("Created by");
        }


        private class itemViewHolder extends RecyclerView.ViewHolder {
            TextView columnLeftTV;
            TextView columnRightTV;

            itemViewHolder(final View itemView) {
                super(itemView);
                columnLeftTV = itemView.findViewById(R.id.columnLeftTV);
                columnRightTV = itemView.findViewById(R.id.columnRightTV);
                itemView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // Pass clicked position to listener
                        mListener.onItemTypesFragmentItemClick(items.get(getAdapterPosition()));
                    }

                });
            }
        }
    }

    //------------------------------------ SwipeAndDragHelper -----------------------------------//

    public class SwipeAndDragHelper extends ItemTouchHelper.Callback {

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            int swipeFlags = ItemTouchHelper.RIGHT;
            return makeMovementFlags(0, swipeFlags);
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return true;
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            int oldPosition = viewHolder.getAdapterPosition();
            int newPosition = target.getAdapterPosition();
            BaseItem baseItem = items.get(oldPosition);
            items.remove(oldPosition);
            items.add(newPosition, baseItem);
            adapter.notifyItemMoved(oldPosition, newPosition);
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            //Get position of the removed list.
            int position = viewHolder.getAdapterPosition();
            //Get the removed list.
            BaseItem baseItem = items.get(position);
            //Remove it from the source list.
            itemsSP.remove(baseItem.toString());
            //Remove it from the local array list.
            items.remove(position);
            adapter.notifyItemRemoved(position);
        }
    }

}
//Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT).show();