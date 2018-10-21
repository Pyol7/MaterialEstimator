package com.jeffreyromero.materialestimator.models.non_quantifiables;

import com.jeffreyromero.materialestimator.models.BaseMaterial;
import com.jeffreyromero.materialestimator.models.quantifiables.Panel;


public class PanelFastener extends BaseMaterial{

    public PanelFastener(String name, double unitPrice, int spacing){
        super("PanelFastener", name, unitPrice, spacing);
    }

    public double calcFastenerQuantity(double dim1, double dim2, BaseMaterial panel, BaseMaterial secondarySupport) {
        double pQ = ((Panel)panel).calcQuantity(dim1,dim2);
        double wQ = (panel.getWidth() / getSpacing()) + 1;
        double lQ = (panel.getLength() / secondarySupport.getSpacing()) + 1;
        return super.setQuantity(pQ * wQ * lQ);
    }
}