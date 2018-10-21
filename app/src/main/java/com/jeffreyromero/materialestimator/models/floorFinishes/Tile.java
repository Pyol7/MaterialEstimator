package com.jeffreyromero.materialestimator.models.floorFinishes;

public class Tile extends FloorFinish{

    public Tile(String type, String name, double price, double length, double width) {
        super("Tile", "Porcelin", "Coral345", 18.50, 16,16);
    }

    @Override
    public double calcQuantity(double length, double width) {
        double area = length * width;
        double tileArea = getLength() * getWidth();
        return super.setQuantity(area/tileArea);
    }
}
