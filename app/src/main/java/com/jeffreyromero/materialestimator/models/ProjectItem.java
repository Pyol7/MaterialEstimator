package com.jeffreyromero.materialestimator.models;

public class ProjectItem {
    private String name;
    private double length;
    private double width;
    private double totalPrice;
    private MaterialList materialList;

    public ProjectItem(String name) {
        this.name = name;
    }

    public ProjectItem(String name, double length, double width, MaterialList materialList) {
        this.name = name;
        this.length = length;
        this.width = width;
        this.materialList = materialList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public MaterialList getMaterialList() {
        return materialList;
    }

    public void setMaterialList(MaterialList materialList) {
        this.materialList = materialList;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void calcTotalPrice() {
        //Sum price from every material in the list.
        double total = 0;
        for (Material material : materialList.getList() ) {
            total += material.getPrice();
        }
        this.totalPrice = total;
    }

    @Override
    public String toString() {
        return name;
    }
}
