package com.jeffreyromero.materialestimator.models;

/**
 * The subType field is used by Gson to identify subTypes for deserialization.
 * All subTypes must use their class name for the subType field and must be registered with
 * their parent class TypeAdapter (see Deserializer.class).
 *
 */
public abstract class BaseMaterial{
    protected String subType;
    protected String name;
    protected double unitPrice;
    protected double length;
    protected double width;
    protected int spacing;
    protected double quantity;
    protected double price;
    protected double coefficient;

    protected BaseMaterial(String subType,
                           String name,
                           double unitPrice)
    {
        this(subType, name, unitPrice, -1);
    }

    protected BaseMaterial(String subType,
                           String name,
                           double unitPrice,
                           int spacing)
    {
        this(subType, name, unitPrice, -1,-1, spacing);
    }

    protected BaseMaterial(String subType,
                           String name,
                           double unitPrice,
                           double length)
    {
        this(subType, name, unitPrice, length, -1);
    }

    protected BaseMaterial(String subType,
                           String name,
                           double unitPrice,
                           double length,
                           int spacing)
    {
        this(subType, name, unitPrice, length, -1, spacing);
    }

    protected BaseMaterial(String subType,
                           String name,
                           double unitPrice,
                           double length,
                           double width)
    {
        this(subType, name, unitPrice, length, width,-1);
    }

    protected BaseMaterial(String subType,
                           String name,
                           double unitPrice,
                           double length,
                           double width,
                           int spacing)
    {
        this.subType = subType;
        this.name = name;
        this.unitPrice = unitPrice;
        this.length = length;
        this.width = width;
        this.spacing = spacing;
        this.price = getQuantity()*getUnitPrice();
        this.coefficient = 1;
    }

    public String getSubType() {
        return subType;
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

    public int getSpacing() {
        return spacing;
    }

    public void setSpacing(int spacing) {
        this.spacing = spacing;
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

    public double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(double coefficient) {
        this.coefficient = coefficient;
    }

    @Override
    public String toString() {
        return name;
    }

}
