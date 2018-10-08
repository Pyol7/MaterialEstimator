//package com.jeffreyromero.materialestimator.material;
//
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.widget.Toast;
//
//import com.jeffreyromero.materialestimator.R;
//import com.jeffreyromero.materialestimator.data.ProjectItemsSharedPreferences;
//import com.jeffreyromero.materialestimator.models.MaterialList;
//
//import java.util.ArrayList;
//
//
///**
// * Handles the following:
// * Loads MaterialListFragment when a list item is click.
// */
//public class MaterialActivity extends AppCompatActivity implements
//        MaterialListsFragment.OnItemClickListener,
//        SharedPreferences.OnSharedPreferenceChangeListener {
//
//    private ArrayList<MaterialList> allLists;
//    private ProjectItemsSharedPreferences dataSource;
//
//
//    @Override
//    protected void onCreate(final Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.material_activity);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        //Create User Shared Preferences Helper.
//        dataSource = new ProjectItemsSharedPreferences(
//                getString(R.string.user_material_lists),
//                this
//        );
//
//        //On SharedPreferences change, onSharedPreferenceChanged() would be called.
//        dataSource.registerOnChangeListener(this);
//
//        allLists = dataSource.getAll();
//
//        // Display all lists.
//        if (findViewById(R.id.fragment_container) != null) {
//            // To avoid overlapping fragments.
//            if (savedInstanceState != null) {
//                return;
//            }
//            MaterialListsFragment f = MaterialListsFragment.newInstance(allLists);
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            transaction.add(R.id.fragment_container, f, f.getClass().getSimpleName());
//            transaction.commit();
//        }
//    }
//
//    /**
//     * Called when a shared preference is changed, added, or removed. This
//     * may be called even if a preference is set to its existing value.
//     * <p>
//     * <p>This callback will be run on your main thread.
//     *
//     * @param sharedPreferences The {@link SharedPreferences} that received the change.
//     *
//     * @param key               The key of the preference that was changed, added, or deleted.
//     */
//    @Override
//    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//
//        Toast.makeText(this, "Key:" + key, Toast.LENGTH_LONG).show();
//
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        dataSource.unRegisterOnChangeListener(this);
//    }
//
//    //----------------- MaterialListsFragment Callbacks --------------------//
//
//    @Override
//    public void onItemClick(int position) {
//        // Reload all the stored material lists
//        allLists = dataSource.getAll();
//        // Get the clicked list
//        MaterialList clickedList = allLists.get(position);
//        // Load MaterialListFragment
//        MaterialListFragment f = MaterialListFragment.newInstance(clickedList);
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.fragment_container, f, f.getClass().getSimpleName());
//        transaction.addToBackStack(null);
//        transaction.commit();
//    }
//
//    @Override
//    public void onAddNewListButtonClick() {
//        // Create an empty list.
//        MaterialList materialList = new MaterialList("New List");
//        // Load MaterialListFragment
//        MaterialListFragment f = MaterialListFragment.newInstance(materialList);
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.fragment_container, f, f.getClass().getSimpleName());
//        transaction.addToBackStack(null);
//        transaction.commit();
//    }
//
//}
//
////Toast.makeText(getActivity(), "Clicked @" + position, Toast.LENGTH_SHORT).show();
//
//
