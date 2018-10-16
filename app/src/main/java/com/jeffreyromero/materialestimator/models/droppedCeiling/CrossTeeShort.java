package com.jeffreyromero.materialestimator.models.droppedCeiling;

import com.jeffreyromero.materialestimator.models.Material;

import static java.lang.Math.sqrt;

public class CrossTeeShort extends Material {

    public CrossTeeShort(String name, double unitPrice, double length, int spacing) {
        super("Cross Tee Short", name, unitPrice, length, spacing);
    }

    @Override
    public double calcQuantity(double length, double width) {
        double q = (width / getLength()) * (length / 48);
        return super.setQuantity(q);
    }

    @Override
    public double calcQuantity(double area) {
        double length = sqrt(area);
        double q = this.calcQuantity(length, length);
        return super.setQuantity(q);
    }
}
