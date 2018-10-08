package com.jeffreyromero.materialestimator.models;

public interface QuantifiableProjectItem {
    void initMaterialList();
    void calcQuantities(double length, double width);
}
