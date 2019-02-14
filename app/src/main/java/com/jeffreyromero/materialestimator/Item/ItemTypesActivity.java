package com.jeffreyromero.materialestimator.Item;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.jeffreyromero.materialestimator.R;
import com.jeffreyromero.materialestimator.data.ItemTypesSharedPreference;
import com.jeffreyromero.materialestimator.models.BaseItem;

import java.util.ArrayList;

/**
 * This activity is responsible for managing all operations relating to item types
 */
public class ItemTypesActivity extends AppCompatActivity implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    private ArrayList<BaseItem> allLists;
    private ItemTypesSharedPreference itemsSP;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_type_activity);
        // Set the toolbar as the action bar.
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Init project item shared preferences
        itemsSP = new ItemTypesSharedPreference(this,
                getString(R.string.item_types_sp_file_name)
        );

        //When a preference is changed the onSharedPreferenceChanged() callback would be called.
        itemsSP.registerOnChangeListener(this);

    }

    /**
     * Called when a shared preference is changed, added, or removed. This
     * may be called even if a preference is set to its existing value.
     * <p>
     * <p>This callback will be run on your main thread.
     *
     * @param sharedPreferences The {@link SharedPreferences} that received the change.
     *
     * @param key               The key of the preference that was changed, added, or deleted.
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        Toast.makeText(this, "Key:" + key, Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        itemsSP.unRegisterOnChangeListener(this);
    }
}
//Toast.makeText(getActivity(), "Clicked @" + position, Toast.LENGTH_SHORT).show();


