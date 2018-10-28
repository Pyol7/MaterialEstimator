package com.jeffreyromero.materialestimator.models.quantifiables;

import com.jeffreyromero.materialestimator.models.BaseMaterial;
import com.jeffreyromero.materialestimator.models.Quantifiable;

import static java.lang.Math.sqrt;

public class FurringChannel extends BaseMaterial implements Quantifiable {

    private int lapLength;

    public FurringChannel(String name, double price, double length, int spacing, int lapLength) {
        super("FurringChannel", name, price, length, spacing);
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
        double pieces = (length / getSpacing()) - 1;
        if (width > getLength()){
            double joints = Math.floor(width / getLength());
            lengths = (width + (joints * lapLength))/getLength();
        } else {
            lengths = width / getLength();
        }
        return super.setQuantity(pieces * lengths * getCoefficient());
    }

    public int getLapLength() {
        return lapLength;
    }

    public void setLapLength(int lapLength) {
        this.lapLength = lapLength;
    }
}
