package com.jeffreyromero.materialestimator.models.drywall;

import com.jeffreyromero.materialestimator.models.Material;

public class JointCompoundSettingType extends Material {

    public JointCompoundSettingType(String name, double price) {
        super("JointCompoundSettingType", name, price);
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
