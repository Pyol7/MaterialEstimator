package com.jeffreyromero.materialestimator.models.quantifiables;

import com.jeffreyromero.materialestimator.models.BaseMaterial;
import com.jeffreyromero.materialestimator.models.Quantifiable;

import static java.lang.Math.sqrt;

/**
 * All dropped ceiling tees are derived from this class
 * Notes:
 * Main tee spacing = Secondary tee length,
 * Secondary tee spacing = Tertiary tee length,
 * Tertiary tee spacing = Secondary tee length.
 */
public class Tee extends BaseMaterial implements Quantifiable {

    private static final int MAIN_TEE_MIN_LENGTH = 120;
    private static final int MAIN_TEE_MAX_LENGTH = 144;
    private static final int SECONDARY_TEE_LENGTH = 48;
    private static final int TERTIARY_TEE_LENGTH = 24;


    public Tee(String name, double unitPrice, double length) {
        super("Tee", name, unitPrice, length);
    }

    @Override
    public double calcQuantity(double area) {
        double length = sqrt(area);
        return this.calcQuantity(length, length);
    }

    @Override
    public double calcQuantity(double length, double width) {
        // Determine type of tee
        if (getLength() >= MAIN_TEE_MIN_LENGTH && getLength() <= MAIN_TEE_MAX_LENGTH){
            // Calculate quantity for main tee
            double q = (length / SECONDARY_TEE_LENGTH) * (width / getLength());
            return super.setQuantity(q);
        } else if (getLength() == SECONDARY_TEE_LENGTH){
            // Calculate quantity for Secondary Tee
            double q = (length / getLength()) * (width / TERTIARY_TEE_LENGTH);
            return  super.setQuantity(q);
        } else if (getLength() == TERTIARY_TEE_LENGTH){
            // Calculate quantity for Tertiary Tee
            double q = (width / getLength()) * (length / SECONDARY_TEE_LENGTH);
            return super.setQuantity(q);
        } else {
            throw new IllegalArgumentException("Unrecognized Tee length");
        }
    }
}
