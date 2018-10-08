package com.jeffreyromero.materialestimator.models.droppedCeiling;

import com.jeffreyromero.materialestimator.models.Material;

import static java.lang.Math.sqrt;

public class MainTee extends Material {

    public MainTee(String name, double unitPrice, double length, int spacing) {
        super("Main Tee", name, unitPrice, length, spacing);
    }

    @Override
    public double calcQuantity(double dim1, double dim2) {
        double length = Math.max(dim1, dim2);
        double width = Math.min(dim1, dim2);
        double q = (length / getSpacing()) * (width / getLength());
        return super.setQuantity(q);
    }

    @Override
    public double calcQuantity(double area) {
        double length = sqrt(area);
        double q = this.calcQuantity(length, length);
        return super.setQuantity(q);
    }
}
