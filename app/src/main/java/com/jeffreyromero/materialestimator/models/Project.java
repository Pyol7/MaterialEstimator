package com.jeffreyromero.materialestimator.models;

import android.text.format.DateFormat;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class Project {
    private ArrayList<BaseItem> items;
    private ArrayList<BaseMaterial> materialListFromItems;
    private double totalPrice;
    private String dateCreated;
    private String location;
    private String client;
    private String name;

    public Project(String name) {
        this.items = new ArrayList<>();
        this.dateCreated = setDateCreated();
        this.name = name;
    }

    public ArrayList<BaseItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<BaseItem> items) {
        this.items = items;
        builtMaterialListFromItems();
    }

    public void addItem(BaseItem item){
        items.add(item);
        builtMaterialListFromItems();
    }

    public void deleteItem(int position){
        items.remove(position);
        builtMaterialListFromItems();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public String setDateCreated() {
        return DateFormat.format("dd-MM-yyyy", new java.util.Date()).toString();
    }

    public double calcTotalPrice() {
        //Sum price from every project item in the list.
        double total = 0;
        for (BaseItem pi : items) {
            total += pi.getTotalPrice();
        }
        this.totalPrice = total;
        return total;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public ArrayList<BaseMaterial> getMaterialList() {
        return materialListFromItems;
    }

    private void builtMaterialListFromItems() {

        //Create a flat list containing all materials from each BaseItem.
        ArrayList<BaseMaterial> flatList = new ArrayList<>();
        for (BaseItem pi : this.getItems()) {
            MaterialList mList = pi.getMaterialList();
            for (int i = 0; i < mList.size(); i++) {
                flatList.add(mList.get(i));
            }
        }

        //Create a map while summing the quantities of materials with the same name.
        Map<String, BaseMaterial> map = new LinkedHashMap<>();
        for (int i = 0; i < flatList.size(); i++) {
            String currentKey = flatList.get(i).getName();
            if (map.containsKey(currentKey)) {
                BaseMaterial stored = map.get(currentKey);
                BaseMaterial current = flatList.get(i);
                double sum = stored.getQuantity() + current.getQuantity();
                stored.setQuantity(sum);
                map.put(currentKey, stored);
            } else {
                map.put(currentKey, flatList.get(i));
            }
        }

        this.materialListFromItems = new ArrayList<>(map.values());
    }

    @Override
    public String toString() {
        return name;
    }

}

