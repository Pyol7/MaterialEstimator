package com.jeffreyromero.materialestimator.models.drywall.materials;

import com.jeffreyromero.materialestimator.models.Material;

import static java.lang.Math.sqrt;

public class JointCompound extends Material {

    private double coverage;

    public JointCompound(String name, double unitPrice, double coverage) {
        super("Joint Compound", name, unitPrice);
        this.coverage = coverage;
    }

    @Override
    public double calcQuantity(double dim1, double dim2) {
        double area = dim1*dim2;
        double q = area / coverage;
        return super.setQuantity(q);
    }

    @Override
    public double calcQuantity(double area) {
        double length = sqrt(area);
        double q = this.calcQuantity(length, length);
        return super.setQuantity(q);
    }

    public double getCoverage() {
        return coverage;
    }

    public void setCoverage(int coverage) {
        this.coverage = coverage;
    }
}
