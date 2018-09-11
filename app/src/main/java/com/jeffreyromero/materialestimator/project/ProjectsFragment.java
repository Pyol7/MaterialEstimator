package com.jeffreyromero.materialestimator.project;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeffreyromero.materialestimator.CustomRecyclerView;
import com.jeffreyromero.materialestimator.R;
import com.jeffreyromero.materialestimator.data.ProjectsDataSource;
import com.jeffreyromero.materialestimator.models.Project;

import java.util.ArrayList;

/**
 * Displays all the projects from shared preferences.
 * Returns the clicked project to the listener.
 */
public class ProjectsFragment extends Fragment implements
        AddProjectDialog.OnCreateListener {

    private OnItemClickListener mListener;
    private ProjectsDataSource dataSource;
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
        //Get the data source.
        dataSource = new ProjectsDataSource(context);
        // Setup the adaptor with the list of projects
        adapter = new ProjectsAdapter(dataSource.getAll());
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
        rv.setHasFixedSize(true);
        rv.setEmptyView(view.findViewById(R.id.empty_view));
        rv.setAdapter(adapter);

        return view;
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
            //Load AddProjectDialog.
            AddProjectDialog f = AddProjectDialog.newInstance();
            f.setTargetFragment(ProjectsFragment.this, 0);
            f.show(getActivity().getSupportFragmentManager(), f.getClass().getSimpleName());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.setProjects(dataSource.getAll());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onAddProjectDialogSubmit(String name, String description) {
        Project project = new Project(name);
        project.setDescription(description);
        //Update local list.
        adapter.addProject(project);
        adapter.notifyDataSetChanged();
        //Update stored list.
        dataSource.put(project);
        //Pass the clicked project to the listener.
        mListener.onProjectsFragmentItemClick(project);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    //------------------------------- Adapter -------------------------------//

    public class ProjectsAdapter extends RecyclerView.Adapter {
        private ArrayList<Project> projects;

        ProjectsAdapter(ArrayList<Project> projects) {
            this.projects = projects;
        }

        void setProjects(ArrayList<Project> projects) {
            this.projects = projects;
            notifyDataSetChanged();
        }

        void addProject(Project project){
            projects.add(project);
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            //projects == null ? 0 : projects.size();
            return projects.size();
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

        private class ItemViewHolder extends RecyclerView.ViewHolder {
            TextView columnLeftTV;
            TextView columnRightTV;

            ItemViewHolder(final View itemView) {
                super(itemView);
                columnLeftTV = itemView.findViewById(R.id.columnLeftTV);
                columnRightTV = itemView.findViewById(R.id.columnRightTV);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //Pass the clicked project to the listener.
                        mListener.onProjectsFragmentItemClick(projects.get(getAdapterPosition()));

                    }
                });
            }
        }
    }
}
