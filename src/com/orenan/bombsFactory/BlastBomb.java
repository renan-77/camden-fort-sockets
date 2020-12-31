package com.orenan.bombsFactory;

//Polymorphism of class bomb.
public class BlastBomb implements Bomb {
    @Override
    public void draw() {
        System.out.println("Blast Bomb");
    }
}