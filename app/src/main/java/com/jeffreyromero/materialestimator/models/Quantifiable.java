package com.jeffreyromero.materialestimator.models;

public interface Quantifiable {
    double calcQuantity(double area);
    double calcQuantity(double length, double width);
}
