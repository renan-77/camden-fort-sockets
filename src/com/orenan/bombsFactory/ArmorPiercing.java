package com.orenan.bombsFactory;

//Polymorphism of class bomb.
public class ArmorPiercing implements Bomb {
    @Override
    public void draw() {
        System.out.println("Armor Piercing");
    }
}