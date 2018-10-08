package com.jeffreyromero.materialestimator.models.drywall.materials;

import com.jeffreyromero.materialestimator.models.Material;

import static java.lang.Math.sqrt;

public class WallAngle extends Material {

    public WallAngle(String name, double price, double length) {
        super("Wall Angle", name, price, length);
    }

    @Override
    public double calcQuantity(double dim1, double dim2) {
        double length = Math.max(dim1, dim2);
        double width = Math.min(dim1, dim2);

        double par = (length * 2) + (width * 2);
        double q = par / getLength();
        return super.setQuantity(q);
    }

    @Override
    public double calcQuantity(double area) {
        double length = sqrt(area);
        double q = this.calcQuantity(length, length);
        return super.setQuantity(q);
    }
}
