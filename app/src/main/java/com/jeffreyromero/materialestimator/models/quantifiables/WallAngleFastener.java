package com.jeffreyromero.materialestimator.models.quantifiables;

import com.jeffreyromero.materialestimator.models.BaseMaterial;
import com.jeffreyromero.materialestimator.models.Quantifiable;

import static java.lang.Math.sqrt;

public class WallAngleFastener extends BaseMaterial implements Quantifiable {

    public WallAngleFastener(String name, double price, int spacing) {
        super("WallAngleFastener", name, price, spacing);
    }

    @Override
    public double calcQuantity(double area) {
        double length = sqrt(area);
        return this.calcQuantity(length, length);
    }

    @Override
    public double calcQuantity(double length, double width) {
        double par = (length * 2) + (width * 2);
        return super.setQuantity(par / getSpacing());
    }
}
