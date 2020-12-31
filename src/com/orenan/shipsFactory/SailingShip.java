package com.orenan.shipsFactory;

//Polymorphism of class ship.
public class SailingShip implements Ship {
    @Override
    public String draw() {
        String shipType = "Sailing ship";
        System.out.println("A Sailing ship has been seen");
        return shipType;
    }
}