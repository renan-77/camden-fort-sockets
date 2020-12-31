package com.orenan;

import com.orenan.shipsFactory.Ship;
import com.orenan.shipsFactory.ShipFactory;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.Random;

public class Youghal implements CreateShip {
    //Creating empty list of ships.
    LinkedList<String> ships = new LinkedList<String>();
    ShipFactory shipFactory =  ShipFactory.getInstance();

    @Override
    public void createShips() throws RemoteException, InterruptedException {
        System.out.println(produceShips());
    }

    //Class to create random number based on a limit (starts from 1).
    int getRandom(int limit){
        Random rand = new Random();
        int upperBound = limit;
        return rand.nextInt(upperBound) + 1;
    }

    //Method that produces a linked list of random size (limit is 10) of random ships.
    public LinkedList<String> produceShips() throws InterruptedException{
        //Getting limit of ships that will be seen at the city.
        int capacity = getRandom(10);

        //Creating ships within capacity.
        while(ships.size() < capacity){
            synchronized (this){
                //Creating a random ship.
                Ship ship = shipFactory.getShip(getRandom(3));

                //If it reaches its limit of capacity in the list it waits until the list is cleared.
                while(ships.size() == capacity){
                    wait();
                }

                //Printing how many ships were created.
                System.out.println(capacity + " ships created.");

                //Calling draw method to see which ship was created.
                ship.draw();

                //Adding ship to the list.
                ships.add(ship.draw());
            }
        }

        return ships;
    }
}
