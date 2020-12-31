package com.orenan.bombsFactory;

//Polymorphism of class bomb.
public class Torpedo implements Bomb {
    @Override
    public void draw() {
        System.out.println("Torpedo");
    }
}