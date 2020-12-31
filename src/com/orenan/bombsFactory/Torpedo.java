package com.orenan.bombsFactory;

//Polymorphism of class bomb.
public class Torpedo implements Bomb {
    @Override
    public String draw() {
        String bombType = "Torpedo";
        System.out.println(bombType);
        return bombType;
    }
}