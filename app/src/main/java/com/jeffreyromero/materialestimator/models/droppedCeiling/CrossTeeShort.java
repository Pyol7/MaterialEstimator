package com.jeffreyromero.materialestimator.models.droppedCeiling;

import com.jeffreyromero.materialestimator.models.Material;

public class CrossTeeShort extends Material {

    public CrossTeeShort(String name, int price, double length) {
        super("CrossTeeShort", name, price, length);
    }

    @Override
    public double calcQuantity(double roomLength, double roomWidth) {
        double q = getLength();
        super.setQuantity(q);
        return q;
    }

    @Override
    public double calcQuantity(double area) {
        double q = getLength();
        super.setQuantity(q);
        return q;
    }
}
