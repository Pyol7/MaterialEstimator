package com.jeffreyromero.materialestimator.models.drywall.materials;

import com.jeffreyromero.materialestimator.models.Material;

import static java.lang.Math.sqrt;

public class Track extends Material {

    public Track(String name, double price, double length) {
        super("Track", name, price, length);
    }

    @Override
    public double calcQuantity(double length, double height) {
        double q = length/getLength()*2;
        return super.setQuantity(q);
    }

    @Override
    public double calcQuantity(double area) {
        double length = sqrt(area);
        double q = this.calcQuantity(length, length);
        return super.setQuantity(q);
    }
}
