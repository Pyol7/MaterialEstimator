package com.jeffreyromero.materialestimator.models.quantifiables;

import com.jeffreyromero.materialestimator.models.BaseMaterial;
import com.jeffreyromero.materialestimator.models.Quantifiable;

import static java.lang.Math.sqrt;

public class MainSupport extends BaseMaterial implements Quantifiable {

    private int lapLength;

    public MainSupport(String name, double price, double length, int spacing, int lapLength) {
        super("MainSupport", name, price, length, spacing);
        this.lapLength = lapLength;
    }

    @Override
    public double calcQuantity(double area) {
        double length = sqrt(area);
        return this.calcQuantity(length, length);
    }

    @Override
    public double calcQuantity(double length, double width) {
        double lengths;
        double pieces = Math.floor(width / getSpacing());
        if (length > getLength()){
            double joints = Math.floor(length / getLength());
            lengths = (length + (joints * lapLength))/getLength();
        } else {
            lengths = length / getLength();
        }
        return super.setQuantity(pieces * lengths);
    }

    public int getLapLength() {
        return lapLength;
    }

    public void setLapLength(int lapLength) {
        this.lapLength = lapLength;
    }
}

