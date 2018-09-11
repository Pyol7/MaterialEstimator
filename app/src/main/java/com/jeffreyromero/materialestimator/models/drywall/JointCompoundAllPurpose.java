package com.jeffreyromero.materialestimator.models.drywall;

import com.jeffreyromero.materialestimator.models.Material;

public class JointCompoundAllPurpose extends Material {

    public JointCompoundAllPurpose(String name, int price) {
        super("JointCompoundAllPurpose", name, price);
    }

    @Override
    public double calcQuantity(double length, double width) {
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
