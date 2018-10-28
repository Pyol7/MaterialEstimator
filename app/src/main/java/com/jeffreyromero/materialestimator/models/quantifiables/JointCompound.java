package com.jeffreyromero.materialestimator.models.quantifiables;

import com.jeffreyromero.materialestimator.models.BaseMaterial;
import com.jeffreyromero.materialestimator.models.Quantifiable;

import static java.lang.Math.sqrt;

public class JointCompound extends BaseMaterial implements Quantifiable {

    private int coverage;

    public JointCompound(String name, double unitPrice, int coverage) {
        super("JointCompound", name, unitPrice);
        this.coverage = coverage;
    }

    @Override
    public double calcQuantity(double area) {
        double length = sqrt(area);
        return this.calcQuantity(length, length);
    }

    @Override
    public double calcQuantity(double length, double width) {
        double area = length * width;
        return super.setQuantity(area / coverage * getCoefficient());
    }

    public int getCoverage() {
        return coverage;
    }

    public void setCoverage(int coverage) {
        this.coverage = coverage;
    }
}
