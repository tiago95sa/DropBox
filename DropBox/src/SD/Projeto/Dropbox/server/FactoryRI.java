/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SD.Projeto.Dropbox.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Tiago
 */
public interface FactoryRI extends Remote {
    public SessionRI login(String u, String p) throws RemoteException;
    public boolean register(String username, String password) throws RemoteException;
}
