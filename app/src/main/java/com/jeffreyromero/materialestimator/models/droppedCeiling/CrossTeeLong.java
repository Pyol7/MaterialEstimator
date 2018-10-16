package com.jeffreyromero.materialestimator.models.droppedCeiling;

import com.jeffreyromero.materialestimator.models.Material;

import static java.lang.Math.sqrt;

public class CrossTeeLong extends Material {

    public CrossTeeLong(String name, double unitPrice, double length, int spacing) {
        super("Cross Tee Long", name, unitPrice, length, spacing);
    }

    @Override
    public double calcQuantity(double length, double width) {
        double q = (length / getLength()) * (width / 24);
        return  super.setQuantity(q);
    }

    @Override
    public double calcQuantity(double area) {
        double length = sqrt(area);
        double q = this.calcQuantity(length, length);
        return super.setQuantity(q);
    }
}
