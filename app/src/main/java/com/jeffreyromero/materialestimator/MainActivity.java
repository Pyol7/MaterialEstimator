package com.jeffreyromero.materialestimator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.jeffreyromero.materialestimator.data.ProjectItemsSharedPreferences;
import com.jeffreyromero.materialestimator.models.Project;
import com.jeffreyromero.materialestimator.models.ProjectItem;
import com.jeffreyromero.materialestimator.models.defaultProjectItems.DroppedCeiling;
import com.jeffreyromero.materialestimator.models.defaultProjectItems.DrywallCeiling;
import com.jeffreyromero.materialestimator.models.defaultProjectItems.DrywallPartition;
import com.jeffreyromero.materialestimator.project.ProjectFragment;
import com.jeffreyromero.materialestimator.project.ProjectItemFragment;
import com.jeffreyromero.materialestimator.project.ProjectsFragment;
import com.jeffreyromero.materialestimator.utilities.Helper;

public class MainActivity extends AppCompatActivity implements
        ProjectsFragment.OnItemClickListener,
        ProjectFragment.OnItemClickListener {

    private static final double FEET_TO_INCHES = 12;
    private DrawerLayout mDrawerLayout;
    private String currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

//        materialTester(16, 14);

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
        initProjectItemsSharedPreferences(this.getApplicationContext());

        //Display Projects fragment.
        if (findViewById(R.id.fragment_container) != null) {
            // To avoid overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }
            ProjectsFragment f = ProjectsFragment.newInstance();
            currentFragment = Helper.addFragment(this, f, false);
        }

        // Set default values for ProjectItem specification.
        // These values are used by Materials to calculate quantities.
        PreferenceManager.setDefaultValues(this, R.xml.project_item_creator_settings, false);
    }

//    private void materialTester(double dim1, double dim2) {
//        double length = dim1 * FEET_TO_INCHES;
//        double width = dim2 * FEET_TO_INCHES;
//        FurringChannel f = new FurringChannel("Furring Channel", 20.00, 144,16);
//        f.calcQuantity(length, width);
//    }

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
//                                Intent intent = new Intent(
//                                        MainActivity.this,
//                                        MaterialActivity.class
//                                );
//                                startActivity(intent);
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
     * On first run, create SharedPreferences files and populate with default project items
     */
    private void initProjectItemsSharedPreferences(Context context) {
        ProjectItemsSharedPreferences sp = new ProjectItemsSharedPreferences(
                getString(R.string.default_project_items_key),
                context
        );
        if (sp.isEmpty()) {
            sp.put(new DroppedCeiling(getString(R.string.untitled)));
            sp.put(new DrywallCeiling(getString(R.string.untitled)));
            sp.put(new DrywallPartition(getString(R.string.untitled)));
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

//Toast.makeText(getActivity(), "Clicked @" + position, Toast.LENGTH_SHORT).show();