package com.jeffreyromero.materialestimator.models;

interface IProjectItem {
    MaterialList initMaterialList();
    void calcQuantities(double x, double y);
    String getXHintText();
    String getYHintText();
}
