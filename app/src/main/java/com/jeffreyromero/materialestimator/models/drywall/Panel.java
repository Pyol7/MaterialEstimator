package com.jeffreyromero.materialestimator.models.drywall;

import com.jeffreyromero.materialestimator.models.Material;

import static java.lang.Math.sqrt;

public class Panel extends Material {

    public Panel(String name, double price, double length, double width) {
        super("Panel", name, price, length, width);
    }

    @Override
    public double calcQuantity(double roomLength, double roomWidth) {
        double area = roomLength*roomWidth;
        double q = area/(getLength()*getWidth());
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