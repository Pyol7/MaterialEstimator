package com.jeffreyromero.materialestimator.material;

import android.annotation.SuppressLint;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jeffreyromero.materialestimator.R;
import com.jeffreyromero.materialestimator.data.Deserializer;
import com.jeffreyromero.materialestimator.data.MaterialListsDataSource;
import com.jeffreyromero.materialestimator.models.Material;
import com.jeffreyromero.materialestimator.models.MaterialList;
import com.jeffreyromero.materialestimator.utilities.SingleInputDialog;

/**
 * Receives a MaterialList and displays it.
 * Provides Adding and editing list items.
 */
public class MaterialListFragment extends Fragment implements
        EditMaterialDialog.OnItemChangeListener,
        AddMaterialDialog.OnItemChangeListener,
        SingleInputDialog.OnDialogSubmitListener {

    private OnItemClickListener mListener;
    private MaterialListsDataSource dataSource;
    private RecyclerView.Adapter adapter;
    private MaterialList materialList;
    private int selectedPosition;
    private TextView titleTV;
    private String listName;
    private Context context;

    public MaterialListFragment() {
        // Required empty public constructor
    }

    public interface OnItemClickListener {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public static MaterialListFragment newInstance(MaterialList materialList) {
        MaterialListFragment fragment = new MaterialListFragment();
        //Serialize the material list.
        String materialListJson = new Gson().toJson(materialList);
        //Add it to the bundle.
        Bundle args = new Bundle();
        args.putString("materialList", materialListJson);
        //Set the bundle to the fragment and return it.
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the data from bundle and deserialize it.
        String json = getArguments().getString("materialList");
        materialList = Deserializer.toMaterialList(json);
        //Create User Shared Preferences Helper.
        dataSource = new MaterialListsDataSource(
                getString(R.string.user_material_lists),
                getActivity()
        );
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Activate options menu.
        setHasOptionsMenu(true);
        // Inflate the fragment layout
        View view = inflater.inflate(R.layout.material_list_fragment, container, false);
        // Get the name of the list.
        listName = materialList.getName();
        // Set the title to the list name.
        titleTV = view.findViewById(R.id.titleTV);
        titleTV.setText(listName);
        // Set a click listener that would open a dialog to edit the Title.
        titleTV.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                // LOAD A DIALOG TO EDIT THE LIST NAME.
                SingleInputDialog f = SingleInputDialog.newInstance("Edit List Name", listName);
                // Set target fragment
                f.setTargetFragment(MaterialListFragment.this, 0);
                // Show fragment
                f.show(getActivity().getSupportFragmentManager(), "SingleInputDialog");

                return false;
            }
        });
        // Get the recyclerView from the parent fragment view.
        RecyclerView rv = view.findViewById(R.id.recyclerView);
        // Instantiate the adaptor
        adapter = new RecyclerViewAdapter();
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(context));
        // Set an item click listener that would load a dialog for editing the clicked Models

        // Swipe and Drag functionality.
        SwipeAndDragHelper swipeAndDragHelper = new SwipeAndDragHelper();
        ItemTouchHelper touchHelper = new ItemTouchHelper(swipeAndDragHelper);
        touchHelper.attachToRecyclerView(rv);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //Add the add (+) button to the root menu.
        MenuItem item = menu.add(Menu.NONE, R.id.action_add, 10, R.string.action_add);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        item.setIcon(R.drawable.ic_add_white_24dp);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                //Load add material dialog.
                AddMaterialDialog f = AddMaterialDialog.newInstance();
                f.setTargetFragment(MaterialListFragment.this, 0);
                f.show(getActivity().getSupportFragmentManager(), f.getClass().getSimpleName());
                return true;
            default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    //----------------------------- Dialogs ------------------------------//

    public void OnSingleInputDialogSubmit(String newName) {
        //Set the title of the list to the user input.
        titleTV.setText(newName);
        //Rename the current materialList.
        materialList.setName(newName);
        //If the materialList is empty, it does not exist in SP yet.
        if (materialList.size() > 0) {
            //Rename the current materialList key in shared preferences
            dataSource.renameKey(listName, newName);
            //Rename locally.
            listName = newName;
        }
    }

    @Override
    public void onEditMaterialDialogSubmit(Material selectedMaterial) {
        materialList.replace(selectedPosition, selectedMaterial);
        adapter.notifyDataSetChanged();
        //Update list in shared preferences
        dataSource.put(materialList);
    }

    @Override
    public void onAddMaterialDialogSubmit(Material material) {
        materialList.add(material);
        adapter.notifyDataSetChanged();
        //Update list in shared preferences
        dataSource.put(materialList);
    }

    //------------------------------- Adapter -------------------------------//

    public class RecyclerViewAdapter extends RecyclerView.Adapter {

        @Override
        public int getItemCount() {
            return materialList.size();
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
            Material material = materialList.get(position);
            itemViewHolder viewHolder = (itemViewHolder) holder;
            viewHolder.columnLeftTV.setText(material.getName());
            viewHolder.columnRightTV.setText(String.valueOf(material.getPrice()));
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
                        // LOAD ADD EDIT MATERIAL DIALOG.
                        // Get the clicked material.
                        Material clickedMaterial = materialList.get(getAdapterPosition());
                        EditMaterialDialog f = EditMaterialDialog
                                .newInstance(clickedMaterial);
                        // Set the target fragment for this dialog.
                        f.setTargetFragment(MaterialListFragment.this, 0);
                        f.show(getActivity().getSupportFragmentManager(), "EditMaterialDialog");

                        // MAKE THE CLICKED POSITION GLOBALLY ACCESSIBLE
                        selectedPosition = getAdapterPosition();
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
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return true;
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            int oldPosition = viewHolder.getAdapterPosition();
            int newPosition = target.getAdapterPosition();
            Material material = materialList.get(oldPosition);
            materialList.remove(oldPosition);
            materialList.add(newPosition, material);
            adapter.notifyItemMoved(oldPosition, newPosition);
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            //Get position of the removed material
            int position = viewHolder.getAdapterPosition();
            //Remove it from the local list.
            materialList.remove(position);
            adapter.notifyItemRemoved(position);
            //Update the source list by overwriting it with the new list.
            dataSource.put(materialList);
        }
    }

}
// Toast.makeText(context, "" + material, Toast.LENGTH_SHORT).show();
