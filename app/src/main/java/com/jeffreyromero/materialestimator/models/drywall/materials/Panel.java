package com.jeffreyromero.materialestimator.models.drywall.materials;

import com.jeffreyromero.materialestimator.models.Material;

import static java.lang.Math.sqrt;

public class Panel extends Material {

    public Panel(String name, double price, double length, double width, int spacing) {
        super("Panel", name, price, length, width, spacing);
    }

    @Override
    public double calcQuantity(double dim1, double dim2) {
        double area = dim1*dim2;
        double q = area /(getLength() * getWidth());
        return super.setQuantity(q);
    }

    @Override
    public double calcQuantity(double area) {
        double length = sqrt(area);
        double q = this.calcQuantity(length, length);
        return super.setQuantity(q);
    }
}
