/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SD.Projeto.Dropbox.server;

import SD.Projeto.Dropbox.client.ObserverRI;
import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;


/**
 *
 * @author Tiago
 */
public interface SubjectRI extends SubjectObserverRI, Remote {
    public void attach(ObserverRI o) throws RemoteException;
    public void dettach(ObserverRI o) throws RemoteException;
    public String getUsername()throws RemoteException;
    public File getFile()throws RemoteException;
    public ObserverRI getObserver(String username) throws RemoteException;
}
