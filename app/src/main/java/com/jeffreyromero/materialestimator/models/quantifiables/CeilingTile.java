package com.jeffreyromero.materialestimator.models.quantifiables;

import com.jeffreyromero.materialestimator.models.BaseMaterial;
import com.jeffreyromero.materialestimator.models.Quantifiable;

import static java.lang.Math.sqrt;

public class CeilingTile extends BaseMaterial implements Quantifiable {

    public CeilingTile(String name, double unitPrice, double length, double width) {
        super("CeilingTile", name, unitPrice, length, width);
    }

    @Override
    public double calcQuantity(double area) {
        double length = sqrt(area);
        return this.calcQuantity(length, length);
    }

    @Override
    public double calcQuantity(double length, double width) {
        double q = (length * width)/(getLength() * getWidth());
        return super.setQuantity(q);
    }
}
