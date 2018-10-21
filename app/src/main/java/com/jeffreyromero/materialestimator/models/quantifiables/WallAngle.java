package com.jeffreyromero.materialestimator.models.quantifiables;

import com.jeffreyromero.materialestimator.models.BaseMaterial;
import com.jeffreyromero.materialestimator.models.Quantifiable;

import static java.lang.Math.sqrt;

public class WallAngle extends BaseMaterial implements Quantifiable {

    public WallAngle(String name, double price, double length) {
        super("WallAngle", name, price, length);
    }

    @Override
    public double calcQuantity(double area) {
        double length = sqrt(area);
        return this.calcQuantity(length, length);
    }

    @Override
    public double calcQuantity(double length, double width) {
        double par = (length * 2) + (width * 2);
        return super.setQuantity(par / getLength()) * getCoefficient();
    }
}
