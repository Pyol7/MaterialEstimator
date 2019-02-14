package com.jeffreyromero.materialestimator.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.jeffreyromero.materialestimator.models.BaseItem;
import com.jeffreyromero.materialestimator.models.Project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ItemTypesSharedPreference implements DataSource<String, BaseItem> {

    private SharedPreferences spInstance;

    public ItemTypesSharedPreference(Context context, String sharedPreferencesFileName) {
        this.spInstance = context.getSharedPreferences(
                sharedPreferencesFileName,
                0
        );
    }

    @Override
    public boolean isEmpty() {
        Map<String, ?> map = spInstance.getAll();
        return map.size() == 0;
    }

    @Override
    public boolean contains(String key){
        return spInstance.contains(key);
    }

    /**
     * Stores an Object using it's <em>subType</em> property as key.
     * If the key exists the Object would be overwritten by the Object provided.
     *  @param key         The key to use to store the Object.
     * @param baseItem The Object to store.
     */
    @Override
    public ArrayList<BaseItem> put(String key, BaseItem baseItem) {
        SharedPreferences.Editor editor = spInstance.edit();
        String json = new Gson().toJson(baseItem);
        editor.putString(key, json);
        editor.commit();
        return getAll();
    }

    @Override
    public BaseItem get(String key) {
        String json = spInstance.getString(key, null);
        return Deserializer.toItemType(json);
    }

    @Override
    public BaseItem get(int position) {
        ArrayList<BaseItem> allBaseItems = getAll();
        return allBaseItems.get(position);
    }

    @Override
    public ArrayList<BaseItem> getAll() {
        List<?> unSortedStrings = new ArrayList<>(spInstance.getAll().values());
        // Convert values to Project objects
        ArrayList<BaseItem> itemTypes = new ArrayList<>();
        for (Object i: unSortedStrings){
            BaseItem itemType = Deserializer.toItemType(i.toString());
            itemTypes.add(itemType);
        }
        return itemTypes;
    }

    @Override
    public Map<String, ?> getAllAsMap() {
        return spInstance.getAll();
    }

    @Override
    public ArrayList<String> getAllKeys() {
        return new ArrayList<>(getAllAsMap().keySet());
    }

    @Override
    public void replace(String key, BaseItem baseItem) {
        put(key, baseItem);
    }

    @Override
    public ArrayList<BaseItem> remove(String key) {
        SharedPreferences.Editor editor = spInstance.edit();
        editor.remove(key).commit();
        return getAll();
    }

    @Override
    public void clearAll(){
        SharedPreferences.Editor editor = spInstance.edit();
        editor.clear();
        editor.commit();
    }

    @Override
    public void registerOnChangeListener(SharedPreferences.OnSharedPreferenceChangeListener mListener) {
        spInstance.registerOnSharedPreferenceChangeListener(mListener);
    }

    @Override
    public void unRegisterOnChangeListener(
            SharedPreferences.OnSharedPreferenceChangeListener mListener){
        spInstance.unregisterOnSharedPreferenceChangeListener(mListener);
    }

}
