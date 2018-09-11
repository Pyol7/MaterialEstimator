package com.jeffreyromero.materialestimator.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.jeffreyromero.materialestimator.models.MaterialList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Provides a single access point to stored SharedPreferences data.
 *
 * Usage:
 * MaterialListsDataSource mds = new MaterialListsDataSource(
 *          R.string.material_list_shared_preferences,
 *          context
 * );
 * MaterialList ml = mds.get("R.string.default_drywall_ceiling_material_list");
 *
 */
public class MaterialListsDataSource implements DataSource<MaterialList> {

    private SharedPreferences spInstance;

    public MaterialListsDataSource(String sharedPreferencesFileName, Context context) {
        this.spInstance = context.getSharedPreferences(
                sharedPreferencesFileName,
                0
        );
    }

    /**
     * Store a MaterialList using it's name property as key.
     * If the list exists it would overwrite it.
     */
    @Override
    public void put(MaterialList materialList) {
        SharedPreferences.Editor editor = spInstance.edit();
        String json = new Gson().toJson(materialList);
        editor.putString(materialList.getName(), json);
        editor.commit();
    }

    /**
     * Get the MaterialList by key.
     */
    @Override
    public MaterialList get(String key) {
        String json = spInstance.getString(key, null);
        return Deserializer.toMaterialList(json);
    }

    /**
     * Get the MaterialList by position.
     */
    @Override
    public MaterialList get(int position) {
        ArrayList<MaterialList> allMaterialLists = getAll();
        return allMaterialLists.get(position);
    }

    /**
     * Get all the stored MaterialList as a Map.
     */
    @Override
    public Map<String, MaterialList> getAllAsMap() {
        Map<String, MaterialList> newMap = new HashMap<>();
        Map<String, ?> map = spInstance.getAll();
        Set<String> keys = map.keySet();
        for (String key: keys){
            newMap.put(key, get(key));
        }
        return newMap;
    }

    /**
     * Get all the stored MaterialList as an ArrayList.
     */
    @Override
    public ArrayList<MaterialList> getAll() {
        return new ArrayList<>(getAllAsMap().values());
    }

    /**
     * Get the keys of all the stored MaterialList.
     * This is also all the names of MaterialLists.
     */
    @Override
    public ArrayList<String> getAllKeys() {
        return new ArrayList<>(getAllAsMap().keySet());
    }


    /**
     * Remove a MaterialList by key.
     */
    @Override
    public void remove(String key) {
        SharedPreferences.Editor editor = spInstance.edit();
        editor.remove(key).commit();
    }

    /**
     * Rename a stored MaterialList.
     */
    @Override
    public void renameKey(String oldName, String newName) {
        MaterialList materialList = get(oldName);
        spInstance.edit().remove(oldName).commit();
        materialList.setName(newName);
        put(materialList);
    }

    @Override
    public boolean contains(String key){
        return spInstance.contains(key);
    }


    /**
     * Check if Shared preferences is empty.
     */
    @Override
    public boolean isEmpty() {
        Map<String, ?> map = spInstance.getAll();
        return map.size() == 0;
    }

    @Override
    public void clearAll(){
        SharedPreferences.Editor editor = spInstance.edit();
        editor.clear();
        editor.commit();
    }

}
