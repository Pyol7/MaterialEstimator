package com.jeffreyromero.materialestimator;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.jeffreyromero.materialestimator.Item.ItemTypeFragment;
import com.jeffreyromero.materialestimator.Item.ItemTypesFragment;
import com.jeffreyromero.materialestimator.data.ItemsDataSource;
import com.jeffreyromero.materialestimator.models.BaseItem;
import com.jeffreyromero.materialestimator.models.Project;
import com.jeffreyromero.materialestimator.models.Items.DroppedCeiling;
import com.jeffreyromero.materialestimator.models.Items.DrywallCeiling;
import com.jeffreyromero.materialestimator.models.Items.DrywallPartition;
import com.jeffreyromero.materialestimator.project.ProjectFragment;
import com.jeffreyromero.materialestimator.project.ItemFragment;
import com.jeffreyromero.materialestimator.project.ProjectsFragment;
import com.jeffreyromero.materialestimator.utilities.Helper;

import static android.view.Gravity.CENTER_VERTICAL;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        ProjectsFragment.OnItemClickListener,
        ProjectFragment.OnItemClickListener,
        ItemTypesFragment.OnItemClickListener{

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // Get the drawer layout
        drawerLayout = findViewById(R.id.drawer_layout);

        // Setup drawer navigation
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Store default project items
        storeDefaultProjectItemsToSharedPreferences(this.getApplicationContext());

        // todo - use for general settings
        PreferenceManager.setDefaultValues(this, R.xml.project_item_creator_settings, false);

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
    }

    /**
     * Provides functionality for the navigation menu
     * @param item Declared in Res.menu.nav.menu
     * @return true if item was found, false otherwise
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_projects:
                // ProjectsFragment was not added to back stack
                Helper.clearBackStack(getSupportFragmentManager());
                break;
            case R.id.nav_item_types:
                // Load all item types
                Helper.replaceFragment(
                        this,
                        ItemTypesFragment.newInstance(),
                        true
                );
                break;
        }
        // Close the navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Switches home icon. Functionality is handled by onOptionsItemSelected() in the
     * calling fragment
     * @param enabled true if enabled, false otherwise
     */
    public void enableDrawerNavigation(Boolean enabled){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (enabled){
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        } else {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        }
    }

    /**
     * Opens and closes the navigation drawer
     */
    public void toggleDrawer(){
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    /**
     * Closes the navigation drawer if it is open then moves back
     */
    @Override
    public void onBackPressed() {
        // Close drawer if its open then move back
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void storeDefaultProjectItemsToSharedPreferences(Context context) {
        ItemsDataSource itemsSP = new ItemsDataSource(getString(R.string.items_key), context);

        if (itemsSP.isEmpty()) {
            itemsSP.put("Dropped Ceiling", new DroppedCeiling(getString(R.string.untitled)));
            itemsSP.put("Drywall Ceiling", new DrywallCeiling(getString(R.string.untitled)));
            itemsSP.put("Drywall Partition", new DrywallPartition(getString(R.string.untitled)));
        }
    }

    @Override
    public void onProjectsFragmentItemClick(Project project) {
        // Show the clicked project
        Helper.replaceFragment(this, ProjectFragment.newInstance(project),true);
    }

    @Override
    public void onProjectFragmentItemClick(BaseItem item) {
        // Show the clicked item
        Helper.replaceFragment(this, ItemFragment.newInstance(item),true);
    }

    @Override
    public void onItemTypesFragmentItemClick(BaseItem itemType) {
        // Show the clicked item type
        Helper.replaceFragment(this, ItemTypeFragment.newInstance(itemType),true);
    }

}

//Toast.makeText(this, "Clicked @" + position, Toast.LENGTH_SHORT).show();