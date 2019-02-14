package com.jeffreyromero.materialestimator.models;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class Project {
    private String name;
    private String dateCreated;
    private ArrayList<BaseItem> items;
//    private Map<String, Double> completeMaterialList;
    // todo - Remove above from here. There is a copy in every project!!
    // todo - Keep as a method on ProjectFragmentViews.
    // todo - Change above class to ProjectFragmentViews and let it handle displaying list view and complete list view

    public Project(String name) {
        this.name = name;
        this.dateCreated = setDateCreated();
        this.items = new ArrayList<>();
//        this.completeMaterialList = new LinkedHashMap<>();
    }

    public ArrayList<BaseItem> getItems() {
        return items;
    }

    public void addItem(BaseItem item){
        items.add(item);
//        builtCompleteMaterialListFromItems();
    }

    public void deleteItem(int position){
        items.remove(position);
//        builtCompleteMaterialListFromItems();
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

    private String setDateCreated() {
        // Specify format
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US);
        // Create Date object
        Date now = new Date();
        // Apply format
        String dateFormatted = df.format(new Date());
        return dateFormatted;
    }

    public double calcTotalPrice() {
        //Sum price from every project item in the list.
        double total = 0;
        for (BaseItem pi : items) {
            total += pi.getTotalPrice();
        }
        return total;
    }

//    public Map<String, Double> getCompleteMaterialList() {
//        return completeMaterialList;
//    }

//    private void builtCompleteMaterialListFromItems() {

        //Flatten items into one list of material.
//        ArrayList<BaseMaterial> flatList = new ArrayList<>();
//        for (BaseItem item : this.getItems()) {
//            MaterialList mList = item.getMaterialList();
//            for (int i = 0; i < mList.size(); i++) {
//                flatList.add(mList.get(i));
//            }
//        }

        // Sum materials using LinkedHashMap
//        Map<String, BaseMaterial> tempM = new LinkedHashMap<>();
//        for (int i = 0; i < flatList.size(); i++) {
//            String currentKey = flatList.get(i).getName();
//            if (tempM.containsKey(currentKey)) {
//                BaseMaterial sto = tempM.get(currentKey);
//                BaseMaterial curr = flatList.get(i);
//                double sum = sto.getQuantity() + curr.getQuantity();
//                sto.setQuantity(555);
//                tempM.put(currentKey, sto);
//            } else {
//                tempM.put(currentKey, flatList.get(i));
//            }
//        }

        // Sum BaseMaterial using ArrayList by modifying the equals and hashcode in BaseMaterial
//        ArrayList<BaseMaterial> newList = new ArrayList<>();
//        for (int i = 0; i < flatList.size(); i++) {
//            BaseMaterial currentMaterial = flatList.get(i);
//            if (newList.contains(currentMaterial)) {
//                BaseMaterial stored = newList.get(newList.indexOf(currentMaterial));
//                double sum = stored.getQuantity() + currentMaterial.getQuantity();
//                stored.setQuantity(sum);
//            } else {
//                newList.add(currentMaterial);
//            }
//        }

        // Both of the above methods did not work as it modified the same objects.
        // Create a new map of primitives derived from summing the fields from the BaseMaterials
//        completeMaterialList.clear();
//        for (int i = 0; i < flatList.size(); i++) {
//            String currentKey = flatList.get(i).getName();
//            if (completeMaterialList.containsKey(currentKey)) {
//                Double storedQuantity = completeMaterialList.get(currentKey);
//                Double currentQuantity = flatList.get(i).getQuantity();
//                double sum = storedQuantity + currentQuantity;
//                completeMaterialList.put(currentKey, sum);
//            } else {
//                completeMaterialList.put(currentKey, flatList.get(i).getQuantity());
//            }
//        }

//    }

    @Override
    public String toString() {
        return name;
    }

}

