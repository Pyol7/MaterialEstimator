package com.jeffreyromero.materialestimator;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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
        ProjectFragment.OnItemClickListener,
        NavigationView.OnNavigationItemSelectedListener {

    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);

        // Set the toolbar as the action bar.
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Setup navigation drawer and tie functionality to toolbar
        drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.nav_open,
                R.string.nav_close
        );
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // Controls up button functionality
        actionBarDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Simulate back button pressed
                onBackPressed();
            }
        });

        // Setup drawer navigation
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        storeDefaultProjectItemsToSharedPreferences(this.getApplicationContext());

        // Display Projects fragment.
        if (findViewById(R.id.fragment_container) != null) {
            // To avoid overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }
            // Note that the first fragment is not added to the back stack
            // therefore clearing the back stack would always reveal it
            Helper.addFragment(this, ProjectsFragment.newInstance(), false);
        }

        // todo - Set default values for ProjectItem specification.
        PreferenceManager.setDefaultValues(this, R.xml.project_item_creator_settings, false);
    }

    @Override
    public void onBackPressed() {
        // Close drawer if its open then move back
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // One of these methods has to be used in the onCreateView method of a fragment
    // to explicitly set up-button or drawer navigation
    public void enableUpNavigation() {
        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void enableDrawerNavigation(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
    }

    private void storeDefaultProjectItemsToSharedPreferences(Context context) {
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
        // Show the clicked project
        Helper.replaceFragment(
                this,
                ProjectFragment.newInstance(project),
                true
        );
    }

    @Override
    public void onProjectFragmentProjectItemClick(ProjectItem projectItem) {
        // Show the clicked project item by loading the ProjectItem Fragment
        Helper.replaceFragment(
                this,
                ProjectItemFragment.newInstance(projectItem),
                true
        );
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_projects:
                // ProjectsFragment was loaded by MainActivity but was not added to back stack
                Helper.clearBackStack(getSupportFragmentManager());
                break;
            case R.id.nav_material_lists:
                //
                break;
        }
        // Close the navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}

//Toast.makeText(this, "Clicked @" + position, Toast.LENGTH_SHORT).show();