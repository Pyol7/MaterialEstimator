package com.jeffreyromero.materialestimator.models;

/**
 * The type field is used by Gson to identify subtypes for deserialization.
 * All subtypes must provide a String field called type and that type must be registered with
 * an instance of GsonRuntimeTypeAdapterFactory(Deserializer.class in this case).
 *
 */
public abstract class BaseMaterial {
    protected String type;
    protected String name;
    protected double unitPrice;
    protected double length;
    protected double width;
    protected int spacing;
    protected double quantity;
    protected double price;

    protected BaseMaterial(String type, String name, double unitPrice, double length, double width, int spacing) {
        this.type = type;
        this.name = name;
        this.unitPrice = unitPrice;
        this.length = length;
        this.width = width;
        this.spacing = spacing;
        this.price = getQuantity()*getUnitPrice();
    }

    protected BaseMaterial(String type, String name, double unitPrice, double length, double width) {
        this.type = type;
        this.name = name;
        this.unitPrice = unitPrice;
        this.length = length;
        this.width = width;
        this.price = getQuantity()*getUnitPrice();
    }

    protected BaseMaterial(String type, String name, double unitPrice, double length, int spacing) {
        this.type = type;
        this.name = name;
        this.unitPrice = unitPrice;
        this.length = length;
        this.spacing = spacing;
        this.price = getQuantity()*getUnitPrice();
    }

    protected BaseMaterial(String type, String name, double unitPrice, double length) {
        this.type = type;
        this.name = name;
        this.unitPrice = unitPrice;
        this.length = length;
        this.price = getQuantity()*getUnitPrice();
    }

    protected BaseMaterial(String type, String name, double unitPrice, int spacing) {
        this.type = type;
        this.name = name;
        this.unitPrice = unitPrice;
        this.spacing = spacing;
        this.price = getQuantity()*getUnitPrice();
    }

    protected BaseMaterial(String type, String name, double unitPrice) {
        this.type = type;
        this.name = name;
        this.unitPrice = unitPrice;
        this.price = getQuantity()*getUnitPrice();
    }

    public String getType() {
        return type;
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

    public double setQuantity(double quantity) {
        this.quantity = quantity;
        this.price = quantity*getUnitPrice();
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        BaseMaterial material = (BaseMaterial) o;

        return name != null ? name.equals(material.name) : material.name == null;
    }

    @Override
    public String toString() {
        return name;
    }

    public int getSpacing() {
        return spacing;
    }

    public void setSpacing(int spacing) {
        this.spacing = spacing;
    }
}
