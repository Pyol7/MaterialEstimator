package com.jeffreyromero.materialestimator.models.droppedCeiling;

import com.jeffreyromero.materialestimator.models.Material;

public class MainTee extends Material {

    public MainTee(String name, int price, double length) {
        super("MainTee", name, price, length);
    }

    @Override
    public double calcQuantity(double roomLength, double roomWidth) {
        double q = getLength();
        super.setQuantity(q);
        return q;
    }

    @Override
    public double calcQuantity(double area) {
        double q = getLength();
        super.setQuantity(q);
        return q;
    }
}
