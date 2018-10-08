package com.jeffreyromero.materialestimator.models.drywall.materials;

import com.jeffreyromero.materialestimator.models.Material;

import static java.lang.Math.sqrt;

public class MainChannel extends Material {
    private double lapLength = 12;

    public MainChannel(String name, double price, double length, int spacing) {
        super("Main Channel", name, price, length, spacing);
    }

    @Override
    public double calcQuantity(double dim1, double dim2) {
        double length = Math.max(dim1, dim2);
        double width = Math.min(dim1, dim2);
        double lengths;

        double pieces = Math.floor(width / getSpacing());
        if (length > getLength()){
            double joints = Math.floor(length / getLength());
            lengths = (length + (joints*lapLength))/getLength();
        } else {
            lengths = length / getLength();
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

