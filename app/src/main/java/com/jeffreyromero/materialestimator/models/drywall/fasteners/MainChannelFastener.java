package com.jeffreyromero.materialestimator.models.drywall.fasteners;

import com.jeffreyromero.materialestimator.models.BaseMaterial;
import com.jeffreyromero.materialestimator.models.Fastener;
import com.jeffreyromero.materialestimator.models.Material;

public class MainChannelFastener extends Fastener {

    public MainChannelFastener(String name, double unitPrice){
        super("Main Channel Fastener", name, unitPrice);
    }

    public double calcFastenerQuantity(double dim1, double dim2, BaseMaterial mainChannel, BaseMaterial furringChannel) {
        double cQ = ((Material)mainChannel).calcQuantity(dim1,dim2);
        double q = ((cQ * mainChannel.getLength() / furringChannel.getSpacing()) + 1) * 2;
        return super.setQuantity(q);
    }
}