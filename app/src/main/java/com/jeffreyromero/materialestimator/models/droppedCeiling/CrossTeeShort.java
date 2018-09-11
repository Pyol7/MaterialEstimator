package com.jeffreyromero.materialestimator.models.droppedCeiling;

import com.jeffreyromero.materialestimator.models.Material;

public class CrossTeeShort extends Material {

    public CrossTeeShort(String name, double unitPrice, double length) {
        super("CrossTeeShort", name, unitPrice, length);
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
