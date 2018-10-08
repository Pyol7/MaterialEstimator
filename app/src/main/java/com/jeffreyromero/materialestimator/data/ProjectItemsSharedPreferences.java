package com.jeffreyromero.materialestimator.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.jeffreyromero.materialestimator.models.ProjectItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Provides a single access point to stored SharedPreferences data.
 * Usage:
 * ProjectItemsSharedPreferences sp = new ProjectItemsSharedPreferences(
 *          R.string.default_project_items,
 *          context
 * );
 * ProjectItem pi = sp.get("R.string.drywall_ceiling");
 */
public class ProjectItemsSharedPreferences implements DataSource<ProjectItem> {

    private SharedPreferences spInstance;

    public ProjectItemsSharedPreferences(String sharedPreferencesFileName, Context context) {
        this.spInstance = context.getSharedPreferences(
                sharedPreferencesFileName,
                0
        );
    }

    public void registerOnChangeListener(
            SharedPreferences.OnSharedPreferenceChangeListener mListener){
        spInstance.registerOnSharedPreferenceChangeListener(mListener);
    }

    public void unRegisterOnChangeListener(
            SharedPreferences.OnSharedPreferenceChangeListener mListener){
        spInstance.unregisterOnSharedPreferenceChangeListener(mListener);
    }

    /**
     * Store a ProjectItem using it's material list name property as key.
     * If the list exists it would overwrite it.
     */
    @Override
    public void put(ProjectItem projectItem) {
        SharedPreferences.Editor editor = spInstance.edit();
        String json = new Gson().toJson(projectItem);
        editor.putString(projectItem.getMaterialList().getName(), json);
        editor.commit();
    }

    /**
     * Get the ProjectItem by key.
     */
    @Override
    public ProjectItem get(String key) {
        String json = spInstance.getString(key, null);
        return Deserializer.toProjectItem(json);
    }

    /**
     * Get the ProjectItem by position.
     */
    @Override
    public ProjectItem get(int position) {
        ArrayList<ProjectItem> allProjectItems = getAll();
        return allProjectItems.get(position);
    }

    /**
     * Get all the stored ProjectItem as a Map.
     */
    @Override
    public Map<String, ProjectItem> getAllAsMap() {
        Map<String, ProjectItem> newMap = new HashMap<>();
        Map<String, ?> map = spInstance.getAll();
        Set<String> keys = map.keySet();
        for (String key: keys){
            newMap.put(key, get(key));
        }
        return newMap;
    }

    /**
     * Get all the stored ProjectItem as an ArrayList.
     */
    @Override
    public ArrayList<ProjectItem> getAll() {
        return new ArrayList<>(getAllAsMap().values());
    }

    /**
     * Get the keys of all the stored ProjectItem.
     * This is also all the names of ProjectItems.
     */
    @Override
    public ArrayList<String> getAllKeys() {
        return new ArrayList<>(getAllAsMap().keySet());
    }


    /**
     * Remove a ProjectItem by key.
     */
    @Override
    public void remove(String key) {
        SharedPreferences.Editor editor = spInstance.edit();
        editor.remove(key).commit();
    }

    /**
     * Rename a stored ProjectItem.
     */
    @Override
    public void renameKey(String oldName, String newName) {
        ProjectItem projectItem = get(oldName);
        spInstance.edit().remove(oldName).commit();
        projectItem.setName(newName);
        put(projectItem);
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
