package com.jeffreyromero.materialestimator.data;

import java.util.ArrayList;
import java.util.Map;

public interface DataSource<T> {

    void put(T t);

    T get(String key);

    T get(int position);

    ArrayList<T> getAll();

    Map<String,T> getAllAsMap();

    ArrayList<String> getAllKeys();

    void remove(String key);

    void renameKey(String oldKey, String newKey);

    boolean contains(String key);

    boolean isEmpty();

    void clearAll();

}

