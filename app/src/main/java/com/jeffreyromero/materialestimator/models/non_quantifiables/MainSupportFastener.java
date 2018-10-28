package com.jeffreyromero.materialestimator.models.non_quantifiables;

import com.jeffreyromero.materialestimator.models.BaseMaterial;
import com.jeffreyromero.materialestimator.models.quantifiables.MainSupport;

public class MainSupportFastener extends BaseMaterial {


    public MainSupportFastener(String name, double unitPrice){
        super("MainSupportFastener", name, unitPrice);
    }

    public double calcFastenerQuantity(double dim1, double dim2, BaseMaterial mainSupport, BaseMaterial secondarySupport) {
        double cQ = ((MainSupport)mainSupport).calcQuantity(dim1,dim2);
        double q = ((cQ * mainSupport.getLength() / secondarySupport.getSpacing()) + 1) * 2;
        return super.setQuantity(q * getCoefficient());
    }
}