package com.jeffreyromero.materialestimator.models.droppedCeiling;

import com.jeffreyromero.materialestimator.models.Material;

public class CeilingTile extends Material {

    public CeilingTile(String name, double unitPrice, double length, double width) {
        super("Ceiling Tile", name, unitPrice, length, width);
    }

    @Override
    public double calcQuantity(double dim1, double dim2) {
        double q = (dim1*dim2)/(getLength()* getWidth());
        return super.setQuantity(q);
    }

    @Override
    public double calcQuantity(double area) {
        double q = area/(getLength()*getWidth());
        return super.setQuantity(q);
    }
}
