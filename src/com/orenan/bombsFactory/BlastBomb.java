package com.orenan.bombsFactory;

//Polymorphism of class bomb.
public class BlastBomb implements Bomb {
    @Override
    public String draw() {
        String bombType = "Blast Bomb";
        System.out.println(bombType);
        return bombType;
    }
}