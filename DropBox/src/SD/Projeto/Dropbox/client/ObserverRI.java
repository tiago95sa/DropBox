/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SD.Projeto.Dropbox.client;

import SD.Projeto.Dropbox.server.SubjectObserverRI;
import SD.Projeto.Dropbox.server.SubjectRI;
import SD.Projeto.Dropbox.server.VisitorI;
import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

/**
 *
 * @author Tiago
 */
public interface ObserverRI extends SubjectObserverRI, Remote {
   public void update(VisitorI v) throws RemoteException ;
   public File getFile()throws RemoteException;
   public String getUsername()throws RemoteException;
   public HashMap<File, Long> getFicheiros() throws RemoteException;
}
