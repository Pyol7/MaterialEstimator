package com.jeffreyromero.materialestimator.models.quantifiables;

import com.jeffreyromero.materialestimator.models.BaseMaterial;
import com.jeffreyromero.materialestimator.models.Quantifiable;

import static java.lang.Math.sqrt;

public class TrackFastener extends BaseMaterial implements Quantifiable {

    public TrackFastener(String name, double price, int spacing) {
        super("TrackFastener", name, price, spacing);
    }

    @Override
    public double calcQuantity(double area) {
        double length = sqrt(area);
        return this.calcQuantity(length, length);
    }

    @Override
    public double calcQuantity(double length, double height) {
        return super.setQuantity(length / getSpacing());
    }
}
