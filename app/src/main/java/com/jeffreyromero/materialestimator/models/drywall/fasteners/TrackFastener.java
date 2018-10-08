package com.jeffreyromero.materialestimator.models.drywall.fasteners;

import com.jeffreyromero.materialestimator.models.Material;

import static java.lang.Math.sqrt;

public class TrackFastener extends Material {

    public TrackFastener(String name, double price, int spacing) {
        super("Track Fastener", name, price, spacing);
    }

    @Override
    public double calcQuantity(double length, double height) {
        double q = length / getSpacing();
        return super.setQuantity(q);
    }

    @Override
    public double calcQuantity(double area) {
        double length = sqrt(area);
        double q = this.calcQuantity(length, length);
        return super.setQuantity(q);
    }
}
