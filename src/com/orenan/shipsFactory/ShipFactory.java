package com.orenan.shipsFactory;

public class ShipFactory extends Thread{

    private static ShipFactory instance = new ShipFactory();

    private ShipFactory(){};

    public static ShipFactory getInstance(){
        return instance;
    }

    public Ship getShip(int shapeType){
        if(shapeType == 1){
            return new AircraftCarrier();

        } else if(shapeType == 2){
            return new Destroyer();

        } else if(shapeType == 3){
            return new SailingShip();
        }

        return null;
    }
}