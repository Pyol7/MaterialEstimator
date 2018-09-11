package com.jeffreyromero.materialestimator.models;

/**
 * The type field is used by Gson to identify subtypes for deserialization.
 * All subtypes must provide a type and that type must be registered with
 * an instance of GsonRuntimeTypeAdapterFactory(Deserializer.class in this case).
 *
 */
public abstract class Material implements Quantifiable {
    protected String type;
    protected String name;
    protected int price;
    protected double length;
    private double width;
    protected double quantity;

    protected Material(String type, String name, int price, double length, double width) {
        this.type = type;
        this.name = name;
        this.price = price;
        this.length = length;
        this.width = width;
    }

    protected Material(String type, String name, int price, double length) {
        this.type = type;
        this.name = name;
        this.price = price;
        this.length = length;
    }

    protected Material(String type, String name, int price) {
        this.type = type;
        this.name = name;
        this.price = price;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
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
    }

    @Override
    public String toString() {
        return name;
    }
}
