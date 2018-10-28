package com.jeffreyromero.materialestimator.models.quantifiables;

import com.jeffreyromero.materialestimator.models.BaseMaterial;
import com.jeffreyromero.materialestimator.models.Quantifiable;

import static java.lang.Math.sqrt;

public class Track extends BaseMaterial implements Quantifiable {

    public Track(String name, double price, double length) {
        super("Track", name, price, length);
    }

    @Override
    public double calcQuantity(double area) {
        double length = sqrt(area);
        return this.calcQuantity(length, length);
    }

    @Override
    public double calcQuantity(double length, double width) {
        return super.setQuantity(length / getLength() * 2 * getCoefficient());
    }
}
