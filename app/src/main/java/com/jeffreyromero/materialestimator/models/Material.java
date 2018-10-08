package com.jeffreyromero.materialestimator.models;

public abstract class Material extends BaseMaterial implements Quantifiable {

    protected Material(String type, String name, double unitPrice, double length, double width, int spacing) {
        super(type, name, unitPrice, length, width, spacing);
    }

    protected Material(String type, String name, double unitPrice, double length, double width) {
        super(type, name, unitPrice, length, width);
    }

    protected Material(String type, String name, double unitPrice, double length, int spacing) {
        super(type, name, unitPrice, length, spacing);
    }

    protected Material(String type, String name, double unitPrice, double length) {
        super(type, name, unitPrice, length);
    }

    protected Material(String type, String name, double unitPrice, int spacing) {
        super(type, name, unitPrice, spacing);
    }

    protected Material(String type, String name, double unitPrice) {
        super(type, name, unitPrice);
    }
}
