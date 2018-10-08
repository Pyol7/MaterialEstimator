package com.jeffreyromero.materialestimator.models;

public interface Quantifiable {
    double calcQuantity(double dim1, double dim2);
    double calcQuantity(double area);
}
