package com.jeffreyromero.materialestimator.models.droppedCeiling;

import com.jeffreyromero.materialestimator.models.Material;

import static java.lang.Math.sqrt;

public class CrossTeeLong extends Material {

    public CrossTeeLong(String name, double unitPrice) {
        super("Cross Tee Long", name, unitPrice, 48);
    }

    @Override
    public double calcQuantity(double dim1, double dim2) {
        double length = Math.max(dim1, dim2);
        double width = Math.min(dim1, dim2);
        double q = (length / getLength()) *
                (width / 24);
        super.setQuantity(q);
        return q;
    }

    @Override
    public double calcQuantity(double area) {
        double length = sqrt(area);
        double q = this.calcQuantity(length, length);
        super.setQuantity(q);
        return q;
    }
}
