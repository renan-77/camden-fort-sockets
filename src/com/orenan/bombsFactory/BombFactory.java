package com.orenan.bombsFactory;

public class BombFactory extends Thread{

    private static BombFactory instance = new BombFactory();

    private BombFactory(){};

    public static BombFactory getInstance(){
        return instance;
    }

    public Bomb getBomb(String bombType){
        if(bombType == "Destroyer"){
            return new ArmorPiercing();

        } else if(bombType == "Aircraft Carrier"){
            return new Torpedo();

        } else if(bombType == "Sailing ship"){
            return new BlastBomb();
        }

        return null;
    }
}