package com.jeffreyromero.materialestimator.project;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.jeffreyromero.materialestimator.utilities.CustomRecyclerView;
import com.jeffreyromero.materialestimator.R;
import com.jeffreyromero.materialestimator.data.Deserializer;
import com.jeffreyromero.materialestimator.data.ProjectsDataSource;
import com.jeffreyromero.materialestimator.models.Project;
import com.jeffreyromero.materialestimator.models.ProjectItem;

import java.util.ArrayList;

/**
 * Displays a Project including it's list of ProjectItems.
 * Receives a Project from the activity and
 * Returns the the Project back to the activity on
 */
public class ProjectFragment extends Fragment implements
        ProjectItemCreatorFragment.OnFragmentInteractionListener {

    private static final String PROJECT = "project";
    private OnItemClickListener mListener;
    private ProjectAdapter adapter;
    private Project project;
    private Context context;

    public ProjectFragment() {
        // Required empty public constructor
    }

    public static ProjectFragment newInstance(Project project) {
        ProjectFragment fragment = new ProjectFragment();
        Bundle args = new Bundle();
        String json = new Gson().toJson(project);
        args.putString(PROJECT, json);
        fragment.setArguments(args);
        return fragment;
    }

    public interface OnItemClickListener {
        void onProjectFragmentProjectItemClick(ProjectItem projectItem);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        //Get the hosting activity to implement this callback interface.
        if (context instanceof ProjectFragment.OnItemClickListener) {
            mListener = (OnItemClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ProjectFragment.OnItemClickListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set all fields.
        if (getArguments() != null) {
            String json = getArguments().getString(PROJECT);
            project = Deserializer.toProject(json);
        }
        adapter = new ProjectAdapter(project.getProjectItems());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Show options menu.
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.project_fragment, container, false);

        //Set a title to the toolbar.
        getActivity().setTitle(project.getName());

        //Set up the recyclerView.
        CustomRecyclerView rv = view.findViewById(R.id.customRecyclerView);
        rv.setHasFixedSize(true);
        rv.setEmptyView(view.findViewById(R.id.empty_view));
        rv.setAdapter(adapter);

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //Add the add (+) button to the root menu.
        MenuItem item = menu.add(Menu.NONE, R.id.action_add, 10, R.string.action_add);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        item.setIcon(R.drawable.ic_add_white_24dp);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {

            //Show the ProjectItemCreatorFragment.
            ProjectItemCreatorFragment f = ProjectItemCreatorFragment.newInstance();
            f.setTargetFragment(ProjectFragment.this, 0);
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, f, f.getClass().getSimpleName());
            transaction.addToBackStack(f.getClass().getSimpleName());
            transaction.commit();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onProjectItemCreated(ProjectItem projectItem) {
        //Add the created ProjectItem to the current Project.
        adapter.addProjectItem(projectItem);
        //TODO - Create stackOverflow background animation.
        Toast.makeText(context, "New Item Created", Toast.LENGTH_SHORT).show();
        //Save the project to the project data source.
        ProjectsDataSource pds = new ProjectsDataSource(context);
        pds.put(project);
    }

    //------------------------------- Adapter -------------------------------//

    public class ProjectAdapter extends RecyclerView.Adapter {
        private ArrayList<ProjectItem> projectItems;

        ProjectAdapter(ArrayList<ProjectItem> projectItems) {
            this.projectItems = projectItems;
        }

        public ArrayList<ProjectItem> getProjectItems() {
            return projectItems;
        }

        public void setProjectItems(ArrayList<ProjectItem> projectItems) {
            this.projectItems = projectItems;
            notifyDataSetChanged();
        }

        void addProjectItem(ProjectItem projectItem){
            projectItems.add(projectItem);
            project.setProjectItems(projectItems);
            notifyDataSetChanged();
            //Save project.

        }

        @Override
        public int getItemCount() {
            return projectItems.size();
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Get the inflater
            LayoutInflater inflater = LayoutInflater.from(context);
            // Inflate the item view layout
            View itemView = inflater.inflate(R.layout.list_item_textview_textview, parent, false);
            return new ItemViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ProjectItem item = projectItems.get(position);
            ItemViewHolder viewHolder = (ItemViewHolder) holder;
            viewHolder.columnLeftTV.setText(item.getName());
            viewHolder.columnRightTV.setText(String.valueOf(item.getMaterialList().size()));
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

                        //Inform mListener show project item.
                        mListener.onProjectFragmentProjectItemClick(projectItems.get(getAdapterPosition()));

                    }
                });
            }
        }
    }
}

//Toast.makeText(context, "resumed", Toast.LENGTH_SHORT).show();
