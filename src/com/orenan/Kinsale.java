package com.orenan;
// Java implementation for a client
// Save file as Client.java

import com.orenan.bombsFactory.Bomb;
import com.orenan.shipsFactory.*;

import java.io.*;
import java.net.*;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

// Client class kinsale
public class Kinsale {

    //Creating empty list of ships.
    LinkedList<Ship> ships = new LinkedList<Ship>();

    ShipFactory shipFactory =  ShipFactory.getInstance();

    //Creating list from produceShips method.
    LinkedList<Ship> shipsList = produceShips();

    //Creating bombsList.
    LinkedList<Bomb> bombsList = new LinkedList<Bomb>();

    public Kinsale() throws InterruptedException {
    }

    //Class to create random number based on a limit (starts from 1).
    int getRandom(int limit){
        Random rand = new Random();
        int upperBound = limit;
        return rand.nextInt(upperBound) + 1;
    }

    //Method that produces a linked list of random size (limit is 10) of random ships.
    public LinkedList<Ship> produceShips() throws InterruptedException{
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
                ships.add(ship);
            }
        }

        return ships;
    }

    public void sendShips() throws IOException, InterruptedException{
        //Creating FileOutPut Stream to send object to server.
        FileOutputStream fos = new FileOutputStream("shipsList");

        //Creating ObjectOutputStream to send LinkedList as a serialized object to server.
        ObjectOutputStream outShipsList = new ObjectOutputStream(fos);

        System.out.println("My ships: " + shipsList);

        //Sending serialized object to server.
        outShipsList.writeObject(shipsList);
        outShipsList.flush();

        //Closing connections.
        outShipsList.close();
        fos.close();
    }

    public void destroyShips() throws IOException, ClassNotFoundException, InterruptedException {
        //Creating deserialization objects.
        FileInputStream fis = new FileInputStream("bombsList");
        ObjectInputStream ois = new ObjectInputStream(fis);

        //Getting bombs sent by server.
        bombsList = (LinkedList<Bomb>) ois.readObject();
        ois.close();
        fis.close();

        System.out.println("My bombs: " + bombsList);

        //Assigning ship with bomb and telling it was destroyed.
        //Handling if there is only 1 ship.
        if(shipsList.size() <= 1){
            System.out.println("Ship " + shipsList.get(0) + "was destroyed by " + bombsList.get(0));
        }else{
            for(int i = 0; i < (shipsList.size() - 1); i++){
                System.out.println(i);

                System.out.println("Ship " + shipsList.get(i) + "was destroyed by " + bombsList.get(i));
            }
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        try {
            Scanner scn = new Scanner(System.in);

            // getting localhost ip
            InetAddress ip = InetAddress.getByName("localhost");

            // establish the connection with server port 5056
            Socket s = new Socket(ip, 5056);

            //Creating new instance of the class.
            Kinsale kinsale =  new Kinsale();

            // obtaining input and out streams
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());

            //Creating ships from class.
            kinsale.sendShips();

            //Receiving bombs from server and destroying ships.
//            kinsale.destroyShips();

            // the following loop performs the exchange of
            // information between client and client handler
            while (true) {
                System.out.println("Enter create to create ships");
                String tosend = scn.nextLine();
                dos.writeUTF(tosend);


                if(tosend.equals("create")){

                }

                // If client sends exit,close this connection
                // and then break from the while loop
                if(tosend.equals("Exit")) {
                    System.out.println("Closing this connection : " + s);
                    s.close();
                    System.out.println("Connection closed");
                    break;
                }
            }

            // closing resources
            scn.close();
            dis.close();
            dos.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
