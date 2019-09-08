/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SD.Projeto.Dropbox.server;

import SD.Projeto.Dropbox.client.ObserverRI;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 *
 * @author Tiago
 */
public interface SessionRI extends Remote{
  
    public User getUser()throws RemoteException;
    public void setUser(User user)throws RemoteException;
    public boolean partilhaPasta(String username) throws RemoteException;
    public ArrayList getSubjects() throws RemoteException;
    public SubjectRI getSubjectUser(String username)throws RemoteException;
    public void partilhaPastaOffline(String username) throws RemoteException ;
}
