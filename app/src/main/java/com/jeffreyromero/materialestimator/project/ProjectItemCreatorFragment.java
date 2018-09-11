package com.jeffreyromero.materialestimator.project;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jeffreyromero.materialestimator.R;
import com.jeffreyromero.materialestimator.data.MaterialListsDataSource;
import com.jeffreyromero.materialestimator.material.SingleInputDialog;
import com.jeffreyromero.materialestimator.material.SingleSelectDialog;
import com.jeffreyromero.materialestimator.models.Material;
import com.jeffreyromero.materialestimator.models.MaterialList;
import com.jeffreyromero.materialestimator.models.ProjectItem;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Displays the ProjectItem creator.
 * Returns the created ProjectItem to ProjectFragment.
 */
public class ProjectItemCreatorFragment extends Fragment implements
        SingleSelectDialog.OnDialogSubmitListener,
        SingleInputDialog.OnDialogSubmitListener {

    private static final String SELECTED_LIST_POSITION = "Selected List Position";
    private static final String PROJECT_ITEM_NAME = "Untitled";
    private OnFragmentInteractionListener mListener;
    private MaterialListsDataSource userMaterialListsDataSource;
    private ArrayList<MaterialList> userMaterialLists;
    private SharedPreferences activitySP;
    private ProjectItemAdapter adapter;
    private int selectedListPosition;
    private TextView projectItemNameTV;
    private TextView projectItemListNameTV;
    private ProjectItem projectItem;
    private String projectItemName;
    private TextView lengthET;
    private TextView widthET;
    private Context context;
    private double length;
    private double width;

    public ProjectItemCreatorFragment() {
        // Required empty public constructor
    }

    public static ProjectItemCreatorFragment newInstance() {
        return new ProjectItemCreatorFragment();
    }

    public interface OnFragmentInteractionListener {
        void onProjectItemCreated(ProjectItem projectItem);
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
                            "ProjectItemCreatorFragment.OnFragmentInteractionListener");
        }
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Load User material list data source.
        userMaterialListsDataSource =
                new MaterialListsDataSource(getString(R.string.user_material_lists), context);
        //Get all user materialLists.
        userMaterialLists = userMaterialListsDataSource.getAll();
        activitySP = getActivity().getPreferences(Context.MODE_PRIVATE);
        //Use the stored position or set zero.
        selectedListPosition = activitySP.getInt(SELECTED_LIST_POSITION, 0);
        //Create a ProjectItem using the stored item name or the default name.
        projectItem = new ProjectItem(activitySP.getString(PROJECT_ITEM_NAME, PROJECT_ITEM_NAME));
        projectItemName = projectItem.getName();
        //Instantiate the adapter
        adapter = new ProjectItemAdapter(userMaterialLists.get(selectedListPosition));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Show options menu.
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        final View view = inflater.inflate(
                R.layout.project_item_creator_fragment,
                container,
                false
        );
        //Display the selected list.
        RecyclerView rv = view.findViewById(R.id.recyclerView);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(context));

        //Set project item name and edit button.
        projectItemNameTV = view.findViewById(R.id.projectItemNameTV);
        projectItemNameTV.setText(projectItemName);
        projectItemNameTV.setOnClickListener(new AddNameDialog());
        ImageView projectItemNameEditBtn = view.findViewById(R.id.projectItemNameEditBtn);
        projectItemNameEditBtn.setOnClickListener(new AddNameDialog());

        //Set List name
        projectItemListNameTV = view.findViewById(R.id.projectItemListNameTV);
        projectItemListNameTV.setText("( " + adapter.getSelectedList() + " )");

        //Set total cost.
        TextView totalTV = view.findViewById(R.id.totalTV);
        totalTV.setText(String.format(
                Locale.US,
                "$%.2f",
                projectItem.getTotalPrice()));

        //Calculate quantities based on user input.
        Button btn = view.findViewById(R.id.calcBTN);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get user inputs.
                lengthET = view.findViewById(R.id.lengthTV);
                widthET = view.findViewById(R.id.widthTV);
                int lengthSize = lengthET.getText().toString().trim().length();
                int widthSize = widthET.getText().toString().trim().length();
                if (lengthSize != 0 && widthSize != 0){

                    calculateMaterialAndCreateProjectItem(view);

                } else {
                    //todo - change to snack bar.
                    Toast.makeText(context, "Enter a length and width", Toast.LENGTH_LONG).show();
                }

            }
        });
        return view;
    }

    private void calculateMaterialAndCreateProjectItem(View view){
        //Ensure that a project item has a name.
        TextView projectItemNameTV = view.findViewById(R.id.projectItemNameTV);
        String projectItemName = projectItemNameTV.getText().toString();
        if (!projectItemName.equals(PROJECT_ITEM_NAME)) {

            length = Double.valueOf(lengthET.getText().toString());
            width = Double.valueOf(widthET.getText().toString());

            //Build the project item.
            projectItem.setLength(length);
            projectItem.setWidth(width);
            projectItem.setMaterialList(adapter.getSelectedList());

            //Calculate quantities.
            adapter.calculateQuantities(length, width);

            //Calculate and set total cost.
            projectItem.calcTotalPrice();
            TextView totalTV = view.findViewById(R.id.totalTV);
            totalTV.setText(String.format(
                    Locale.US,
                    "$%.2f",
                    projectItem.getTotalPrice()));

            //Close keyboard and clear focus from inputs.
            InputMethodManager imm = (InputMethodManager) getActivity()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            lengthET.clearFocus();
            widthET.clearFocus();

        } else {
            //todo - change to snack bar.
            Toast.makeText(context, "Add a name to this project item", Toast.LENGTH_LONG).show();
        }
    }

    private class AddNameDialog implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //Open single input dialog to update name.
            SingleInputDialog f = SingleInputDialog.newInstance(getString(R.string.add_name_dialog_title), projectItemName);
            // Set target fragment
            f.setTargetFragment(ProjectItemCreatorFragment.this, 0);
            // Show fragment
            f.show(getActivity().getSupportFragmentManager(), f.getClass().getSimpleName());
        }
    }

    private void addNameDialog(View v){
        //Open single input dialog to update name.
        SingleInputDialog f = SingleInputDialog.newInstance(getString(R.string.add_name_dialog_title), projectItemName);
        // Set target fragment
        f.setTargetFragment(ProjectItemCreatorFragment.this, 0);
        // Show fragment
        f.show(getActivity().getSupportFragmentManager(), f.getClass().getSimpleName());
    }

    @Override
    public void onResume() {
        super.onResume();
        //Reload al  userMaterialLists to catch any changes.
        userMaterialLists = userMaterialListsDataSource.getAll();
        //Set the selected list or the first list to the adapter.
        selectedListPosition = activitySP.getInt(SELECTED_LIST_POSITION, 0);
        if (selectedListPosition <= userMaterialLists.size()) {
            adapter.setList(userMaterialLists.get(selectedListPosition));
        } else {
            adapter.setList(userMaterialLists.get(0));
        }
        projectItemListNameTV.setText("( " + adapter.getSelectedList() + " )");
        //Use the stored item name or set a name.
        projectItemName = activitySP.getString(PROJECT_ITEM_NAME, PROJECT_ITEM_NAME);
        projectItemNameTV.setText(projectItemName);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //Create the save menu item.
        MenuItem itemSave = menu.add(Menu.NONE, R.id.action_save, 2, R.string.action_save);
        itemSave.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        itemSave.setIcon(R.drawable.ic_save_white_24dp);

        MenuItem itemList = menu.add(Menu.NONE, R.id.action_material_lists, 1, R.string.action_material_lists);
        itemList.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        itemList.setIcon(R.drawable.ic_list_white_24dp);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_material_lists:
                //Show select dialog with all userMaterialLists.
                SingleSelectDialog f = SingleSelectDialog.newInstance(
                        getString(R.string.select_list_dialog_title),
                        userMaterialListsDataSource.getAllKeys(),
                        selectedListPosition
                );
                // Set the target fragment for this dialog.
                f.setTargetFragment(ProjectItemCreatorFragment.this, 0);
                f.show(getActivity().getSupportFragmentManager(), f.getClass().getSimpleName());
                return true;

            case R.id.action_save:
                //Pass the created ProjectItem to the hosting fragment.
                mListener.onProjectItemCreated(projectItem);
                //Reset the projectItem name in activity SP.
                activitySP.edit().putString(PROJECT_ITEM_NAME, PROJECT_ITEM_NAME).apply();
                //Redirect back to hosting fragment.
                getFragmentManager().popBackStackImmediate();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OnSingleInputDialogSubmit(String userInput) {
        projectItemName = userInput;
        projectItemNameTV.setText(userInput);
        //Store the item name to activity SP.
        activitySP.edit().putString(PROJECT_ITEM_NAME, userInput).apply();
    }

    @Override
    public void OnSingleSelectDialogSubmit(int selectedListPosition) {
        this.selectedListPosition = selectedListPosition;
        //Store the selected position to activity SP.
        activitySP.edit().putInt(SELECTED_LIST_POSITION, selectedListPosition).apply();
        adapter.setList(userMaterialLists.get(selectedListPosition));
        projectItemListNameTV.setText("( " + adapter.getSelectedList() + " )");
    }

    //------------------------------- Adapter -------------------------------//

    public class ProjectItemAdapter extends RecyclerView.Adapter {
        private MaterialList selectedList;

        ProjectItemAdapter(MaterialList selectedList) {
            this.selectedList = selectedList;
        }

        void setList(MaterialList selectedList) {
            this.selectedList = selectedList;
            notifyDataSetChanged();
        }

        MaterialList getSelectedList() {
            return selectedList;
        }

        public void calculateQuantities(double length, double width) {
            selectedList.calculateQuantities(length, width);
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return selectedList.size();
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Get the inflater
            LayoutInflater inflater = LayoutInflater.from(context);
            // Inflate the item view layout
            View itemView = inflater.inflate(R.layout.project_item_list_item, parent, false);
            return new ItemViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            Material material = selectedList.get(position);
            ItemViewHolder viewHolder = (ItemViewHolder) holder;
            if (position %2 == 0){
                viewHolder.itemView.setBackgroundColor(getResources().getColor(R.color.lightGray));
            } else {
                viewHolder.itemView.setBackgroundColor(getResources().getColor(R.color.white));
            }
            viewHolder.nameTV.setText(material.toString());
            String qup = String.format(
                    Locale.US,
                    "%.0fpcs  @  $%.2f",
                    material.getQuantity(),
                    material.getUnitPrice());
            viewHolder.quantityUnitPriceTV.setText(qup);
            viewHolder.priceTV.setText(String.format(
                    Locale.US,
                    "$%.2f",
                    material.getPrice()));
        }

        private class ItemViewHolder extends RecyclerView.ViewHolder {
            TextView nameTV;
            TextView quantityUnitPriceTV;
            TextView priceTV;

            ItemViewHolder(final View itemView) {
                super(itemView);
                nameTV = itemView.findViewById(R.id.nameTV);
                quantityUnitPriceTV = itemView.findViewById(R.id.quantityUnitPriceTV);
                priceTV = itemView.findViewById(R.id.priceTV);
            }
        }
    }
}
//Toast.makeText(context, "" + materialList + " " + position, Toast.LENGTH_SHORT).show();
