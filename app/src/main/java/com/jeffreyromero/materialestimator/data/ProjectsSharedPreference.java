package com.jeffreyromero.materialestimator.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.jeffreyromero.materialestimator.models.Project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ProjectsSharedPreference implements DataSource<String, Project> {

    private SharedPreferences spInstance;

    public ProjectsSharedPreference(Context context, String sharedPreferencesFileName) {
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
     *  @param key     The key to use to store the Object.
     * @param project The Object to store.
     */
    @Override
    public ArrayList<Project> put(String key, Project project) {
        SharedPreferences.Editor editor = spInstance.edit();
        String json = new Gson().toJson(project);
        editor.putString(project.getName(), json);
        editor.commit();
        return getAll();
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
    public Map<String, ?> getAllAsMap() {
        return spInstance.getAll();
    }

    @Override
    public ArrayList<Project> getAll() {
        List<?> unSortedStrings = new ArrayList<>(getAllAsMap().values());
        // Convert values to Project objects
        ArrayList<Project> projects = new ArrayList<>();
        for (Object p: unSortedStrings){
            Project project = Deserializer.toProject(p.toString());
            projects.add(project);
        }
        Collections.sort(projects, new Comparator<Project>() {
            @Override
            public int compare(Project o1, Project o2) {
                return o1.getDateCreated().compareTo(
                        o2.getDateCreated());
            }
        });
        return projects;
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
    public ArrayList<Project> remove(String key) {
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
