package com.jeffreyromero.materialestimator.models;


import android.os.Build;

import java.util.Objects;

/**
 * The type field is used by Gson to identify subtypes for deserialization.
 * All subtypes must provide a type and that type must be registered with
 * an instance of GsonRuntimeTypeAdapterFactory(Deserializer.class in this case).
 *
 */
public abstract class Material implements Quantifiable {
    protected String type;
    protected String name;
    protected double unitPrice;
    protected double length;
    private double width;
    protected double quantity;
    protected double price;

    protected Material(String type, String name, double unitPrice, double length, double width) {
        this.type = type;
        this.name = name;
        this.unitPrice = unitPrice;
        this.length = length;
        this.width = width;
        this.price = getQuantity()*getUnitPrice();
    }

    protected Material(String type, String name, double unitPrice, double length) {
        this.type = type;
        this.name = name;
        this.unitPrice = unitPrice;
        this.length = length;
        this.price = getQuantity()*getUnitPrice();
    }

    protected Material(String type, String name, double unitPrice) {
        this.type = type;
        this.name = name;
        this.unitPrice = unitPrice;
        this.price = getQuantity()*getUnitPrice();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
        this.price = unitPrice*getQuantity();
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

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
        this.price = quantity*getUnitPrice();
    }

    public double getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
//        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Material material = (Material) o;

        return name != null ? name.equals(material.name) : material.name == null;
    }

    //    String oName = ((Material) o).getName().trim();
//        String name = this.getName().trim();
//        return name.equalsIgnoreCase(oName);

    @Override
    public String toString() {
        return name;
    }
}
