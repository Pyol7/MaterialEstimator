package com.jeffreyromero.materialestimator.models;

import android.text.format.DateFormat;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class Project {
    private ArrayList<ProjectItem> projectItems;
    private ArrayList<BaseMaterial> materialListFromProjectItems;
    private double totalPrice;
    private String dateCreated;
    private String location;
    private String client;
    private String name;

    public Project(String name) {
        this.projectItems = new ArrayList<>();
        this.dateCreated = setDateCreated();
        this.name = name;
    }

    public Project(String name, ArrayList<ProjectItem> projectItems) {
        this.dateCreated = setDateCreated();
        this.projectItems = projectItems;
        builtMaterialListFromProjectItems();
        this.name = name;
    }

    public ArrayList<ProjectItem> getProjectItems() {
        return projectItems;
    }

    public void setProjectItems(ArrayList<ProjectItem> projectItems) {
        this.projectItems = projectItems;
        builtMaterialListFromProjectItems();
    }

    public void addProjectItem(ProjectItem projectItem){
        projectItems.add(projectItem);
        builtMaterialListFromProjectItems();
    }

    public void deleteProjectItem(int position){
        projectItems.remove(position);
        builtMaterialListFromProjectItems();
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

    public void setDateCreated(String date) {
        this.dateCreated = date;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public double calcTotalPrice() {
        //Sum price from every project item in the list.
        double total = 0;
        for (ProjectItem pi : projectItems ) {
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
        return materialListFromProjectItems;
    }

    private void builtMaterialListFromProjectItems() {

        //Create a flat list containing all materials from each ProjectItem.
        ArrayList<BaseMaterial> flatList = new ArrayList<>();
        for (ProjectItem pi : this.getProjectItems()) {
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

        this.materialListFromProjectItems = new ArrayList<>(map.values());
    }

    @Override
    public String toString() {
        return name;
    }

}

