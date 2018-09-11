package com.jeffreyromero.materialestimator.models;

public interface Quantifiable {
    double calcQuantity(double length, double width);
    double calcQuantity(double area);
}
