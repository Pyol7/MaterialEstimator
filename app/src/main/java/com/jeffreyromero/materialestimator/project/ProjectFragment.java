package com.jeffreyromero.materialestimator.project;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v4.view.MenuItemCompat;
import android.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
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
import com.jeffreyromero.materialestimator.data.ProjectsDataSource;
import com.jeffreyromero.materialestimator.models.BaseMaterial;
import com.jeffreyromero.materialestimator.models.Project;
import com.jeffreyromero.materialestimator.models.ProjectItem;
import com.jeffreyromero.materialestimator.utilities.PrimaryActionModeCallBack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

/**
 * Receives a Project from the activity and displays it.
 */
public class ProjectFragment extends Fragment implements
        ProjectItemCreatorFragment.OnFragmentInteractionListener {

    private static final String LOG_TAG = ProjectFragment.class.getSimpleName();
    private static final double INCHES_TO_FEET = 0.0833;
    private static final String PROJECT = "project";
    private ShareActionProvider mShareActionProvider;
    private MaterialListAdapter materialListAdapter;
    private ProjectItemAdapter projectItemAdapter;
    private OnItemClickListener mListener;
    private Project project;
    private Context context;
    private View view;

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

        if (savedInstanceState == null){
            // Use the provided project
            if (getArguments() != null) {
                String json = getArguments().getString(PROJECT);
                project = Deserializer.toProject(json);
            }
        } else {
            // Get the project item currently in use from the savedInstanceState bundle
            project = Deserializer.toProject(savedInstanceState.getString(PROJECT));
        }

        // If project items is empty show ProjectItemCreator.
//        if (project.getProjectItems().size() == 0) {
//            showProjectItemCreator();
//        }
        // Init list adapters
        projectItemAdapter = new ProjectItemAdapter();
        materialListAdapter = new MaterialListAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Show options menu.
        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.project_fragment, container, false);

        //Set a title to the toolbar.
        getActivity().setTitle(project.getName());

        //Set client, location, dateCreated and description.
        TextView clientTV = view.findViewById(R.id.clientTV);
        TextView locationTV = view.findViewById(R.id.locationTV);
        TextView dateCreatedTV = view.findViewById(R.id.dateCreatedTV);
        clientTV.setText(project.getClient());
        locationTV.setText(project.getLocation());
        dateCreatedTV.setText(project.getDateCreated());

        //Set up the recyclerView.
        RecyclerView rv = view.findViewById(R.id.recyclerView);
        rv.setAdapter(projectItemAdapter);

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(PROJECT, new Gson().toJson(project));
    }

    @Override
    public void onPause() {
        super.onPause();
        // Save altered project to SP
        ProjectsDataSource pds = new ProjectsDataSource(context);
        pds.put(project);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onProjectItemCreated(ProjectItem projectItem) {
        //Add the created ProjectItem to the current Project.
        project.addProjectItem(projectItem);
        projectItemAdapter.notifyItemInserted(project.getProjectItems().size());
        //TODO - Create stackOverflow background animation.
        Toast.makeText(context, "New Item Created", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.project_menu, menu);
        //Fetch and store ShareActionProvider
        MenuItem item = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                showProjectItemCreator();
                return true;
            case R.id.action_share:
                setShareIntent(createIntent());
                return true;
            case R.id.action_project_item_view:
                showProjectItemView();
                return true;
            case R.id.action_material_list_view:
                showMaterialListView();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setShareIntent(Intent intent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(intent);
        }
        else {
            Log.d(LOG_TAG, "Share action provider is null");
        }
    }

    private Intent createIntent(){
        //Create bitmap
        Bitmap mBitmap = createFinalBitmap();
        //Save bitmap to cache directory
        try {
            //Create subdirectory in cache which corresponds to path.filepaths.xml
            File cachePath = new File(getActivity().getCacheDir(), "tempSharedImageDir");
            cachePath.mkdirs();

            //Overwrites this image every time
            FileOutputStream stream = new FileOutputStream(cachePath + "/material_estimator_list.jpg");

            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            stream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        //Read image from cache
        //Get directory: cache/tempSharedImageDir
        File imagePath = new File(getActivity().getCacheDir(), "tempSharedImageDir");
        //Get file: cache/tempSharedImageDir/material_estimator_list.jpg
        File newFile = new File(imagePath, "material_estimator_list.jpg");
        //Create uri:
        // content://com.jeffreyromero.materialestimator.fileprovider/temp_shared_image/tempShareImage.png
        Uri contentUri = FileProvider.getUriForFile(
                getActivity(),
                "com.jeffreyromero.materialestimator.fileprovider",
                newFile
        );
        //Send image via intent
        if (contentUri != null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            //Temp permission for receiving app to read this file
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            shareIntent.setDataAndType(contentUri, context.getContentResolver().getType(contentUri));
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            shareIntent.setType("image/jpg");
            return shareIntent;
        }
        return null;
    }

    private Bitmap createFinalBitmap(){
        //Create individual bitmaps
        Bitmap header = createBitmapFromView(view.findViewById(R.id.headerCL));
        Bitmap recyclerView = createBitmapFromView(view.findViewById(R.id.recyclerView));
        //Combine bitmaps
        return combineBitmaps(header, recyclerView);
    }

    private Bitmap createBitmapFromView(View v) {
        if (v instanceof RecyclerView){
            //Scroll to top for correct measurements
            ((RecyclerView) v).getLayoutManager().scrollToPosition(0);
        }
        v.measure(
                View.MeasureSpec.makeMeasureSpec(v.getWidth(), View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        Bitmap bitmap = Bitmap.createBitmap(
                v.getWidth(),
                v.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(bitmap);
        //Get the view's background
        Drawable bgDrawable = v.getBackground();
        if (bgDrawable != null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        }   else{
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(getResources().getColor(R.color.white));
        }
        //Draw the view on the canvas
        v.draw(canvas);
        return bitmap;
    }

    private Bitmap combineBitmaps(Bitmap b1, Bitmap b2){
        int width = b1.getWidth();
        int height = b1.getHeight() + b2.getHeight();
        Bitmap newBitmap = Bitmap.createBitmap(width, height, b1.getConfig());
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawBitmap(b1, 0, 0, null);
        canvas.drawBitmap(b2, 0, b1.getHeight(), null);
        return newBitmap;
    }

    private void showMaterialListView(){
        //Create a new adapter and set it to the existing recyclerView.
        RecyclerView rv = view.findViewById(R.id.recyclerView);
        rv.setAdapter(materialListAdapter);
    }

    private void showProjectItemView(){
        //Create a new adapter and set it to the existing recyclerView.
        RecyclerView rv = view.findViewById(R.id.recyclerView);
        rv.setAdapter(projectItemAdapter);
    }

    private void showProjectItemCreator() {
        //Show the ProjectItemCreatorFragment.
        ProjectItemCreatorFragment f = ProjectItemCreatorFragment.newInstance(project.getName());
        f.setTargetFragment(ProjectFragment.this, 0);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, f, f.getClass().getSimpleName());
        transaction.addToBackStack(f.getClass().getSimpleName());
        transaction.commit();
    }

    //------------------------------- ProjectItemAdapter -------------------------------//

    public class ProjectItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        //View types
        private final int ITEM_VIEW = 0;
        private final int TOTAL_VIEW = 1;
        private ActionMode mActionMode;


        @Override
        public int getItemCount() {
            return project.getProjectItems() == null ? 0 : project.getProjectItems().size() + 1;
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
                return new ItemVH(LayoutInflater.from(context).inflate(
                        R.layout.project_list_item, parent, false));

            } else if (viewType == TOTAL_VIEW) {
                //Inflate the TOTAL_VIEW.
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
                    ProjectItem pi = project.getProjectItems().get(position);
                    ItemVH itemVH = (ItemVH) holder;
//                    if (position % 2 == 0) {
//                        itemVH.itemView.setBackground(
//                                getResources().getDrawable(R.drawable.light_gray_to_light_blue));
//                    } else {
//                        itemVH.itemView.setBackground(
//                                getResources().getDrawable(R.drawable.white_to_light_blue));
//                    }
                    itemVH.nameTV.setText(pi.getName());
                    double area = pi.getLength() * pi.getWidth();
                    String dimArea = String.format(
                            Locale.US,
                            "%.1fft x %.1fft  (%.0fft2)",
                            pi.getLength() * INCHES_TO_FEET,
                            pi.getWidth() * INCHES_TO_FEET,
                            area * INCHES_TO_FEET);
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
                nameTV = itemView.findViewById(R.id.nameLabelTV);
                dimAreaTV = itemView.findViewById(R.id.dimAreaTV);
                priceTV = itemView.findViewById(R.id.priceTV);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //Inform mListener show ic_project item.
                        mListener.onProjectFragmentProjectItemClick(
                                project.getProjectItems().get(getAdapterPosition()));

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
                project.deleteProjectItem(position);
                projectItemAdapter.notifyItemRemoved(position);
                projectItemAdapter.notifyItemChanged(getItemCount()-1);
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

    //------------------------------- MaterialListAdapter -------------------------------//

    public class MaterialListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public int getItemCount() {
            return project.getMaterialList() == null ? 0 : project.getMaterialList().size();
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ItemVH(LayoutInflater.from(context).inflate(
                    R.layout.list_item_textview_textview, parent, false));

        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            BaseMaterial m = project.getMaterialList().get(position);
            ItemVH itemVH = (ItemVH) holder;
            if (position % 2 == 0) {
                itemVH.itemView.setBackgroundColor(getResources().getColor(R.color.lightGray));
            } else {
                itemVH.itemView.setBackgroundColor(getResources().getColor(R.color.white));
            }
            itemVH.columnLeftTV.setText(m.getName());
            itemVH.columnRightTV.setText(String.format(
                    Locale.US,
                    "%.1f",
                    m.getQuantity()));
        }

        private class ItemVH extends RecyclerView.ViewHolder {
            TextView columnLeftTV;
            TextView columnRightTV;

            ItemVH(final View itemView) {
                super(itemView);
                columnLeftTV = itemView.findViewById(R.id.columnLeftTV);
                columnRightTV = itemView.findViewById(R.id.columnRightTV);
            }
        }
    }
}

//Toast.makeText(context, "resumed", Toast.LENGTH_SHORT).show();
