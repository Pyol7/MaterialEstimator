package com.jeffreyromero.materialestimator.models;

import org.apache.commons.lang3.StringUtils;

public abstract class ProjectItem implements IProjectItem{
    private String subType;
    private MaterialList materialList;
    private double totalPrice;
    protected double length;
    protected double width;
    protected double height;
    protected String name;

    protected ProjectItem(String subType, String name) {
        this.subType = subType;
        this.name = name;
    }

    public String getSubType() {
        return subType;
    }

    public MaterialList getMaterialList() {
        return materialList;
    }

    protected void setMaterialList(MaterialList materialList) {
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

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public double calcTotalPrice() {
        //Sum price from every material in the list.
        double total = 0;
        for (BaseMaterial material : materialList.getList() ) {
            total += material.getPrice();
        }
        this.totalPrice = total;
        return total;
    }

    @Override
    public String toString() {
        return name;
    }

    protected String generateNameFromClassName(){
        return StringUtils.join(
                StringUtils.splitByCharacterTypeCamelCase( getClass().getSimpleName()),
                ' '
        );
    }
}
