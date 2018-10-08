package com.jeffreyromero.materialestimator.models;

import android.text.format.DateFormat;

import java.util.ArrayList;
import java.util.List;

public class MaterialList{
    private List<BaseMaterial> materialList;
    private String dateCreated;
    private String name;

    public MaterialList(String name) {
        this.materialList = new ArrayList<>();
        this.dateCreated = setDateCreated();
        this.name = name;
    }

    public MaterialList(List<BaseMaterial> materialList, String name) {
        this.dateCreated = setDateCreated();
        this.materialList = materialList;
        this.name = name;
    }

    public List<BaseMaterial> getList() {
        return materialList;
    }

    public void setList(List<BaseMaterial> materialList) {
        this.materialList = materialList;
    }

    public String getName() {
        return name;
    }

    public List<String> getNames() {
        List<String> list = new ArrayList<>();
        for (BaseMaterial material : materialList ) {
            list.add(material.getName());
        }
        return list;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BaseMaterial get(int position) {
        return materialList.get(position);
    }

    public BaseMaterial get(Class clazz){
        for (BaseMaterial bm : materialList) {
            if (clazz.isInstance(bm)) {
                return bm;
            }
        }
        return null;
    }

    public BaseMaterial remove(int position) {
        return materialList.remove(position);
    }

    public void replace(int position, BaseMaterial material) {
        materialList.set(position, material);
    }

    public void add(BaseMaterial material) {
        materialList.add(material);
    }

    public int size() {
        return materialList.size();
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public String setDateCreated() {
        return DateFormat.format("dd-MM-yyyy", new java.util.Date()).toString();
    }

    public List<String> listToString(){
        List<String> l = new ArrayList<>();
        for (BaseMaterial m: materialList){
            l.add(m.toString());
        }
        return l;
    }

    public int getPosition(BaseMaterial material) {
        for (int i = 0; i < materialList.size(); i++) {
            if (materialList.get(i).getName().equals(material.getName())){
                return i;
            }
        }
        return -1;
    }

    @Override
    public String toString() {
        return name;
    }

}
