package com.jeffreyromero.materialestimator.models.droppedCeiling;

import com.jeffreyromero.materialestimator.models.Material;

public class CeilingTile extends Material {

    public CeilingTile(String name, int price, double length, double width) {
        super("CeilingTile", name, price, length, width);
    }

    @Override
    public double calcQuantity(double roomLength, double roomWidth) {
        double area = roomLength*roomWidth;
        double q = calcQuantity(area);
        super.setQuantity(q);
        return q;
    }

    @Override
    public double calcQuantity(double area) {
        double tileArea = getLength()*getWidth();
        double q = area/tileArea;
        super.setQuantity(q);
        return q;
    }
}
