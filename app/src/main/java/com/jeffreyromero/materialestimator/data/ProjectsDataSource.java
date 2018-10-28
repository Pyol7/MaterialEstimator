package com.jeffreyromero.materialestimator.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.jeffreyromero.materialestimator.R;
import com.jeffreyromero.materialestimator.models.Project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ProjectsDataSource implements DataSource<String, Project> {

    private SharedPreferences spInstance;

    public ProjectsDataSource(String sharedPreferencesFileName, Context context) {
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
     * Stores an Object using it's <em>name</em> property as key.
     * If the key exists the Object would be overwritten by the Object provided.
     *
     * @param key     The key to use to store the Object.
     * @param project The Object to store.
     */
    @Override
    public void put(String key, Project project) {
        SharedPreferences.Editor editor = spInstance.edit();
        String json = new Gson().toJson(project);
        editor.putString(project.getName(), json);
        editor.commit();
    }

    @Override
    public Project get(String key) {
        String json = spInstance.getString(key, null);
        return Deserializer.toProject(json);
    }

    @Override
    public Project get(int position) {
        ArrayList<Project> projects = getAll();
        return projects.get(position);
    }

    @Override
    public ArrayList<Project> getAll() {
        return new ArrayList<>(getAllAsMap().values());
    }

    @Override
    public Map<String, Project> getAllAsMap() {
        Map<String, Project> newMap = new HashMap<>();
        Map<String, ?> map = spInstance.getAll();
        Set<String> keys = map.keySet();
        for (String key: keys){
            newMap.put(key, get(key));
        }
        return newMap;
    }

    @Override
    public ArrayList<String> getAllKeys() {
        Map<String, ?> map = spInstance.getAll();
        if (map != null) {
            return new ArrayList<>(map.keySet());
        }
        return null;
    }

    @Override
    public void replace(String key, Project project) {
        put(key, project);
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
