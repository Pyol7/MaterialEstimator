package com.jeffreyromero.materialestimator.data;

import android.content.SharedPreferences;

import com.jeffreyromero.materialestimator.models.Project;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//todo - change to an abstract class to remove code duplication

/**
 * Provides a single location to store and retrieve Objects.
 */
public interface DataSource<String, T> {

    /**
     * Check if Shared preferences is empty.
     * @return Returns true if empty, otherwise false.
     */
    boolean isEmpty();

    /**
     * Check if a Object exists.
     * @return Returns true if exists, otherwise false.
     */
    boolean contains(String key);

    /**
     * Stores an Object using it's <em>name</em> property as key
     *  and return the new list of objects.
     * If the key exists the Object would be overwritten by the Object provided.
     *  @param key   The key to use to store the Object.
     * @param t     The Object to store.
     */
    ArrayList<T> put(String key, T t);

    /**
     * Get the Object with a particular key.
     */
    T get(String key);

    /**
     * Retrieve a list of Objects and get the Object at a particular position in that list.
     * @param position The position in the list.
     * @return Returns the Object at the position.
     */
    T get(int position);

    /**
     * Get all stored Objects.
     */
    ArrayList<T> getAll();

    /**
     * Get all stored objects as a Map<String, Object>.
     */
    Map<String, ?> getAllAsMap();

    /**
     * Get a list of all the keys.
     */
    List<String> getAllKeys();

    /**
     * Replace a stored Object with a new one.
     * @param key key to use to store the Object.
     * @param t The Object to store.
     */
    void replace(String key, T t);

    /**
     * Delete an Object and return the new list of objects.
     * @param key Key of object to delete.
     */
    ArrayList<T> remove(String key);

    /**
     * Delete all Objects.
     */
    void clearAll();

    /**
     * Registers a callback to be invoked when a change happens to a preference.
     * Hold a strong reference to avoid garbage collection.
     * @param mListener The callback that will run.
     */
    void registerOnChangeListener(SharedPreferences.OnSharedPreferenceChangeListener mListener);

    /**
     * Unregister previously stored callback.
     * @param mListener The callback that should be unregistered.
     */
    void unRegisterOnChangeListener(SharedPreferences.OnSharedPreferenceChangeListener mListener);
}

