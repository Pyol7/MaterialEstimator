package com.jeffreyromero.materialestimator.models.drywall;

import com.jeffreyromero.materialestimator.models.Material;
import static java.lang.Math.sqrt;

public class DrywallScrew extends Material {

    public DrywallScrew(String name, double price) {
        super("DrywallScrew", name, price);
    }

    @Override
    public double calcQuantity(double roomLength, double roomWidth) {
        double screwsPer4x8Board = 32;
        double q = (roomLength*roomWidth)/screwsPer4x8Board;
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
