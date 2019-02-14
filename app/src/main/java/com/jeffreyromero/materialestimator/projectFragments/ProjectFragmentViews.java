package com.jeffreyromero.materialestimator.projectFragments;


import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jeffreyromero.materialestimator.R;
import com.jeffreyromero.materialestimator.data.Deserializer;
import com.jeffreyromero.materialestimator.models.BaseItem;
import com.jeffreyromero.materialestimator.models.Project;
import com.jeffreyromero.materialestimator.utilities.CustomRecyclerView;
import com.jeffreyromero.materialestimator.utilities.PrimaryActionModeCallBack;

import java.util.Locale;

/**
 * Displays the list of project associated with it's hosting project.
 */
public class ProjectFragmentViews extends Fragment {

    private static final double INCHES_TO_FEET = 0.0833;
    private static final String PROJECT = "project";
    private ItemsAdapter itemsAdapter;
    private Project project;

    public ProjectFragmentViews() {
        // Required empty public constructor
    }

    public static ProjectFragmentViews newInstance(Project project) {
        ProjectFragmentViews fragment = new ProjectFragmentViews();
        Bundle args = new Bundle();
        String json = new Gson().toJson(project);
        args.putString(PROJECT, json);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String json = getArguments().getString(PROJECT);
            project = Deserializer.toProject(json);
        }
        itemsAdapter = new ItemsAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.project_fragment_item_list_view, container, false);

        // Force the recreation of the original xml options menu that has been modified by other fragments
        getActivity().invalidateOptionsMenu();

        //Set up the recyclerView.
        CustomRecyclerView rv = view.findViewById(R.id.item_list_view_rv);
        rv.setEmptyView(view.findViewById(R.id.empty_view_ll));
        rv.setAdapter(itemsAdapter);

        return view;
    }

    public void refreshItemsAdapter(){
        //TODO - Create stackOverflow background animation.
        itemsAdapter.notifyDataSetChanged();
    }

    //------------------------------- ItemsAdapter -------------------------------//

    public class ItemsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        //View types
        private final int ITEM_VIEW = 0;
        private final int TOTAL_VIEW = 1;
        private ActionMode mActionMode;


        @Override
        public int getItemCount() {
            if (project == null || project.getItems().size() == 0) {
                return 0;
            } else {
                return project.getItems().size() + 1;
            }
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
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == ITEM_VIEW) {
                //Inflate the ITEM_VIEW.
                return new ItemVH(LayoutInflater.from(getActivity()).inflate(
                        R.layout.project_fragment_item_list_view_list_item, parent, false));

            } else if (viewType == TOTAL_VIEW) {
                //Inflate the TOTAL_VIEW.
                return new TotalVH(LayoutInflater.from(getActivity()).inflate(
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
                    BaseItem pi = project.getItems().get(position);
                    ItemVH itemVH = (ItemVH) holder;
//                    if (position % 2 == 0) {
//                        itemVH.itemView.setBackground(
//                                getResources().getDrawable(R.drawable.light_gray_to_light_blue));
//                    } else {
//                        itemVH.itemView.setBackground(
//                                getResources().getDrawable(R.drawable.white_to_light_blue));
//                    }
                    itemVH.nameTV.setText(pi.getName());

                    double length = pi.getLength() * INCHES_TO_FEET;
                    double width = pi.getWidth() * INCHES_TO_FEET;
                    String dimArea = String.format(
                            Locale.US,
                            "%.1fft x %.1fft  (%.0fSF)",
                            length,
                            width,
                            length * width);

                    itemVH.dimAreaTV.setText(dimArea);
                    itemVH.priceTV.setText(String.format(
                            Locale.US,
                            "$%.2f",
                            pi.getTotalPrice()));
                    break;

                case TOTAL_VIEW:
                    TotalVH totalVH = (TotalVH) holder;
                    totalVH.totalTV.setText(String.format(
                            Locale.US,
                            "$%.2f",
                            project.calcTotalPrice()));
                    break;

                default:
                    break;
            }
        }

        private class ItemVH extends RecyclerView.ViewHolder implements
                PrimaryActionModeCallBack.OnActionItemClickListener {

            TextView nameTV;
            TextView dimAreaTV;
            TextView priceTV;
            TransitionDrawable transition;

            ItemVH(final View itemView) {
                super(itemView);
                nameTV = itemView.findViewById(R.id.toolbarTitle);
                dimAreaTV = itemView.findViewById(R.id.dimAreaTV);
                priceTV = itemView.findViewById(R.id.priceTV);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //Inform mListener show ic_project item.
//                        mListener.onProjectFragmentItemClick(
//                                project.getItems().get(getAdapterPosition()));

                    }
                });

                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    // Called when the user long-clicks
                    public boolean onLongClick(View view) {
                        if (mActionMode != null) {
                            return false;
                        }

                        // Start the CAB using the ActionMode.Callback and hold on to it's instance
                        mActionMode = getActivity().startActionMode(
                                new PrimaryActionModeCallBack(
                                        ItemVH.this,
                                        getAdapterPosition()
                                )
                        );
                        transition = (TransitionDrawable) view.getBackground();
                        transition.startTransition(350);
                        return true;
                    }
                });
            }

            @Override
            public void deleteItem(int position) {
                project.deleteItem(position);
                itemsAdapter.notifyItemRemoved(position);
                itemsAdapter.notifyItemChanged(getItemCount() == 0 ? 0 : getItemCount()-1);
                // Update data source
//                projectsSP.replace(project.getName(), project);
            }

            @Override
            public void destroyActionMode() {
                transition.reverseTransition(350);
                mActionMode = null;
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
