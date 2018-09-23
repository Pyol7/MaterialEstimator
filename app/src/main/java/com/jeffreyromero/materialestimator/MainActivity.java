package com.jeffreyromero.materialestimator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.jeffreyromero.materialestimator.data.DefaultDroppedCeilingMaterialList;
import com.jeffreyromero.materialestimator.data.DefaultDrywallCeilingMaterialList;
import com.jeffreyromero.materialestimator.data.DefaultDrywallPartitionMaterialList;
import com.jeffreyromero.materialestimator.data.DefaultMaterialList;
import com.jeffreyromero.materialestimator.data.MaterialListsDataSource;
import com.jeffreyromero.materialestimator.material.MaterialActivity;
import com.jeffreyromero.materialestimator.models.Project;
import com.jeffreyromero.materialestimator.models.ProjectItem;
import com.jeffreyromero.materialestimator.project.ProjectFragment;
import com.jeffreyromero.materialestimator.project.ProjectItemFragment;
import com.jeffreyromero.materialestimator.project.ProjectsFragment;
import com.jeffreyromero.materialestimator.utilities.Helper;

public class MainActivity extends AppCompatActivity implements
        ProjectsFragment.OnItemClickListener,
        ProjectFragment.OnItemClickListener {

    private DrawerLayout mDrawerLayout;
    private String currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        //Set the toolbar as the action bar.
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);

        //Get the drawer layout
        mDrawerLayout = findViewById(R.id.drawer_layout);
        //Get the navigationView
        NavigationView navigationView = findViewById(R.id.nav_view);
        //Setup navigationView.
        setupDrawerContent(navigationView);

        //On first run initialize data source.
        initDataSource(this.getApplicationContext());

        //Display Projects fragment.
        if (findViewById(R.id.fragment_container) != null) {
            // To avoid overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }
            ProjectsFragment f = ProjectsFragment.newInstance();
            currentFragment = Helper.addFragment(this, f, false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.nav_projects:
                                ProjectsFragment f = ProjectsFragment.newInstance();
                                //To avoid overlapping.
                                if (f.getClass().getSimpleName().equals(currentFragment)) {
                                    break;
                                }
                                currentFragment = Helper.replaceFragment(
                                        MainActivity.this, f, true
                                );
                                break;
                            case R.id.nav_material_lists:
                                Intent intent = new Intent(
                                        MainActivity.this,
                                        MaterialActivity.class
                                );
                                startActivity(intent);
                                break;
                        }
                        // Close the navigation drawer
                        mDrawerLayout.closeDrawers();
                        return true;
                    }

                }
        );
    }

    /**
     * On first run, create SharedPreferences files and populate with default and sample lists:
     * <p>
     * "App SharedPreferences" - Contains three(3) lists:
     * "Default Material List" - Contains all the materials used to create other lists.
     * "Sample Ceiling Material List" - Provides a starting point for user lists and can be
     * modified and restored.
     * "Sample Partition Material List" - Same as above.
     * <p>
     * "User SharedPreferences" - Contains the two sample lists and all user created lists.
     */
    private void initDataSource(Context context) {
        //Create and Populate Default material lists Shared Preferences file.
        MaterialListsDataSource dml = new MaterialListsDataSource(
                getString(R.string.default_material_lists),
                context
        );
        if (dml.isEmpty()) {
            dml.put(DefaultMaterialList.getList(context));
            dml.put(DefaultDrywallCeilingMaterialList.getList(context));
            dml.put(DefaultDrywallPartitionMaterialList.getList(context));
            dml.put(DefaultDroppedCeilingMaterialList.getList(context));
        }
        //Create and populate User Material Lists with default lists.
        MaterialListsDataSource uml = new MaterialListsDataSource(
                getString(R.string.user_material_lists),
                context
        );
        if (uml.isEmpty()) {
            uml.put(dml.get(context.getString(R.string.default_drywall_ceiling_material_list)));
            uml.put(dml.get(context.getString(R.string.default_drywall_partition_material_list)));
            uml.put(dml.get(context.getString(R.string.default_dropped_ceiling_material_list)));
        }

    }

    @Override
    public void onProjectsFragmentItemClick(Project project) {
        //Show the clicked project by loading the Project Fragment.
        ProjectFragment f = ProjectFragment.newInstance(project);
        currentFragment = Helper.replaceFragment(this, f, true);
    }

    @Override
    public void onProjectFragmentProjectItemClick(ProjectItem projectItem) {
        //Show the clicked project item by loading the Project item Fragment.
        ProjectItemFragment f = ProjectItemFragment.newInstance(projectItem);
        currentFragment = Helper.replaceFragment(this, f, true);
    }

}
