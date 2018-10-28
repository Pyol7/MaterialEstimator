package com.jeffreyromero.materialestimator.models;

interface Item {
    MaterialList initMaterialList();
    void calcQuantities(double x, double y);
    String getXHintText();
    String getYHintText();
}
