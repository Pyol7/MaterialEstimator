package com.jeffreyromero.materialestimator.project;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.jeffreyromero.materialestimator.MainActivity;
import com.jeffreyromero.materialestimator.utilities.CustomRecyclerView;
import com.jeffreyromero.materialestimator.R;
import com.jeffreyromero.materialestimator.data.ProjectsDataSource;
import com.jeffreyromero.materialestimator.models.Project;
import com.jeffreyromero.materialestimator.utilities.PrimaryActionModeCallBack;

import java.util.ArrayList;

/**
 * Displays all the stored projects.
 * Returns the clicked project to MainActivity.
 */
public class ProjectsFragment extends Fragment {

    private static final String PROJECTS = "projects";
    private ProjectsDataSource projectsdataSource;
    private OnItemClickListener mListener;
    private ArrayList<Project> projects;
    private ProjectsAdapter adapter;
    private Context context;

    public ProjectsFragment() {
        // Required empty public constructor
    }

    public static ProjectsFragment newInstance() {
        return new ProjectsFragment();
    }

    public interface OnItemClickListener {
        void onProjectsFragmentItemClick(Project project);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        if (context instanceof OnItemClickListener) {
            mListener = (OnItemClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ProjectsFragment.OnItemClickListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Init ProjectsDataSource
        projectsdataSource = new ProjectsDataSource(context);
        // Get array list of projects from SP
        projects = projectsdataSource.getAll();
        // Init list adapter
        adapter = new ProjectsAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Show options menu.
        setHasOptionsMenu(true);
        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.projects_fragment, container, false);
        //Set title
        getActivity().setTitle(R.string.nav_projects);
        //Get the recyclerView from the parent fragment view.
        CustomRecyclerView rv = view.findViewById(R.id.customRecyclerView);
        rv.setEmptyView(view.findViewById(R.id.emptyView));
        rv.setAdapter(adapter);

        // Enable drawer navigation for this fragment
        ((MainActivity)getActivity()).enableDrawerNavigation();

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //Create the add menu item
        MenuItem item = menu.add(Menu.NONE, R.id.action_add, 10, R.string.action_add);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        item.setIcon(R.drawable.ic_add_white_24dp);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            addProject();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addProject() {
        // Inflate the view
        final View dialogView = getActivity().getLayoutInflater()
                .inflate(R.layout.add_project_dialog, null);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle(getActivity().getString(R.string.add_project_dialog_title));
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Get user input
                EditText nameET = dialogView.findViewById(R.id.nameET);
                String name = nameET.getText().toString();
                // Create new project
                Project project = new Project(name);
                // Add new project to SP
                projectsdataSource.put(project);
                // Get updated list of projects
                projects = projectsdataSource.getAll();
                adapter.notifyDataSetChanged();
                //Pass the new project to MainActivity
                mListener.onProjectsFragmentItemClick(project);

            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //
            }
        });
        AlertDialog alertDialog = dialogBuilder.create();
        //Show keyboard.
        alertDialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        // Show dialog
        alertDialog.show();
    }

    //------------------------------- Adapter -------------------------------//

    public class ProjectsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private ActionMode mActionMode;

        @Override
        public int getItemCount() {
            return projects == null ? 0 : projects.size();
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Get the inflater
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            // Inflate the item view layout
            View itemView = inflater.inflate(
                    R.layout.list_item_textview_textview,
                    parent,
                    false
            );
            return new ItemViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            Project project = projects.get(position);
            ItemViewHolder viewHolder = (ItemViewHolder) holder;
            viewHolder.columnLeftTV.setText(project.getName());
            viewHolder.columnRightTV.setText(String.valueOf(project.getDateCreated()));
        }

        private class ItemViewHolder extends RecyclerView.ViewHolder implements
                PrimaryActionModeCallBack.OnActionItemClickListener {

            TextView columnLeftTV;
            TextView columnRightTV;
            TransitionDrawable transition;

            ItemViewHolder(final View itemView) {
                super(itemView);
                columnLeftTV = itemView.findViewById(R.id.columnLeftTV);
                columnRightTV = itemView.findViewById(R.id.columnRightTV);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //Pass the clicked project to MainActivity.
                        mListener.onProjectsFragmentItemClick(projects.get(getAdapterPosition()));

                    }
                });

                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    public boolean onLongClick(View view) {
                        if (mActionMode != null) {
                            return false;
                        }

                        // Start the CAB using the ActionMode.Callback and hold on to it's instance
                        mActionMode = getActivity().startActionMode(
                                new PrimaryActionModeCallBack(
                                        ItemViewHolder.this,
                                        getAdapterPosition()
                                )
                        );
                        transition = (TransitionDrawable) view.getBackground();
                        transition.startTransition(350);
                        return true;
                    }
                });
            }
            // PrimaryActionModeCallBack interface callback methods
            @Override
            public void deleteItem(final int position) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder
                        .setTitle("Delete project?")
                        .setPositiveButton("Delete",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {

                                // Remove the selected project
                                projectsdataSource.remove(projects.get(position).getName());
                                projects = projectsdataSource.getAll();
                                adapter.notifyDataSetChanged();

                            }
                        })
                        .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }

            @Override
            public void destroyActionMode() {
                transition.reverseTransition(350);
                mActionMode = null;
            }
        }
    }
}
