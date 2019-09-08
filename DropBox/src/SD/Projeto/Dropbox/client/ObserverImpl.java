/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SD.Projeto.Dropbox.client;

import SD.Projeto.Dropbox.server.SingletonFolderOperationsUser;
import SD.Projeto.Dropbox.server.SubjectRI;
import SD.Projeto.Dropbox.server.VisitorI;
import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;


/**
 *
 * @author Tiago
 */
public class ObserverImpl implements ObserverRI {
    
    private File file;
    private String username;
    public SubjectRI subjectRI;
    private SingletonFolderOperationsUser singletonFolder=null;
    private HashMap<File, Long> ficheiros = null;
    


    public ObserverImpl(String username, SubjectRI subjectRI , File f) throws RemoteException {
     
       this.file = f;
       this.username = username;
       this.subjectRI = subjectRI;
       this.singletonFolder = new SingletonFolderOperationsUser(f.getAbsolutePath());
       this.ficheiros = new HashMap();
       preencherHash();
       System.out.println("Observer " + username+ " :Path = " + f.getAbsolutePath());
        export();
    }

    public void preencherHash() throws RemoteException{
        this.getFicheiros().clear();
        for (File fileCurrent : file.listFiles()) {
            getFicheiros().put(fileCurrent, (Long)fileCurrent.lastModified());
        }
    }
    
    public void export() throws RemoteException {
        UnicastRemoteObject.exportObject(this, 0);
    }
    
    
    public SingletonFolderOperationsUser getSingletonFolder(){
        return this.singletonFolder;
    }
      
    /**
     * executa a operacao 
     * @param v
     * @throws RemoteException 
     */
     @Override
    public void update(VisitorI v) throws RemoteException {
        Object r = v.visit(this);
        preencherHash();
    }

    @Override
    public Object acceptVisitor(VisitorI v) throws RemoteException {
        return v.visit(this);
    }

    /**
     * @return the file
     * @throws java.rmi.RemoteException
     */
    @Override
    public File getFile() throws RemoteException{
        return file;
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
     * @return the ficheiros
     */
    @Override
    public HashMap<File, Long> getFicheiros() throws RemoteException{
        return ficheiros;
    }
}
