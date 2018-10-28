package com.jeffreyromero.materialestimator.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.jeffreyromero.materialestimator.models.BaseItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ItemsDataSource implements DataSource<String, BaseItem> {

    private SharedPreferences spInstance;

    public ItemsDataSource(String sharedPreferencesFileName, Context context) {
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
     *
     * @param key         The key to use to store the Object.
     * @param baseItem The Object to store.
     */
    @Override
    public void put(String key, BaseItem baseItem) {
        SharedPreferences.Editor editor = spInstance.edit();
        String json = new Gson().toJson(baseItem);
        editor.putString(key, json);
        editor.commit();
    }

    @Override
    public BaseItem get(String key) {
        String json = spInstance.getString(key, null);
        return Deserializer.toItem(json);
    }

    @Override
    public BaseItem get(int position) {
        ArrayList<BaseItem> allBaseItems = getAll();
        return allBaseItems.get(position);
    }

    @Override
    public ArrayList<BaseItem> getAll() {
        return new ArrayList<>(getAllAsMap().values());
    }

    @Override
    public Map<String, BaseItem> getAllAsMap() {
        Map<String, BaseItem> newMap = new HashMap<>();
        Map<String, ?> map = spInstance.getAll();
        Set<String> keys = map.keySet();
        for (String key: keys){
            newMap.put(key, get(key));
        }
        return newMap;
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
    public void remove(String key) {
        SharedPreferences.Editor editor = spInstance.edit();
        editor.remove(key).commit();
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
