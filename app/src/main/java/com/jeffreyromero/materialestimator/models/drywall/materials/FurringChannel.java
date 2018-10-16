package com.jeffreyromero.materialestimator.models.drywall.materials;

import com.jeffreyromero.materialestimator.models.Material;

import static java.lang.Math.sqrt;

public class FurringChannel extends Material {

    private double lapLength = 12;

    public FurringChannel(String name, double price, double length, int spacing) {
        super("Furring Channel", name, price, length, spacing);
    }

    @Override
    public double calcQuantity(double length, double width) {
        double lengths;

        double pieces = (length / getSpacing()) - 1;
        if (width > getLength()){
            double joints = Math.floor(width / getLength());
            lengths = (width + (joints*lapLength))/getLength();
        } else {
            lengths = width / getLength();
        }
        return super.setQuantity(pieces * lengths);
    }

    @Override
    public double calcQuantity(double area) {
        double length = sqrt(area);
        double q = this.calcQuantity(length, length);
        return super.setQuantity(q);
    }

    public double getLapLength() {
        return lapLength;
    }

    public void setLapLength(double lapLength) {
        this.lapLength = lapLength;
    }
}
