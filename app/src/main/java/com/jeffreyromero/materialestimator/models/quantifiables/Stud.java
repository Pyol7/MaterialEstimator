package com.jeffreyromero.materialestimator.models.quantifiables;

import com.jeffreyromero.materialestimator.models.BaseMaterial;
import com.jeffreyromero.materialestimator.models.Quantifiable;

import static java.lang.Math.sqrt;

public class Stud extends BaseMaterial implements Quantifiable {

    public Stud(String name, double price, double length, int spacing) {
        super("Stud", name, price, length, spacing);
    }

    @Override
    public double calcQuantity(double area) {
        double length = sqrt(area);
        return this.calcQuantity(length, length);
    }

    @Override
    public double calcQuantity(double length, double height) {
        double pieces = length / getSpacing();
        double lengths = height/getLength();
        return super.setQuantity(pieces * lengths * getCoefficient());
    }
}
