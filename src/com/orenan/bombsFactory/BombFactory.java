package com.orenan.bombsFactory;

public class BombFactory extends Thread{

    private static BombFactory instance = new BombFactory();

    private BombFactory(){};

    public static BombFactory getInstance(){
        return instance;
    }

    public Bomb getBomb(String bombType){
        if(bombType.equals("Destroyer")){
            return new ArmorPiercing();

        } else if(bombType.equals("Aircraft Carrier")){
            return new Torpedo();

        } else if(bombType.equals("Sailing ship")){
            return new BlastBomb();
        }

        return null;
    }
}