package com.orenan.shipsFactory;

//Polymorphism of class ship.
public class AircraftCarrier implements Ship {
    @Override
    public String draw() {
            String shipType = "Aircraft Carrier";
            System.out.println("An Aircraft Carrier has been seen");
            return shipType;
    }
}