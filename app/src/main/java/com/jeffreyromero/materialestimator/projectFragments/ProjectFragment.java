package com.jeffreyromero.materialestimator.projectFragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.Toolbar;
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

import com.google.gson.Gson;
import com.jeffreyromero.materialestimator.MainActivity;
import com.jeffreyromero.materialestimator.R;
import com.jeffreyromero.materialestimator.data.Deserializer;
import com.jeffreyromero.materialestimator.data.ProjectsSharedPreference;
import com.jeffreyromero.materialestimator.models.BaseItem;
import com.jeffreyromero.materialestimator.models.ItemTypes.DroppedCeiling;
import com.jeffreyromero.materialestimator.models.ItemTypes.DrywallCeiling;
import com.jeffreyromero.materialestimator.models.ItemTypes.DrywallPartition;
import com.jeffreyromero.materialestimator.models.Project;
import com.jeffreyromero.materialestimator.utilities.CustomRecyclerView;
import com.jeffreyromero.materialestimator.utilities.Helper;
import com.jeffreyromero.materialestimator.utilities.dialogCreateNewItem.DialogCreateItem;
import com.jeffreyromero.materialestimator.utilities.dialogCreateNewItem.DialogDroppedCeilingDetails;
import com.jeffreyromero.materialestimator.utilities.dialogCreateNewItem.DialogDrywallCeilingDetails;
import com.jeffreyromero.materialestimator.utilities.dialogCreateNewItem.DialogDrywallPartitionDetails;
import com.jeffreyromero.materialestimator.utilities.dialogCreateNewItem.DialogSelectItemType;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

/**
 * Receives a Project from MainActivity and displays it.
 */
public class ProjectFragment extends Fragment implements
    DialogSelectItemType.OnFragmentInteractionListener,
    DialogDroppedCeilingDetails.OnFragmentInteractionListener,
    DialogDrywallCeilingDetails.OnFragmentInteractionListener,
    DialogDrywallPartitionDetails.OnFragmentInteractionListener,
    DialogCreateItem.OnFragmentInteractionListener{

    private static final String PROJECT = "project";
    private ShareActionProvider mShareActionProvider;
//    private MaterialListAdapter materialListAdapter;
    private OnItemClickListener mListener;
    private ProjectsSharedPreference projectsSP;
    private ArrayList<BaseItem> itemTypes;
    private MainActivity mainActivity;
    private Project project;
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
        void onProjectFragmentItemClick(BaseItem item);
        void onProjectFragmentCreateNewItemBtnClick();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Ensure that there is a listener and it implements the callback(s).
        // In this case MainActivity is the listener so we can cast from context.
        if (context instanceof ProjectFragment.OnItemClickListener) {
            mListener = (OnItemClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement Project.OnItemClickListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Use MainActivity as context
        mainActivity = (MainActivity)getActivity();

        if (savedInstanceState == null){
            // Load and add as nested fragment.
            Helper.addFragment(mainActivity,
                    ProjectFragmentViews.newInstance(project),
                    R.id.project_fragment_container,
                    false
            );

            // Use the passed in project
            if (getArguments() != null) {
                String json = getArguments().getString(PROJECT);
                project = Deserializer.toProject(json);
            }
        } else {
            // Get the current project from the savedInstanceState bundle
            project = Deserializer.toProject(savedInstanceState.getString(PROJECT));
        }

        // Init list adapters
//        materialListAdapter = new MaterialListAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.project_fragment, container, false);
        // Get this fragment's toolbar and set it as the action bar in MainActivity.
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        mainActivity.setSupportActionBar(toolbar);
        // Hide default title which shows the app name.
        mainActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Show custom title
        toolbar.setTitle(project.getName());
        // Make this fragment's options visible on the main menu
        setHasOptionsMenu(true);
        // Enable up navigation for this fragment
        mainActivity.enableDrawerNavigation(false);
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(PROJECT, new Gson().toJson(project));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
            case android.R.id.home:
                // Provide functionality for the home icon.
                // Used when enableDrawerNavigation = false
                mainActivity.onBackPressed();
                return true;
            case R.id.action_add:
                mListener.onProjectFragmentCreateNewItemBtnClick();
                return true;
            case R.id.action_share:
//                setShareIntent(createIntent());
                return true;
            case R.id.action_project_item_view:
//                showProjectItemView();
                return true;
            case R.id.action_material_list_view:
//                showMaterialListView();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //------------------------------- Callbacks -------------------------------//

    /**
     * Determines which item type the user selected from DialogSelectItemType.
     * And loads the corresponding dialog to prompt the user for more information.
     * @param selectedItemType The item type the user selected.
     */
    @Override
    public void onDialogSelectItemTypeNextBtnClick(BaseItem selectedItemType) {
        // Load corresponding details menu
        if (selectedItemType.getSubType().equals("Dropped Ceiling")){
            Helper.replaceFragment(mainActivity,
                    DialogDroppedCeilingDetails.newInstance(selectedItemType),
                    ProjectFragment.this,
                    R.id.project_fragment_container,
                    true
            );
        }
        if (selectedItemType.getSubType().equals("Drywall Ceiling")){
            Helper.replaceFragment(mainActivity,
                    DialogDrywallCeilingDetails.newInstance(selectedItemType),
                    ProjectFragment.this,
                    R.id.project_fragment_container,
                    true
            );
        }
        if (selectedItemType.getSubType().equals("Drywall Partition")){
            Helper.replaceFragment(mainActivity,
                    DialogDrywallPartitionDetails.newInstance(selectedItemType),
                    ProjectFragment.this,
                    R.id.project_fragment_container,
                    true
            );
        }
    }


    /**
     * All Dialog******Details fragments provide this same method for loading the DialogCreateItem.
     * @param itemType The selected itemType with necessary details added to it in preparation
     *                 for final item creation.
     */
    @Override
    public void loadDialogCreateItem(BaseItem itemType) {
        // Show the DialogCreateItem
        Helper.replaceFragment(mainActivity,
                DialogCreateItem.newInstance(itemType),
                ProjectFragment.this,
                R.id.project_fragment_container,
                true
        );
    }

    /**
     * Receives the newly created item from DialogCreateItem and adds it to the project.
     * @param newItem The newly created item.
     */
    @Override
    public void onDialogCreateItemSaveBtnClick(BaseItem newItem) {
        // Add new item to the project.
        project.addItem(newItem);

        // Get ProjectFragmentViews and refresh its list view.
        FragmentManager fm = getActivity().getSupportFragmentManager();
        ProjectFragmentViews f =
                (ProjectFragmentViews) fm.findFragmentByTag(
                        "com.jeffreyromero.materialestimator.project.ProjectFragmentViews"
                );
        f.refreshItemsAdapter();

        // Update the project in shared preferences.
        projectsSP.replace(project.getName(), project);
    }

    //------------------------------- Menu Methods -------------------------------//

//    private void setShareIntent(Intent intent) {
//        if (mShareActionProvider != null) {
//            mShareActionProvider.setShareIntent(intent);
//        }
//        else {
//            Log.d(getClass().getCanonicalName(), "Share action provider is null");
//        }
//    }

//    private Intent createIntent(){
//        //Create bitmap
//        Bitmap mBitmap = createFinalBitmap();
////        Save bitmap to cache directory
//        try {
//            //Create subdirectory in cache which corresponds to path.filepaths.xml
//            File cachePath = new File(getActivity().getCacheDir(), "tempSharedImageDir");
//            cachePath.mkdirs();
//
//            //Overwrites this image every time
//            FileOutputStream stream = new FileOutputStream(cachePath + "/material_estimator_list.jpg");
//
//            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//            stream.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        //Read image from cache
//        //Get directory: cache/tempSharedImageDir
//        File imagePath = new File(getActivity().getCacheDir(), "tempSharedImageDir");
//        //Get file: cache/tempSharedImageDir/material_estimator_list.jpg
//        File newFile = new File(imagePath, "material_estimator_list.jpg");
//        //Create uri:
//        // content://com.jeffreyromero.materialestimator.fileprovider/temp_shared_image/tempShareImage.png
//        Uri contentUri = FileProvider.getUriForFile(
//                getActivity(),
//                "com.jeffreyromero.materialestimator.fileprovider",
//                newFile
//        );
//        //Send image via intent
//        if (contentUri != null) {
//            Intent shareIntent = new Intent();
//            shareIntent.setAction(Intent.ACTION_SEND);
//            //Temp permission for receiving app to read this file
//            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            shareIntent.setDataAndType(contentUri, mainActivity.getContentResolver().getType(contentUri));
//            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
//            shareIntent.setType("image/jpg");
//            return shareIntent;
//        }
//        return null;
//    }

//    private Bitmap createFinalBitmap(){
        //Create individual bitmaps
//        Bitmap header = createBitmapFromView(view.findViewById(R.id.headerCL));
//        Bitmap recyclerView = createBitmapFromView(view.findViewById(R.id.recyclerView));
        //Combine bitmaps
//        return combineBitmaps(header, recyclerView);
//    }

//    private Bitmap createBitmapFromView(View v) {
//        if (v instanceof RecyclerView){
//            //Scroll to top for correct measurements
//            ((RecyclerView) v).getLayoutManager().scrollToPosition(0);
//        }
//        v.measure(
//                View.MeasureSpec.makeMeasureSpec(v.getWidth(), View.MeasureSpec.EXACTLY),
//                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//        Bitmap bitmap = Bitmap.createBitmap(
//                v.getWidth(),
//                v.getMeasuredHeight(),
//                Bitmap.Config.ARGB_8888);
//        //Bind a canvas to it
//        Canvas canvas = new Canvas(bitmap);
//        //Get the view's background
//        Drawable bgDrawable = v.getBackground();
//        if (bgDrawable != null) {
//            //has background drawable, then draw it on the canvas
//            bgDrawable.draw(canvas);
//        }   else{
//            //does not have background drawable, then draw white background on the canvas
//            canvas.drawColor(getResources().getColor(R.color.colorWhite));
//        }
//        //Draw the view on the canvas
//        v.draw(canvas);
//        return bitmap;
//    }
//
//    private Bitmap combineBitmaps(Bitmap b1, Bitmap b2){
//        int width = b1.getWidth();
//        int height = b1.getHeight() + b2.getHeight();
//        Bitmap newBitmap = Bitmap.createBitmap(width, height, b1.getConfig());
//        Canvas canvas = new Canvas(newBitmap);
//        canvas.drawBitmap(b1, 0, 0, null);
//        canvas.drawBitmap(b2, 0, b1.getHeight(), null);
//        return newBitmap;
//    }

//    private void showProjectItemView(){
//        CustomRecyclerView rv = view.findViewById(R.id.item_list_view_rv);
//        rv.setAdapter(ItemsAdapter);
//    }

//    private void showMaterialListView(){
//        CustomRecyclerView rv = view.findViewById(R.id.item_list_view_rv);
//        rv.setAdapter(materialListAdapter);
//    }

    //------------------------------- MaterialListAdapter -------------------------------//

//    public class MaterialListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//        // Get Complete material list
//        Map<String, Double> map = project.getCompleteMaterialList();
//
//        @Override
//        public int getItemCount() {
//            return map == null ? 0 : map.size();
//        }
//
//        @NonNull
//        @Override
//        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            return new ItemVH(LayoutInflater.from(mainActivity).inflate(
//                    R.layout.list_item_textview_textview, parent, false));
//
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//            // Get values from map
//            String name = (String) ( map.keySet().toArray())[ position ];
//            double quantity = (double) (map.values().toArray())[ position ];
//
//            ItemVH itemVH = (ItemVH) holder;
//            if (position % 2 == 0) {
//                itemVH.itemView.setBackgroundColor(getResources().getColor(R.color.colorLightGray));
//            } else {
//                itemVH.itemView.setBackgroundColor(getResources().getColor(R.color.colorWhite));
//            }
//            itemVH.columnLeftTV.setText(name);
//            itemVH.columnRightTV.setText(String.format(
//                    Locale.US,
//                    "%.1f",
//                    quantity));
//        }
//
//        private class ItemVH extends RecyclerView.ViewHolder {
//            TextView columnLeftTV;
//            TextView columnRightTV;
//
//            ItemVH(final View itemView) {
//                super(itemView);
//                columnLeftTV = itemView.findViewById(R.id.columnLeftTV);
//                columnRightTV = itemView.findViewById(R.id.columnRightTV);
//            }
//        }
//    }
}

//Toast.makeText(context, "resumed", Toast.LENGTH_SHORT).show();
