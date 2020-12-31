package com.orenan.bombsFactory;

//Polymorphism of class bomb.
public class ArmorPiercing implements Bomb {
    @Override
    public String draw() {
        String bombType = "Armor Piercing";
        System.out.println(bombType);
        return bombType;
    }
}