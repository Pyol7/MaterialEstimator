package com.jeffreyromero.materialestimator.models;

interface Item {
    MaterialList initMaterialList();
    void calcQuantities();
}
