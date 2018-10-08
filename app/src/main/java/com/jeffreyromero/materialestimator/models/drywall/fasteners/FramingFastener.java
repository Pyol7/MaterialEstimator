package com.jeffreyromero.materialestimator.models.drywall.fasteners;


import com.jeffreyromero.materialestimator.models.BaseMaterial;
import com.jeffreyromero.materialestimator.models.Material;
import com.jeffreyromero.materialestimator.models.drywall.materials.FurringChannel;
import com.jeffreyromero.materialestimator.models.drywall.materials.Stud;

public class FramingFastener extends BaseMaterial {

    public FramingFastener(String name, double price) {
        super("Framing Fastener", name, price);
    }

    public double calcFastenerQuantity(double dim1, double dim2, BaseMaterial panelFraming) {
        double length = Math.max(dim1, dim2);
        double width = Math.min(dim1, dim2);
        double q;

        double joints = Math.floor(width / panelFraming.getLength());
        double pieces = (length / panelFraming.getSpacing()) - 1;

        if (panelFraming instanceof Stud){
            q = ((joints*4) + 4) * pieces;
        } else {
            q = ((joints*4) + 2) * pieces;
        }
        return super.setQuantity(q);
    }

}
