package com.orenan;
// Java implementation of  Server side
// It contains two classes : Server and ClientHandler
// Save file as Server.java

import com.orenan.bombsFactory.Bomb;
import com.orenan.bombsFactory.BombFactory;
import com.orenan.shipsFactory.Ship;

import java.io.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.*;
import java.util.*;
import java.net.*;

// Server class
public class Blarney extends Youghal {
    public static void main(String[] args) throws IOException {

//        Youghal youghal = new Youghal();
//
//        CreateShip Stub = (CreateShip) UnicastRemoteObject.exportObject(youghal, 0);
//
//        Registry registry = LocateRegistry.getRegistry();

        System.out.println("Server is now running (Blarney)");

        // server is listening on port 5056
        ServerSocket ss = new ServerSocket(5056);

        // running infinite loop for getting
        // client request
        while (true) {
            Socket s = null;

            try {
                // socket object to receive incoming client requests
                s = ss.accept();

                System.out.println("A new client is connected : " + s);

                // obtaining input and out streams
                DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                System.out.println("Assigning new thread for this client");

                // create a new thread object
                Thread t = new ClientHandler(s, dis, dos);

                // Invoking the start() method
                t.start();

            }
            catch (Exception e){
                s.close();
                e.printStackTrace();
            }
        }
    }
}

// ClientHandler class
class ClientHandler extends Thread {
    DateFormat fordate = new SimpleDateFormat("yyyy/MM/dd");
    DateFormat fortime = new SimpleDateFormat("hh:mm:ss");
    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket s;

    //Creating FileOutPut Stream to send object to server.
    FileOutputStream fos = new FileOutputStream("bombsList.txt", false);

    //Creating ObjectOutputStream to send LinkedList as a serialized object to server.
    ObjectOutputStream outBombsList = new ObjectOutputStream(fos);

    //Creating shipsList.
    LinkedList<String> shipsList = new LinkedList<String>();

    //Creating bombsList.
    LinkedList<String> bombsList = new LinkedList<String>();

    //Getting instance of the BombFactory.
    BombFactory bombFactory = BombFactory.getInstance();

    // Constructor
    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos) throws IOException {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
    }

    @Override
    public void run() {
        String received;
        String toreturn;
        while (true)
        {
            try {
                //Creating deserialization objects.
                FileInputStream fis = new FileInputStream("shipsList.txt");
                ObjectInputStream ois = new ObjectInputStream(fis);

                //Creating shipList based on input from client
                shipsList = (LinkedList<String>) ois.readObject();
                ois.close();
                fis.close();

                File f = new File("shipsList.txt");

                f.delete();

                System.out.println(shipsList);

                //Declaring bombs based on ship.
                for(int i = 0; i < shipsList.size(); i++){
                    String currentShip = shipsList.get(i);

                    System.out.println(currentShip);

                    Bomb currentBomb = bombFactory.getBomb(currentShip);

                    bombsList.add(currentBomb.draw());
                }

                System.out.println(bombsList);

                //Sending bombs to client
                if(bombsList.size() == shipsList.size()){
                    outBombsList.writeObject(bombsList);
                    //Closing connections.
                    outBombsList.flush();
                    outBombsList.close();
                    fos.close();
                }

                // receive the answer from client
                received = dis.readUTF();

                if(received.equals("destroy")){
                    f = new File("bombsList.txt");
                    f.delete();
                    System.out.println("Client " + this.s + " sends destroy...");
                    System.out.println("Closing this connection.");
                    this.s.close();
                    System.out.println("Connection closed");
                    break;
                }

                if(received.equals("Exit")) {
                    System.out.println("Client " + this.s + " sends exit...");
                    System.out.println("Closing this connection.");
                    this.s.close();
                    System.out.println("Connection closed");
                    break;
                }

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
