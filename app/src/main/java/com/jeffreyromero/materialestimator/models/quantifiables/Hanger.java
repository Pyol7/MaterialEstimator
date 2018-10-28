package com.jeffreyromero.materialestimator.models.quantifiables;

import com.jeffreyromero.materialestimator.models.BaseMaterial;
import com.jeffreyromero.materialestimator.models.Quantifiable;

import static java.lang.Math.sqrt;

public class Hanger extends BaseMaterial implements Quantifiable {

    public Hanger(String name, double price, int spacing) {
        super("Hanger", name, price, spacing);
    }

    @Override
    public double calcQuantity(double area) {
        double length = sqrt(area);
        return this.calcQuantity(length, length);
    }

    @Override
    public double calcQuantity(double length, double width) {
        double q = Math.ceil(
                (length / getSpacing() - 1 ) * (width / getSpacing() - 1)) * getCoefficient();
        return super.setQuantity((q%2 != 0) ? q+1 : q);
    }
}
