package com.jeffreyromero.materialestimator.material;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.jeffreyromero.materialestimator.R;
import com.jeffreyromero.materialestimator.data.MaterialListsDataSource;
import com.jeffreyromero.materialestimator.models.MaterialList;

import java.util.ArrayList;


/**
 * Handles the following:
 * Loads MaterialListFragment when a list item is click.
 */
public class MaterialActivity extends AppCompatActivity implements
        MaterialListsFragment.OnItemClickListener {

    private ArrayList<MaterialList> allLists;
    private MaterialListsDataSource dataSource;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.material_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Create User Shared Preferences helper.
        dataSource = new MaterialListsDataSource(
                getString(R.string.user_material_lists),
                this
        );
        allLists = dataSource.getAll();

        // Display all lists.
        if (findViewById(R.id.fragment_container) != null) {
            // To avoid overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }
            MaterialListsFragment f = MaterialListsFragment.newInstance(allLists);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_container, f, f.getClass().getSimpleName());
            transaction.commit();
        }
    }

    //----------------- MaterialListsFragment Callbacks --------------------//

    @Override
    public void onItemClick(int position) {
        // Reload all the stored material lists
        allLists = dataSource.getAll();
        // Get the clicked list
        MaterialList clickedList = allLists.get(position);
        // Load MaterialListFragment
        MaterialListFragment f = MaterialListFragment.newInstance(clickedList);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, f, f.getClass().getSimpleName());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onAddNewListButtonClick() {
        // Create an empty list.
        MaterialList materialList = new MaterialList("New List");
        // Load MaterialListFragment
        MaterialListFragment f = MaterialListFragment.newInstance(materialList);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, f, f.getClass().getSimpleName());
        transaction.addToBackStack(null);
        transaction.commit();
    }

}

//Toast.makeText(getActivity(), "Clicked @" + position, Toast.LENGTH_SHORT).show();


