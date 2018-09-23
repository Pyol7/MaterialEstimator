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
import com.jeffreyromero.materialestimator.data.MaterialListsDataSource;
import com.jeffreyromero.materialestimator.material.SingleSelectDialog;
import com.jeffreyromero.materialestimator.models.Material;
import com.jeffreyromero.materialestimator.models.MaterialList;
import com.jeffreyromero.materialestimator.models.Project;
import com.jeffreyromero.materialestimator.models.ProjectItem;
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

    private static final String PROJECT = "project";
    private OnFragmentInteractionListener mListener;
    private MaterialListsDataSource userMaterialListsDataSource;
    private ArrayList<MaterialList> userMaterialLists;
    private int selectedMaterialListPosition;
    private SharedPreferences activitySP;
    private ProjectItemAdapter adapter;
    private TextView projectItemNameTV;
    private TextView projectItemListNameTV;
    private ProjectItem projectItem;
    private TextView lengthET;
    private TextView widthET;
    private Project project;
    private View view;
    private Context context;

    public ProjectItemCreatorFragment() {
        // Required empty public constructor
    }

    public static ProjectItemCreatorFragment newInstance(Project project) {
        ProjectItemCreatorFragment fragment = new ProjectItemCreatorFragment();
        Bundle args = new Bundle();
        String json = new Gson().toJson(project);
        args.putString(PROJECT, json);
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

        if (getArguments() != null) {
            String json = getArguments().getString(PROJECT);
            project = Deserializer.toProject(json);
        }

        //Load User material list data source.
        userMaterialListsDataSource =
                new MaterialListsDataSource(getString(R.string.user_material_lists), context);

        //Get all user materialLists.
        userMaterialLists = userMaterialListsDataSource.getAll();

        //Get activity shared preferences.
        activitySP = getActivity().getPreferences(Context.MODE_PRIVATE);

        //Get the stored list position or use the first list.
        selectedMaterialListPosition = activitySP.getInt(
                context.getString(R.string.selected_material_list_position),
                0
        );

        //Create a ProjectItem.
        projectItem = new ProjectItem(getString(R.string.projectItemNameTV_text));

        //Set list to project item.
        projectItem.setMaterialList(userMaterialLists.get(selectedMaterialListPosition));

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

        getActivity().setTitle(project.getName());

        //Show help message if there is no project items in project.
        if (project.getProjectItems().size() == 0) {
            showAddProjectItemMessageDialog();
        }

        //Set project item name and click listener.
        projectItemNameTV = view.findViewById(R.id.projectItemNameTV);
        projectItemNameTV.setText(projectItem.getName());
        projectItemNameTV.setOnClickListener(new showAddNameDialog());
        View projectItemNameIcon = view.findViewById(R.id.projectItemNameIcon);
        projectItemNameIcon.setOnClickListener(new showAddNameDialog());

        //Set the current list name and click listener.
        projectItemListNameTV = view.findViewById(R.id.projectItemListNameTV);
        projectItemListNameTV.setText(projectItem.getMaterialList().getName());
        projectItemListNameTV.setOnClickListener(new showMaterialListSelectDialog());
        View projectItemListNameIcon = view.findViewById(R.id.projectItemListNameIcon);
        projectItemListNameIcon.setOnClickListener(new showMaterialListSelectDialog());

        //Display the current project item list.
        RecyclerView rv = view.findViewById(R.id.recyclerView);
        rv.setHasFixedSize(true);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(context));

        //Calculate quantities based on user input.
        Button btn = view.findViewById(R.id.calcBTN);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get user inputs.
                lengthET = view.findViewById(R.id.lengthTV);
                widthET = view.findViewById(R.id.widthTV);
                //Check the length of the input text to determine if its empty.
                int lengthSize = lengthET.getText().toString().trim().length();
                int widthSize = widthET.getText().toString().trim().length();
                if (lengthSize != 0 && widthSize != 0) {

                    calculateQuantities(view);

                } else {
                    //todo - change to snack bar.
                    Toast.makeText(context, "Enter a length and width", Toast.LENGTH_LONG).show();
                }

            }
        });
        return view;
    }

    private void calculateQuantities(View view) {
        //Get inputs.
        double length = Double.valueOf(lengthET.getText().toString());
        double width = Double.valueOf(widthET.getText().toString());

        //Build the project item.
        projectItem.setLength(length);
        projectItem.setWidth(width);

        //Calculate quantities.
        projectItem.getMaterialList().calculateQuantities(length, width);

        //Update adapter
        adapter.notifyDataSetChanged();

        //Close keyboard and clear focus from inputs.
        InputMethodManager imm = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        lengthET.clearFocus();
        widthET.clearFocus();
    }

    private void showAddProjectItemMessageDialog() {
        //Create and show dialog.
        AddProjectItemMessageDialog f = AddProjectItemMessageDialog.newInstance();
        f.show(getActivity().getSupportFragmentManager(), f.getClass().getSimpleName());
    }

    //Uses SingleInputDialog
    private class showAddNameDialog implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //Open single input dialog to update name.
            SingleInputDialog f = SingleInputDialog.newInstance(getString(R.string.add_name_dialog_title), projectItem.getName());
            // Set target fragment
            f.setTargetFragment(ProjectItemCreatorFragment.this, 0);
            // Show fragment
            f.show(getActivity().getSupportFragmentManager(), f.getClass().getSimpleName());
        }
    }

    @Override
    public void OnSingleInputDialogSubmit(String userInput) {
        projectItem.setName(userInput);
        projectItemNameTV.setText(userInput);
    }

    private class showMaterialListSelectDialog implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //Show select dialog with all userMaterialLists.
            SingleSelectDialog f = SingleSelectDialog.newInstance(
                    getString(R.string.select_list_dialog_title),
                    userMaterialListsDataSource.getAllKeys(),
                    selectedMaterialListPosition
            );
            // Set the target fragment for this dialog.
            f.setTargetFragment(ProjectItemCreatorFragment.this, 0);
            f.show(getActivity().getSupportFragmentManager(), f.getClass().getSimpleName());
        }
    }

    @Override
    public void OnSingleSelectDialogSubmit(int selectedMaterialListPosition) {
        this.selectedMaterialListPosition = selectedMaterialListPosition;
        //Store the selected position to activity SP.
        activitySP.edit().putInt(
                getString(R.string.selected_material_list_position),
                selectedMaterialListPosition
        ).apply();
        projectItem.setMaterialList(userMaterialLists.get(selectedMaterialListPosition));
        projectItemListNameTV.setText(projectItem.getMaterialList().getName());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        //Reload all userMaterialLists just in case the user made changes.
        userMaterialLists = userMaterialListsDataSource.getAll();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //Create the help menu item.
        MenuItem help = menu.add(Menu.NONE, R.id.action_save, 1, R.string.action_save);
        help.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        help.setIcon(R.drawable.ic_save_white_24dp);

        //Create the save menu item.
        MenuItem save = menu.add(Menu.NONE, R.id.action_help, 2, R.string.action_help);
        save.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        save.setIcon(R.drawable.ic_help_outline_white_24dp);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:

                saveProjectItem();
                return true;

            case R.id.action_help:

                showAddProjectItemMessageDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveProjectItem() {
        //Ensure that a project item has a name before saving.
        TextView projectItemNameTV = view.findViewById(R.id.projectItemNameTV);
        String projectItemName = projectItemNameTV.getText().toString();
        if (!projectItemName.equals(getString(R.string.projectItemNameTV_text))) {
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
                    Material material = projectItem.getMaterialList().get(position);
                    ItemVH itemVH = (ItemVH) holder;
                    if (position % 2 == 0) {
                        itemVH.itemView.setBackgroundColor(getResources().getColor(R.color.lightGray));
                    } else {
                        itemVH.itemView.setBackgroundColor(getResources().getColor(R.color.white));
                    }
                    itemVH.nameTV.setText(material.toString());
                    String qup = String.format(
                            Locale.US,
                            "%.0f pcs   @   $%.2f",
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
                nameTV = itemView.findViewById(R.id.nameTV);
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
