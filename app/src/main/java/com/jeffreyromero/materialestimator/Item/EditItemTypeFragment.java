package com.jeffreyromero.materialestimator.Item;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jeffreyromero.materialestimator.R;
import com.jeffreyromero.materialestimator.data.Deserializer;
import com.jeffreyromero.materialestimator.data.ItemTypesSharedPreference;
import com.jeffreyromero.materialestimator.models.BaseMaterial;
import com.jeffreyromero.materialestimator.models.BaseItem;

import java.util.Locale;

/**
 * Displays an Item Type for editing.
 */
public class EditItemTypeFragment extends Fragment implements
        EditMaterialDialog.OnItemChangeListener {

    private static final String TAG = "EditItemTypeFragment";
    private RecyclerView.Adapter adapter;
    private ItemTypesSharedPreference itemsSP;
    private Context context;
    private BaseItem item;

    public EditItemTypeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public static EditItemTypeFragment newInstance(BaseItem item) {
        EditItemTypeFragment fragment = new EditItemTypeFragment();
        //Serialize the material list.
        String json = new Gson().toJson(item);
        //Add it to the bundle.
        Bundle args = new Bundle();
        args.putString("item", json);
        //Set the bundle to the fragment and return it.
        fragment.setArguments(args);
        return fragment;
    }

    public static String getTAG() {
        return TAG;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the data from bundle and deserialize it.
        String json = getArguments().getString("item");
        item = Deserializer.toItemType(json);
        // Init project item shared preferences
        itemsSP = new ItemTypesSharedPreference(context,
                getString(R.string.item_types_sp_file_name)
        );
        adapter = new RecyclerViewAdapter();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Activate options menu.
        setHasOptionsMenu(true);

        // Inflate the fragment layout
        View view = inflater.inflate(R.layout.item_type_fragment, container, false);

        // Set the title
        getActivity().setTitle(item.getSubType());

        // Get the recyclerView from the parent fragment view.
        RecyclerView rv = view.findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.setAdapter(adapter);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //Add the add (+) button to the root menu.
//        MenuItem item = menu.add(Menu.NONE, R.id.action_add, 10, R.string.action_add);
//        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//        item.setIcon(R.drawable.ic_add_white_24dp);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                //Load add material dialog.
//                AddMaterialDialog f = AddMaterialDialog.newInstance();
//                f.setTargetFragment(EditItemTypeFragment.this, 0);
//                f.show(getActivity().getSupportFragmentManager(), f.getClass().getSimpleName());
                return true;
            default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onEditMaterialDialogSubmit(BaseMaterial material, int position) {
        // Replace material at position with modified material
        item.getMaterialList().replace(position, material);

        // Update Shared Preferences
        itemsSP.replace(item.getSubType(), item);

        adapter.notifyItemChanged(position);

        Toast.makeText(context, "Material updated", Toast.LENGTH_SHORT).show();
    }

    //------------------------------- ItemTypesListAdapter -------------------------------//

    public class RecyclerViewAdapter extends RecyclerView.Adapter {

        @Override
        public int getItemCount() {
            return item.getMaterialList().size();
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
            BaseMaterial material = item.getMaterialList().get(position);
            itemViewHolder viewHolder = (itemViewHolder) holder;
            viewHolder.columnLeftTV.setText(material.getName());
            viewHolder.columnRightTV.setText(String.format(
                    Locale.US,
                    "$%.2f",
                    material.getUnitPrice())
            );
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
                        // Load EditMaterialDialog
                        BaseMaterial clickedMaterial = item.getMaterialList().get(getAdapterPosition());
                        EditMaterialDialog f = EditMaterialDialog
                                .newInstance(clickedMaterial, getAdapterPosition());
                        // Set the target fragment for this dialog.
                        f.setTargetFragment(EditItemTypeFragment.this, 0);
                        f.show(getActivity().getSupportFragmentManager(), f.getClass().getSimpleName());
                    }
                });
            }
        }
    }
}
// Toast.makeText(context, "" + material, Toast.LENGTH_SHORT).show();
