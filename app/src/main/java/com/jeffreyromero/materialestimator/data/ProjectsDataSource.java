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

/**
 * Provides a single access point to stored SharedPreferences data.
 *
 * Usage:
 * ProjectDataSource projectDataSource = new ProjectDataSource(context);
 * Project p = projectDataSource.get("R.string.sample_project");
 *
 */
public class ProjectsDataSource implements DataSource<Project> {

    private SharedPreferences spInstance;

    public ProjectsDataSource(Context context) {
        this.spInstance = context.getSharedPreferences(
                context.getString(R.string.projects_key),
                0
        );
    }

    /**
     * Store a Project using it's name property as key.
     * If the list exists it would overwrite it.
     */
    @Override
    public void put(Project project) {
        SharedPreferences.Editor editor = spInstance.edit();
        String json = new Gson().toJson(project);
        editor.putString(project.getName(), json);
        editor.commit();
    }

    /**
     * Get the Project by key.
     */
    @Override
    public Project get(String key) {
        String json = spInstance.getString(key, null);
        return Deserializer.toProject(json);
    }

    /**
     * Get the MaterialList by position.
     */
    @Override
    public Project get(int position) {
        ArrayList<Project> projects = getAll();
        return projects.get(position);
    }

    /**
     * Get all the stored Project as an ArrayList.
     */
    @Override
    public ArrayList<Project> getAll() {
        return new ArrayList<>(getAllAsMap().values());
    }

    /**
     * Get all the stored MaterialList as a Map.
     */
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

    /**
     * Get the keys of all the stored Project.
     * This is also all the names of Projects.
     */
    @Override
    public ArrayList<String> getAllKeys() {
        Map<String, ?> map = spInstance.getAll();
        if (map != null) {
            return new ArrayList<>(map.keySet());
        }
        return null;
    }


    /**
     * Remove a Project by key.
     */
    @Override
    public void remove(String key) {
        SharedPreferences.Editor editor = spInstance.edit();
        editor.remove(key).commit();
    }

    /**
     * Rename a stored Project.
     */
    @Override
    public void renameKey(String oldName, String newName) {
        Project project = get(oldName);
        spInstance.edit().remove(oldName).commit();
        project.setName(newName);
        put(project);
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
