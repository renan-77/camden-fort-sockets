package com.orenan.shipsFactory;

//Polymorphism of class ship.
public class Destroyer implements Ship {
    @Override
    public String draw() {
        String shipType = "Destroyer";
        System.out.println("A Destroyer has been seen");
        return shipType;
    }
}