package com.jeffreyromero.materialestimator.project;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jeffreyromero.materialestimator.Item.ItemTypeFragment;
import com.jeffreyromero.materialestimator.MainActivity;
import com.jeffreyromero.materialestimator.R;
import com.jeffreyromero.materialestimator.data.Deserializer;
import com.jeffreyromero.materialestimator.data.ItemsDataSource;
import com.jeffreyromero.materialestimator.models.BaseItem;
import com.jeffreyromero.materialestimator.models.MaterialList;
import com.jeffreyromero.materialestimator.utilities.Helper;
import com.jeffreyromero.materialestimator.utilities.SingleSelectDialog;
import com.jeffreyromero.materialestimator.models.BaseMaterial;
import com.jeffreyromero.materialestimator.models.Items.DroppedCeiling;
import com.jeffreyromero.materialestimator.models.Items.DrywallCeiling;
import com.jeffreyromero.materialestimator.models.Items.DrywallPartition;
import com.jeffreyromero.materialestimator.Item.EditQuantityDialog;
import com.jeffreyromero.materialestimator.utilities.SingleTextInputDialog;

import java.util.Locale;

/**
 * Provides a way to create new ProjectItems from a list of ProjectItems
 * stored in shared preferences.
 *
 * An ItemType is copied from it's data source and used to create new items.
 * The created item is then sent back to ProjectFragment to be added
 * to it's list of items.
 */
public class ItemCreatorFragment extends Fragment implements
        SingleSelectDialog.OnDialogSubmitListener,
        SingleTextInputDialog.OnDialogSubmitListener,
        EditQuantityDialog.OnDialogSubmitListener {

    private static final double FEET_TO_INCHES = 12;
    private static final String PROJECT_NAME = "projectName";
    private static final String PROJECT_ITEM = "item";
    private OnFragmentInteractionListener mListener;
    private ItemsDataSource itemsSP;
    private int selectedItemTypePosition;
    private SharedPreferences activitySP;
    private RecyclerView recyclerView;
    private MainActivity mainActivity;
    private ItemAdapter adapter;
    private TextView lengthET;
    private TextView widthET;
    private TextView title;
    private TextView subtitle;
    private BaseItem item;
    private View view;

    public ItemCreatorFragment() {
        // Required empty public constructor
    }

    public static ItemCreatorFragment newInstance(String projectName) {
        ItemCreatorFragment fragment = new ItemCreatorFragment();
        Bundle args = new Bundle();
        args.putString(PROJECT_NAME, projectName);
        fragment.setArguments(args);
        return fragment;
    }

    public interface OnFragmentInteractionListener {
        void onItemCreated(BaseItem item);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //Get the hosting fragment to implement this fragment's callback interface.
        try {
            mListener = (OnFragmentInteractionListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(
                    "The hosting Fragment must implement " +
                            "ItemCreatorFragment.OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Use MainActivity as context
        mainActivity = (MainActivity)getActivity();
        // Init project item shared preferences
        itemsSP = new ItemsDataSource(
                getString(R.string.items_key),
                mainActivity
        );
        //Init main activity shared preferences.
        activitySP = getActivity().getPreferences(Context.MODE_PRIVATE);
        if (savedInstanceState == null){
            // Use the stored item or the one at position zero
            selectedItemTypePosition = activitySP.getInt(
                    mainActivity.getString(R.string.selected_project_item_position_key),
                    0
            );
            item = itemsSP.get(selectedItemTypePosition);
        } else {
            // Use the item currently being modified from the savedInstanceState bundle
            item = Deserializer.toItem(savedInstanceState.getString(PROJECT_ITEM));
        }
        // Instantiate the adapter.
        adapter = new ItemAdapter();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Show options menu.
        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        view = inflater.inflate(
                R.layout.item_creator_fragment,
                container,
                false
        );

        // Get this fragment's toolbar and set it as the action bar in MainActivity.
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        mainActivity.setSupportActionBar(toolbar);

        // Hide default title which shows the app name.
        mainActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Set item name (toolbar title) and click listener.
        title = toolbar.findViewById(R.id.toolbarTitle);
        title.setText(item.getName());
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSingleTextInputDialog(item.getName());
            }
        });

        // Set the current item type and click listener.
        subtitle = toolbar.findViewById(R.id.toolbarSubtitle);
        subtitle.setText(item.getMaterialList().getName());
        subtitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowSingleSelectDialogForItems();
            }
        });

        // Make this fragment's options visible on the main menu
        setHasOptionsMenu(true);

        // Enable up navigation for this fragment
        mainActivity.enableDrawerNavigation(false);

        // Display the current item's material list
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));
        recyclerView.setAdapter(adapter);

        // Get input views
        lengthET = view.findViewById(R.id.lengthET);
        widthET = view.findViewById(R.id.widthET);

        // Set input hint text
        setHints();

        // Calculate quantities based on user input.
        Button btn = view.findViewById(R.id.calcBTN);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                calculateQuantities();

            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.item_creator_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Provide functionality for the home icon.
                // Used when enableDrawerNavigation = false
                mainActivity.onBackPressed();
                return true;
            case R.id.action_save:
                sendCreatedItemToListener();
                return true;
            case R.id.action_settings:
                editItemTypeMaterialSettings();
                return true;
            case R.id.action_help:
                showItemCreatorFragmentHelpDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(PROJECT_ITEM, new Gson().toJson(item));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (lengthET.getText().toString().length() != 0 &&
                widthET.getText().toString().length() != 0){
            calculateQuantities();
        }
    }

    //---------------------------- Callbacks --------------------------

    // Set item name
    private void showSingleTextInputDialog(String name){
        SingleTextInputDialog f = SingleTextInputDialog.newInstance(
                getString(R.string.add_name_dialog_title),
                name
        );
        f.setTargetFragment(ItemCreatorFragment.this, 0);
        f.show(getActivity().getSupportFragmentManager(), f.getClass().getSimpleName());
    }

    @Override
    public void OnSingleTextInputDialogSubmit(String newName) {
        // Set new name to item
        item.setName(newName);
        // Set new name to title
        title.setText(item.getName());
    }

    // Set item type
    private void ShowSingleSelectDialogForItems(){
        SingleSelectDialog f = SingleSelectDialog.newInstance(
                getString(R.string.select_item_type_dialog_title),
                itemsSP.getAllKeys(),
                selectedItemTypePosition
        );
        f.setTargetFragment(ItemCreatorFragment.this, 0);
        f.show(getActivity().getSupportFragmentManager(), f.getClass().getSimpleName());
    }

    @Override
    public void OnSingleSelectDialogSubmit(int selectedItemTypePosition) {
        this.selectedItemTypePosition = selectedItemTypePosition;
        // Store the selected position to activity SP
        activitySP.edit().putInt(
                getString(R.string.selected_project_item_position_key),
                selectedItemTypePosition
        ).apply();
        // Get selected item
        BaseItem selectedItemType = itemsSP.get(selectedItemTypePosition);
        // Keep user provided name
        selectedItemType.setName(item.getName());
        item = selectedItemType;
        // Set item type name to subtitle
        subtitle.setText(item.getMaterialList().getName());
        setHints();
        adapter.notifyDataSetChanged();
    }

    // Edit quantity
    private  void showEditQuantityDialog(int position){
        EditQuantityDialog f = EditQuantityDialog.newInstance(
                position,
                item
        );
        f.setTargetFragment(ItemCreatorFragment.this, 0);
        f.show(getActivity().getSupportFragmentManager(), f.getClass().getSimpleName());
    }

    @Override
    public void OnEditQuantityDialogSubmit(int position, double userInput) {
        // Get material at position and update quantity
        BaseMaterial m = item.getMaterialList().get(position);
        m.setQuantity(userInput);
        adapter.notifyItemChanged(position);
    }

    // Item type material settings
    private void editItemTypeMaterialSettings() {
        Helper.replaceFragment(
                (AppCompatActivity)getActivity(),
                ItemTypeFragment.newInstance(itemsSP.get(selectedItemTypePosition)),
                true
        );
        adapter.notifyDataSetChanged();
    }

    // Calculate quantities
    private void calculateQuantities() {
        // reload the materialList to catch any property changes
        item.setMaterialList(itemsSP.get(selectedItemTypePosition).getMaterialList());
        // Check the length of the input text to determine if its empty.
        if (lengthET.getText().toString().trim().length() != 0 &&
                widthET.getText().toString().trim().length() != 0) {

            // Get inputs
            double x = Double.valueOf(lengthET.getText().toString()) * FEET_TO_INCHES;
            double y = Double.valueOf(widthET.getText().toString()) * FEET_TO_INCHES;

            //todo - input validation?
            // Determine the type and calculate quantities
            switch (item.getSubType()) {
                case "Dropped Ceiling":
                    DroppedCeiling droppedCeiling = (DroppedCeiling) item;
                    droppedCeiling.calcQuantities(x, y);
                    break;
                case "Drywall Ceiling":
                    DrywallCeiling drywallCeiling = (DrywallCeiling) item;
                    drywallCeiling.calcQuantities(x, y);
                    break;
                case "Drywall Partition":
                    DrywallPartition drywallPartition = (DrywallPartition) item;
                    drywallPartition.calcQuantities(x, y);
                    break;
            }

            //Update adapter
            adapter.notifyDataSetChanged();
            recyclerView.scrollToPosition(adapter.getItemCount()-1);

            //Close keyboard
            InputMethodManager imm = (InputMethodManager) getActivity()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

            // Clear focus from inputs
            lengthET.clearFocus();
            widthET.clearFocus();

        } else {
            //todo - change to snack bar.
            Toast.makeText(mainActivity, "A length and width is required", Toast.LENGTH_LONG).show();
        }
    }

    private void setHints(){
        TextInputLayout lengthTIL = view.findViewById(R.id.lengthTIL);
        lengthTIL.setHint(item.getXHintText());
        TextInputLayout widthTIL = view .findViewById(R.id.widthTIL);
        widthTIL.setHint(item.getYHintText());
    }

    private void sendCreatedItemToListener() {
        // Ensure that the modified item has a name before saving.
        TextView itemNameTV = view.findViewById(R.id.toolbarTitle);
        String itemName = itemNameTV.getText().toString();
        if (!itemName.equals(getString(R.string.untitled)) || itemName.equals("")) {
            //Pass the created item to the listener.
            mListener.onItemCreated(item);
            //Redirect back to hosting fragment.
            getFragmentManager().popBackStackImmediate();
        } else {
            //todo - change to snack bar.
            Toast.makeText(mainActivity, "Enter a name before saving", Toast.LENGTH_LONG).show();
        }
    }

    private void showItemCreatorFragmentHelpDialog() {
        ItemCreatorFragmentHelpDialog f = ItemCreatorFragmentHelpDialog.newInstance();
        f.show(getActivity().getSupportFragmentManager(), f.getClass().getSimpleName());
    }

    //------------------------------- Adapter -------------------------------//

    public class ItemAdapter extends RecyclerView.Adapter {
        //View types
        private final int ITEM_VIEW = 0;
        private final int TOTAL_VIEW = 1;

        @Override
        public int getItemCount() {
            return item.getMaterialList() == null ? 0 :
                    item.getMaterialList().size() + 1;
        }

        //Determine which layout to use for the row.
        @Override
        public int getItemViewType(int position) {
            if (position < getItemCount() - 1) {
                return ITEM_VIEW;
            } else {
                return TOTAL_VIEW;
            }
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            if (viewType == ITEM_VIEW) {
                // Inflate the ITEM_VIEW.
                return new ItemVH(LayoutInflater.from(mainActivity).inflate(
                        R.layout.item_creator_list_item, parent, false));

            } else if (viewType == TOTAL_VIEW) {
                // Inflate the TOTAL_VIEW.
                return new TotalVH(LayoutInflater.from(mainActivity).inflate(
                        R.layout.total_list_item, parent, false));

            } else {
                //Throw exception if view type is not found.
                throw new RuntimeException("View type not found");
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            switch (holder.getItemViewType()) {

                case ITEM_VIEW:
                    BaseMaterial material = item.getMaterialList().get(position);
                    // Set background color
                    ItemVH itemVH = (ItemVH) holder;
                    if (position % 2 == 0) {
                        itemVH.itemView.setBackgroundColor(getResources().getColor(R.color.white));
                    } else {
                        itemVH.itemView.setBackgroundColor(getResources().getColor(R.color.lightGray));
                    }
                    // Set values
                    itemVH.nameTV.setText(material.toString());
                    String q = String.format(
                            Locale.US,
                            "%.1f",
                            material.getQuantity());
                    itemVH.quantityTV.setText(q);
                    String u = String.format(
                            Locale.US,
                            "$%.2f",
                            material.getUnitPrice());
                    itemVH.unitPriceTV.setText(u);
                    itemVH.priceTV.setText(String.format(
                            Locale.US,
                            "$%.2f",
                            material.getPrice()));
                    break;

                case TOTAL_VIEW:
                    TotalVH totalVH = (TotalVH) holder;
                    totalVH.totalTV.setText(String.format(
                            Locale.US,
                            "$%.2f",
                            item.calcTotalPrice()));
                    break;

                default:
                    break;
            }
        }

        private class ItemVH extends RecyclerView.ViewHolder {
            TextView nameTV;
            TextView quantityTV;
            TextView unitPriceTV;
            TextView priceTV;

            ItemVH(View itemView) {
                super(itemView);
                nameTV = itemView.findViewById(R.id.toolbarTitle);
                quantityTV = itemView.findViewById(R.id.quantityTV);
                unitPriceTV = itemView.findViewById(R.id.unitPriceTV);
                priceTV = itemView.findViewById(R.id.priceTV);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showEditQuantityDialog(getAdapterPosition());
                    }
                });
            }
        }

        private class TotalVH extends RecyclerView.ViewHolder {
            TextView totalTV;

            TotalVH(View itemView) {
                super(itemView);
                totalTV = itemView.findViewById(R.id.totalTV);
            }
        }
    }
}
//Toast.makeText(context, "" + materialList + " " + position, Toast.LENGTH_SHORT).show();
