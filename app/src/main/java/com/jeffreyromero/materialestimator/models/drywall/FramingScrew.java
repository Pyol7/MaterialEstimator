package com.jeffreyromero.materialestimator.models.drywall;

import com.jeffreyromero.materialestimator.models.Material;

public class FramingScrew extends Material {

    public FramingScrew(String name, double price) {
        super("FramingScrew", name, price);
    }

    @Override
    public double calcQuantity(double roomLength, double roomWidth) {
        double q = getPrice();
        super.setQuantity(q);
        return q;
    }

    @Override
    public double calcQuantity(double area) {
        double q = getPrice();
        super.setQuantity(q);
        return q;
    }
}
