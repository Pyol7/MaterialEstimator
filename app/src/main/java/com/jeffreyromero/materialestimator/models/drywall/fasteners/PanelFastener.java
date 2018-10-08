package com.jeffreyromero.materialestimator.models.drywall.fasteners;

import com.jeffreyromero.materialestimator.models.BaseMaterial;
import com.jeffreyromero.materialestimator.models.Fastener;
import com.jeffreyromero.materialestimator.models.Material;


public class PanelFastener extends Fastener {

    public PanelFastener(String name, double unitPrice){
        super("Panel Fastener", name, unitPrice);
    }

    public double calcFastenerQuantity(double dim1, double dim2, BaseMaterial panel, BaseMaterial panelFraming) {
        double pQ = ((Material)panel).calcQuantity(dim1,dim2);
        double wQ = (panel.getWidth() / panel.getSpacing()) + 1;
        double lQ = (panel.getLength() / panelFraming.getSpacing()) + 1;
        return super.setQuantity(pQ * wQ * lQ);
    }
}