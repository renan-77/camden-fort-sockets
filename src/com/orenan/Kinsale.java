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
    LinkedList<String> ships = new LinkedList<String>();

    //Getting instance of ShipFactory.
    ShipFactory shipFactory =  ShipFactory.getInstance();

    //Creating list from produceShips method.
    LinkedList<String> shipsList = produceShips();

    //Creating bombsList.
    LinkedList<String> bombsList = new LinkedList<String>();

    public Kinsale() throws InterruptedException {
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

    public void sendShips() throws IOException, InterruptedException{
        //Creating FileOutPut Stream to send object to server.
        FileOutputStream fos = new FileOutputStream("shipsList.txt",false);

        //Creating ObjectOutputStream to send LinkedList as a serialized object to server.
        ObjectOutputStream outShipsList = new ObjectOutputStream(fos);

        System.out.println("My ships: " + shipsList);

        //Sending serialized object to server.
        outShipsList.writeObject(shipsList);
        outShipsList.flush();
        fos.flush();

        //Closing connections.
        outShipsList.close();
        fos.close();
    }

    public boolean destroyShips() throws IOException, ClassNotFoundException, InterruptedException {
        //Creating deserialization objects.
        FileInputStream fis = new FileInputStream("bombsList.txt");
        ObjectInputStream ois = new ObjectInputStream(fis);

        //Getting bombs sent by server.
        bombsList = (LinkedList<String>) ois.readObject();
        ois.close();
        fis.close();

        System.out.println("My bombs: " + bombsList);

        //Assigning ship with bomb and telling it was destroyed.
        //Handling if there is only 1 ship.
        if(shipsList.size() <= 1){
            System.out.println("Destroying ship number: 1");
            System.out.println("Ship " + shipsList.get(0) + " was destroyed by " + bombsList.get(0));
            shipsList.pop();
            bombsList.pop();
        }else{
            for(int i = 0; i < (shipsList.size()); i++){
                System.out.println("Destroying ship number: " + (i+2));

                System.out.println("Ship " + shipsList.get(i) + " was destroyed by " + bombsList.get(i));
                shipsList.pop();
                bombsList.pop();
            }
        }
        return true;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        try {
            String tosend = "";
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

            // the following loop performs the exchange of
            // information between client and client handler
            while (true) {
                File f = new File("bombsList.txt");

                //Creating ships from class.
                kinsale.sendShips();

                if(f.exists()){
                    //Receiving bombs from server and destroying ships.
                    boolean areShipsDestroyed = kinsale.destroyShips();
                    System.out.println("Deleting File");
                    f.delete();
                    if(areShipsDestroyed){
                        dos.writeUTF(tosend);
                        break;
                    }
                }

                System.out.println("Bombs received by Blarney \n" +
                        "Enter destroy to destroy ships");
                 tosend = scn.nextLine();


                if(tosend.equals("destroy")){

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
