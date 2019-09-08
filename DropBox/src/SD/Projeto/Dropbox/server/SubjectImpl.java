/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SD.Projeto.Dropbox.server;

import SD.Projeto.Dropbox.client.ObserverRI;
import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tiago
 */
public class SubjectImpl implements SubjectRI {
    
    private File f;
    private String username;
    public ArrayList<ObserverRI> observers = new ArrayList<>();
    private SingletonFolderOperationsUser singletonFolder = null;
   
    
    public SubjectImpl(String path, String username, File f ) throws RemoteException {
        
        this.f = f;
        this.username = username;
        this.singletonFolder = new SingletonFolderOperationsUser(path);
        export();

    }

    public void export() throws RemoteException {
        UnicastRemoteObject.exportObject(this, 0);
    }

    public SingletonFolderOperationsUser getSingletonFolder(){
        return this.singletonFolder;
    }
    
    /**
     * Faz o attach de um observer
     * @param o
     * @throws RemoteException 
     */
    @Override
    public void attach(ObserverRI o) throws RemoteException {
        
        this.observers.add(o);
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "utilizador adicionado");
        (new Thread(new OfflineUpdate(this, o))).start();
    }

    
    /**
     * Faz o dettach de um observer
     * @param o
     * @throws RemoteException 
     */
    @Override
    public void dettach(ObserverRI o) throws RemoteException {
        this.observers.remove(o);
    }

    /**
     * Recebe uma operação e executa
     * @param v
     * @return
     * @throws RemoteException 
     */
    @Override
    public Object acceptVisitor(VisitorI v) throws RemoteException {
        //this.state = s;
        Object o = v.visit(this);
     
//        for (int i = 0; i < path.size(); i++) {
//            o = v.criarFicheiro(path.get(i));
//            System.out.println(path.get(i));
//            if (o.equals(false)) {
//                return o;
//            }
//        }
        notifyAllObservers(v);
        return o;
    }

    /**
     * Notifica todos os observadores que estão no arraylist
     * @param v 
     */
    public void notifyAllObservers(VisitorI v) {
        for (ObserverRI ob : observers) {
            try {
                ob.update(v);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @return the username
     * @throws java.rmi.RemoteException
     */
    @Override
    public String getUsername()throws RemoteException {
        return username;
    }

    /**
     * @return the f
     * @throws java.rmi.RemoteException
     */
    @Override
    public File getFile() throws RemoteException {
        return f;
    }
    
    @Override
   public ObserverRI getObserver(String username) throws RemoteException{
       for (ObserverRI ob : this.observers) {
               if(ob.getUsername().compareTo(username)==0 ){
                   return ob;
               }
       }
       return null;
   }

    
}
