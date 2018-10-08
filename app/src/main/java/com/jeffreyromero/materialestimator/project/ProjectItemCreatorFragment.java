package com.jeffreyromero.materialestimator.project;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jeffreyromero.materialestimator.R;
import com.jeffreyromero.materialestimator.data.Deserializer;
import com.jeffreyromero.materialestimator.data.ProjectItemsSharedPreferences;
import com.jeffreyromero.materialestimator.material.SingleSelectDialog;
import com.jeffreyromero.materialestimator.models.BaseMaterial;
import com.jeffreyromero.materialestimator.models.MaterialList;
import com.jeffreyromero.materialestimator.models.Project;
import com.jeffreyromero.materialestimator.models.ProjectItem;
import com.jeffreyromero.materialestimator.models.defaultProjectItems.DroppedCeiling;
import com.jeffreyromero.materialestimator.models.defaultProjectItems.DrywallCeiling;
import com.jeffreyromero.materialestimator.models.defaultProjectItems.DrywallPartition;
import com.jeffreyromero.materialestimator.utilities.SingleInputDialog;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Displays the ProjectItem creator.
 * Returns the created ProjectItem to ProjectFragment.
 */
public class ProjectItemCreatorFragment extends Fragment implements
        SingleSelectDialog.OnDialogSubmitListener,
        SingleInputDialog.OnDialogSubmitListener {

    private static final double FEET_TO_INCHES = 12;
    private static final String PROJECT_NAME = "projectName";
    private static final String PROJECT_ITEM = "projectItem";
    private ProjectItemsSharedPreferences projectItemsSP;
    private OnFragmentInteractionListener mListener;
    private int selectedProjectItemPosition;
    private SharedPreferences activitySP;
    private ProjectItemAdapter adapter;
    private RecyclerView recyclerView;
    private ProjectItem projectItem;
    private TextView lengthET;
    private TextView widthET;
    private Context context;
    private View view;

    public ProjectItemCreatorFragment() {
        // Required empty public constructor
    }

    public static ProjectItemCreatorFragment newInstance(String projectName) {
        ProjectItemCreatorFragment fragment = new ProjectItemCreatorFragment();
        Bundle args = new Bundle();
        args.putString(PROJECT_NAME, projectName);
        fragment.setArguments(args);
        return fragment;
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

        // Init project item shared preferences
        projectItemsSP = new ProjectItemsSharedPreferences(
                getString(R.string.default_project_items_key),
                context
        );

        //Init main activity shared preferences.
        activitySP = getActivity().getPreferences(Context.MODE_PRIVATE);

        if (savedInstanceState == null){
            // Use the stored project item or the one at position zero
            selectedProjectItemPosition = activitySP.getInt(
                    context.getString(R.string.selected_project_item_position_key),
                    0
            );
            projectItem = projectItemsSP.get(selectedProjectItemPosition);
        } else {
            // Get the project item currently in use from the savedInstanceState bundle
            projectItem = Deserializer.toProjectItem(savedInstanceState.getString(PROJECT_ITEM));
        }

        //Instantiate the adapter.
        adapter = new ProjectItemAdapter();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Show options menu.
        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        view = inflater.inflate(
                R.layout.project_item_creator_fragment,
                container,
                false
        );

        //Set a title to the toolbar.
//        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
//        actionBar.setTitle(project.getName());
//        actionBar.setSubtitle(getString(R.string.projectItemCreatorFragment_title));

        if (getArguments() != null) {
            getActivity().setTitle(getArguments().getString(PROJECT_NAME));
        }

        //Show help message if there is no project items in project.
//        if (project.getProjectItems().size() == 0) {
//            showAddProjectItemMessageDialog();
//        }

        //Set project item name and click listener.
        TextView projectItemNameTV = view.findViewById(R.id.projectItemNameTV);
        projectItemNameTV.setText(projectItem.getName());
        projectItemNameTV.setOnClickListener(new showAddNameDialog());

        //Set the current list name and click listener.
        TextView projectItemListNameTV = view.findViewById(R.id.projectItemListNameTV);
        projectItemListNameTV.setText(projectItem.getMaterialList().getName());
        projectItemListNameTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProjectItemSelectDialog();
            }
        });

        //Display the current project item list.
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        //Calculate quantities based on user input.
        Button btn = view.findViewById(R.id.calcBTN);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get user inputs views.
                lengthET = view.findViewById(R.id.lengthTV);
                widthET = view.findViewById(R.id.widthTV);
                //Check the length of the input text to determine if its empty.
                int lengthSize = lengthET.getText().toString().trim().length();
                int widthSize = widthET.getText().toString().trim().length();
                if (lengthSize != 0 && widthSize != 0) {

                    calculateQuantities();

                } else {
                    //todo - change to snack bar.
                    Toast.makeText(context, "Enter a length and width", Toast.LENGTH_LONG).show();
                }

            }
        });
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(PROJECT_ITEM, new Gson().toJson(projectItem));
    }

    private void calculateQuantities() {
        //Get inputs.
        double length =
                Double.valueOf(lengthET.getText().toString()) * FEET_TO_INCHES;
        double width =
                Double.valueOf(widthET.getText().toString()) * FEET_TO_INCHES;

        //Calculate quantities.
        if (projectItem.getMaterialList().getName().equals("Dropped Ceiling")) {
            DroppedCeiling droppedCeiling = (DroppedCeiling) projectItem;
            droppedCeiling.calcQuantities(length, width);
        } else if (projectItem.getMaterialList().getName().equals("Drywall Ceiling")) {
            DrywallCeiling drywallCeiling = (DrywallCeiling) projectItem;
            drywallCeiling.calcQuantities(length, width);
        } else if (projectItem.getMaterialList().getName().equals("Drywall Partition")) {
            DrywallPartition drywallPartition = (DrywallPartition) projectItem;
            drywallPartition.calcQuantities(length, width);
        }

        //Update adapter
        adapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(adapter.getItemCount()-1);


        //Close keyboard and clear focus from inputs.
        InputMethodManager imm = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        lengthET.clearFocus();
        widthET.clearFocus();
    }

    private void showProjectItemSelectDialog (){
        SingleSelectDialog f = SingleSelectDialog.newInstance(
                getString(R.string.select_project_item_type_dialog_title),
                projectItemsSP.getAllKeys(),
                selectedProjectItemPosition
        );
        f.setTargetFragment(ProjectItemCreatorFragment.this, 0);
        f.show(getActivity().getSupportFragmentManager(), f.getClass().getSimpleName());
    }

    private void showAddProjectItemMessageDialog() {
        //Create and show dialog.
        AddProjectItemMessageDialog f = AddProjectItemMessageDialog.newInstance();
        f.show(getActivity().getSupportFragmentManager(), f.getClass().getSimpleName());
    }

    private class showAddNameDialog implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            SingleInputDialog f = SingleInputDialog.newInstance(
                    getString(R.string.add_name_dialog_title),
                    projectItem.getName()
            );
            f.setTargetFragment(ProjectItemCreatorFragment.this, 0);
            f.show(getActivity().getSupportFragmentManager(), f.getClass().getSimpleName());
        }
    }

    @Override
    public void OnSingleInputDialogSubmit(String newName) {
        projectItem.setName(newName);
        TextView projectItemNameTV = view.findViewById(R.id.projectItemNameTV);
        projectItemNameTV.setText(newName);
    }

    @Override
    public void OnSingleSelectDialogSubmit(int selectedProjectItemPosition) {
        this.selectedProjectItemPosition = selectedProjectItemPosition;
        // Store the selected position to activity SP.
        activitySP.edit().putInt(
                getString(R.string.selected_project_item_position_key),
                selectedProjectItemPosition
        ).apply();
        ProjectItem selectedProjectItem = projectItemsSP.get(selectedProjectItemPosition);
        // Keep user provided name
        selectedProjectItem.setName(projectItem.getName());
        projectItem = selectedProjectItem;
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.project_item_creator_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveProjectItem();
                return true;
            case R.id.action_settings:
                showSettingsDialog();
                return true;
            case R.id.action_help:
                showAddProjectItemMessageDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSettingsDialog() {
        getActivity().getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_container, new ProjectItemCreatorSettingsFragment()).
                addToBackStack(null).
                commit();
    }

    private void saveProjectItem() {
        //Ensure that a project item has a name before saving.
        TextView projectItemNameTV = view.findViewById(R.id.projectItemNameTV);
        String projectItemName = projectItemNameTV.getText().toString();
        if (!projectItemName.equals(getString(R.string.untitled))) {
            //Pass the created ProjectItem to the hosting fragment.
            mListener.onProjectItemCreated(projectItem);
            //Redirect back to hosting fragment.
            getFragmentManager().popBackStackImmediate();
        } else {
            //todo - change to snack bar.
            Toast.makeText(context, "Enter a name before saving", Toast.LENGTH_LONG).show();
        }
    }

    //------------------------------- Adapter -------------------------------//

    public class ProjectItemAdapter extends RecyclerView.Adapter {
        //View types
        private final int ITEM_VIEW = 0;
        private final int TOTAL_VIEW = 1;

        @Override
        public int getItemCount() {
            return projectItem.getMaterialList() == null ? 0 :
                    projectItem.getMaterialList().size() + 1;
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
                return new ItemVH(LayoutInflater.from(context).inflate(
                        R.layout.project_item_creator_list_item, parent, false));

            } else if (viewType == TOTAL_VIEW) {
                // Inflate the TOTAL_VIEW.
                return new TotalVH(LayoutInflater.from(context).inflate(
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
                    BaseMaterial material = projectItem.getMaterialList().get(position);
                    ItemVH itemVH = (ItemVH) holder;
                    if (position % 2 == 0) {
                        itemVH.itemView.setBackgroundColor(getResources().getColor(R.color.lightGray));
                    } else {
                        itemVH.itemView.setBackgroundColor(getResources().getColor(R.color.white));
                    }
                    itemVH.nameTV.setText(material.toString());
                    String qup = String.format(
                            Locale.US,
                            "%.1f pcs   @   $%.2f",
                            material.getQuantity(),
                            material.getUnitPrice());
                    itemVH.quantityUnitPriceTV.setText(qup);
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
                            projectItem.calcTotalPrice()));
                    break;

                default:
                    break;
            }
        }

        private class ItemVH extends RecyclerView.ViewHolder {
            TextView nameTV;
            TextView quantityUnitPriceTV;
            TextView priceTV;

            ItemVH(final View itemView) {
                super(itemView);
                nameTV = itemView.findViewById(R.id.nameLabelTV);
                quantityUnitPriceTV = itemView.findViewById(R.id.quantityUnitPriceTV);
                priceTV = itemView.findViewById(R.id.priceTV);
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
