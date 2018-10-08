package com.jeffreyromero.materialestimator.models.drywall.materials;

import com.jeffreyromero.materialestimator.models.Material;

import static java.lang.Math.sqrt;

public class Hanger extends Material {

    public Hanger(String name, double price, int spacing) {
        super("Hanger", name, price, spacing);
    }

    @Override
    public double calcQuantity(double dim1, double dim2) {
        double length = Math.max(dim1, dim2);
        double width = Math.min(dim1, dim2);
        double q = Math.ceil((length / getSpacing() - 1 ) *
                (width / getSpacing()-1));
        return super.setQuantity((q%2 != 0) ? q+1 : q);
    }

    @Override
    public double calcQuantity(double area) {
        double length = sqrt(area);
        double q = this.calcQuantity(length, length);
        return super.setQuantity((q%2 != 0) ? q+1 : q);
    }
}
