package com.orenan;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CreateShip extends Remote {
    void createShips() throws RemoteException, InterruptedException;
}
