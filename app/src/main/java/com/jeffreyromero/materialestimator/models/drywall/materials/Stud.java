package com.jeffreyromero.materialestimator.models.drywall.materials;

import com.jeffreyromero.materialestimator.models.Material;

import static java.lang.Math.sqrt;

public class Stud extends Material {

    public Stud(String name, double price, double length, int spacing) {
        super("Stud", name, price, length, spacing);
    }

    //todo - get exact quantities(if statements etc)
    @Override
    public double calcQuantity(double length, double height) {
        double pieces = length/ getSpacing();
        double lengths = height/getLength();
        return super.setQuantity(pieces*lengths);
    }

    @Override
    public double calcQuantity(double area) {
        double length = sqrt(area);
        double q = this.calcQuantity(length, length);
        return super.setQuantity(q);
    }
}
