package com.jeffreyromero.materialestimator.material;

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

import com.google.gson.Gson;
import com.jeffreyromero.materialestimator.R;
import com.jeffreyromero.materialestimator.data.Deserializer;
import com.jeffreyromero.materialestimator.data.MaterialListsDataSource;
import com.jeffreyromero.materialestimator.models.MaterialList;

import java.util.ArrayList;

/**
 * Receives a list of MaterialList and displays it.
 * Returns the clicked list item position to the Listener.
 * Informs the listener when the add new button is clicked.
 */
public class MaterialListsFragment extends Fragment {

    private MaterialListsDataSource dataSource;
    private ArrayList<MaterialList> allLists;
    private OnItemClickListener mListener;
    private RecyclerView.Adapter adapter;
    private Context context;

    public MaterialListsFragment() {
        // Required empty public constructor
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onAddNewListButtonClick();
    }

    public static MaterialListsFragment newInstance(ArrayList<MaterialList> allLists) {
        MaterialListsFragment fragment = new MaterialListsFragment();
        //Serialize the material list.
        String json = new Gson().toJson(allLists);
        //Add it to the bundle.
        Bundle args = new Bundle();
        args.putString("allLists", json);
        //Set the bundle to the fragment and return it.
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnItemClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement MaterialListsFragment callback methods");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set all fields
        context = getActivity();
        String json = getArguments().getString("allLists");
        allLists = Deserializer.toArrayListOfMaterialList(json);
        //Create User Shared Preferences helper.
        dataSource = new MaterialListsDataSource(
                getString(R.string.user_material_lists),
                context
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Show options menu.
        setHasOptionsMenu(true);
        // Inflate the fragment layout
        final View view = inflater.inflate(R.layout.material_lists_fragment, container, false);
        // Set the title
        TextView titleTV = view.findViewById(R.id.titleTV);
        titleTV.setText(context.getString(R.string.material_lists_title));
        // Get the recyclerView view
        RecyclerView rv = view.findViewById(R.id.recyclerView);
        // Instantiate the adapter and load the list
        adapter = new Adapter();
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(adapter);

        // Swipe and Drag functionality.
        SwipeAndDragHelper swipeAndDragHelper = new SwipeAndDragHelper();
        ItemTouchHelper touchHelper = new ItemTouchHelper(swipeAndDragHelper);
        touchHelper.attachToRecyclerView(rv);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Reload all the stored material lists to catch any changes.
        allLists = dataSource.getAll();
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
        MenuItem item = menu.add(Menu.NONE, R.id.action_add, 10, R.string.action_add);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        item.setIcon(R.drawable.ic_add_white_24dp);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {

            // Respond to the add (+) menu item with adding a new list.
            // This is done by the listener (MaterialActivity).
            mListener.onAddNewListButtonClick();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //---------------------------------------- adapter ------------------------------------------//

    public class Adapter extends RecyclerView.Adapter {

        @Override
        public int getItemCount() {
            return allLists.size();
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
            MaterialList materialList = allLists.get(position);
            itemViewHolder viewHolder = (itemViewHolder) holder;
            viewHolder.columnLeftTV.setText(materialList.getName());
            viewHolder.columnRightTV.setText(materialList.getDateCreated());
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
                        mListener.onItemClick(getAdapterPosition());
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
            MaterialList materialList = allLists.get(oldPosition);
            allLists.remove(oldPosition);
            allLists.add(newPosition, materialList);
            adapter.notifyItemMoved(oldPosition, newPosition);
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            //Get position of the removed list.
            int position = viewHolder.getAdapterPosition();
            //Get the removed list.
            MaterialList materialList = allLists.get(position);
            //Remove it from the source list.
            dataSource.remove(materialList.toString());
            //Remove it from the local array list.
            allLists.remove(position);
            adapter.notifyItemRemoved(position);
        }
    }

}
//Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT).show();