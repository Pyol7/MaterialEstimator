package com.jeffreyromero.materialestimator.models.drywall;

import com.jeffreyromero.materialestimator.models.Material;
import static java.lang.Math.sqrt;

public class Track extends Material {

    public Track(String name, double price, double length, double width) {
        super("Track", name, price, length, width);
    }

    @Override
    public double calcQuantity(double roomLength, double roomHeight) {
        double centers = 24;
        double pieces = roomLength/centers;
        double lengths = roomHeight/getLength();
        double q = pieces*lengths;
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
