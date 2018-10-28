package com.jeffreyromero.materialestimator.models.quantifiables;

import com.jeffreyromero.materialestimator.models.BaseMaterial;
import com.jeffreyromero.materialestimator.models.Quantifiable;

import static java.lang.Math.sqrt;

public class Panel extends BaseMaterial implements Quantifiable {

    public Panel(String name, double price, double length, double width) {
        super("Panel", name, price, length, width);
    }

    @Override
    public double calcQuantity(double area) {
        double length = sqrt(area);
        return this.calcQuantity(length, length);
    }

    @Override
    public double calcQuantity(double length, double width) {
        double area = length*width;
        return super.setQuantity(area / (getLength() * getWidth()) * getCoefficient());
    }

}
