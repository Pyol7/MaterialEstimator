package com.jeffreyromero.materialestimator;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.view.MenuItem;

import com.jeffreyromero.materialestimator.Item.EditItemTypeFragment;
import com.jeffreyromero.materialestimator.Item.ItemTypesFragment;
import com.jeffreyromero.materialestimator.data.ItemTypesSharedPreference;
import com.jeffreyromero.materialestimator.data.ProjectsSharedPreference;
import com.jeffreyromero.materialestimator.models.BaseItem;
import com.jeffreyromero.materialestimator.models.ItemTypes.DroppedCeiling;
import com.jeffreyromero.materialestimator.models.ItemTypes.DrywallCeiling;
import com.jeffreyromero.materialestimator.models.ItemTypes.DrywallPartition;
import com.jeffreyromero.materialestimator.models.Project;
import com.jeffreyromero.materialestimator.projectFragments.ItemFragment;
import com.jeffreyromero.materialestimator.projectFragments.ProjectFragment;
import com.jeffreyromero.materialestimator.projectFragments.ProjectsFragment;
import com.jeffreyromero.materialestimator.utilities.Helper;
import com.jeffreyromero.materialestimator.utilities.dialogCreateNewItem.DialogSelectItemType;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        ProjectsFragment.OnItemClickListener,
        ProjectFragment.OnItemClickListener,
        ItemTypesFragment.OnItemClickListener {

    private DrawerLayout drawerLayout;

    private ProjectsSharedPreference projectsSharedPreference;
    private ItemTypesSharedPreference itemTypesSharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // Setup data sources
        projectsSharedPreference = new ProjectsSharedPreference(this, getResources().getString(R.string.projects_sp_file_name));
        itemTypesSharedPreference = new ItemTypesSharedPreference(this, getResources().getString(R.string.item_types_sp_file_name));

        storeItemTypesToSharedPreferences();

        // todo - use for general settings
        PreferenceManager.setDefaultValues(this, R.xml.project_item_creator_settings, false);

        // Load ProjectsFragment fragment.
        if (findViewById(R.id.fragment_container) != null) {
            // To avoid overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }
            // Note that the first fragment is not added to the back stack
            // therefore clearing the back stack would always reveal it
            Helper.addFragment(this,
                    ProjectsFragment.newInstance(projectsSharedPreference.getAll()),
                    R.id.fragment_container,
                    false
            );
        }

        // Setup drawer navigation
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
                        R.id.fragment_container,
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
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        } else {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
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

    /**
     * Creates the list of item types that would be used to create new items.
     * The list is refreshed every time MainActivity is called.
     */
    private void storeItemTypesToSharedPreferences() {
        itemTypesSharedPreference.clearAll();
        itemTypesSharedPreference.put("dropped_ceiling", new DroppedCeiling("Untitled"));
        itemTypesSharedPreference.put("drywall_ceiling", new DrywallCeiling("Untitled"));
        itemTypesSharedPreference.put("drywall_partition", new DrywallPartition("Untitled"));
    }

    // ------------------------------ ProjectsFragment ----------------------------------- //

    @Override
    public void onProjectsFragmentLoadProjectRequest(Project project) {
        Helper.replaceFragment(this,
                ProjectFragment.newInstance(project),
                R.id.fragment_container,
                true
        );
    }

    @Override
    public ArrayList<Project> onProjectsFragmentAddProjectRequest(Project project) {
        return projectsSharedPreference.put(project.getName(), project);
    }

    @Override
    public ArrayList<Project> onProjectsFragmentDeleteProjectRequest(String key) {
        return projectsSharedPreference.remove(key);
    }

    // ------------------------------ ProjectFragment ----------------------------------- //

    @Override
    public void onProjectFragmentItemClick(BaseItem item) {
        Helper.replaceFragment(this,
                ItemFragment.newInstance(item),
                R.id.fragment_container,
                true
        );
    }

    @Override
    public void onProjectFragmentCreateNewItemBtnClick() {
        // Get target fragment
        Fragment targetFragment = getSupportFragmentManager().findFragmentByTag(
                "com.jeffreyromero.materialestimator.projectFragments.ProjectFragment"
        );
        Helper.replaceFragment(this,
                DialogSelectItemType.newInstance(itemTypesSharedPreference.getAll()),
                targetFragment,
                R.id.project_fragment_container,
                true
        );
    }

    // ------------------------------ ItemTypesFragment ----------------------------------- //

    @Override
    public void onItemTypesFragmentItemClick(BaseItem itemType) {
        Helper.replaceFragment(this,
                EditItemTypeFragment.newInstance(itemType),
                R.id.fragment_container,
                true
        );
    }

}

//Toast.makeText(this, "Clicked @" + position, Toast.LENGTH_SHORT).show();