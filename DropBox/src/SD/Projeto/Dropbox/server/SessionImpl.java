/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SD.Projeto.Dropbox.server;

import SD.Projeto.Dropbox.client.ObserverImpl;
import SD.Projeto.Dropbox.client.ObserverRI;
import static SD.Projeto.Dropbox.server.DropboxServer.CLIENTE_PATH;
import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 *
 * @author Tiago
 */
public class SessionImpl implements SessionRI {

    private ArrayList<SubjectRI> subjectRI;
    private User user;
    private FactoryImpl factoryImpl;

    public SessionImpl(User user, FactoryImpl factoryImpl) throws RemoteException {
        this.user = user;
        this.factoryImpl = factoryImpl;
        this.subjectRI = this.getSubjectsFromDb();
        export();
    }

    public void export() throws RemoteException {
        UnicastRemoteObject.exportObject(this, 0);
    }

    @Override
    public ArrayList getSubjects() {
        return this.subjectRI;
    }

    /**
     * Retorna um subject de um determinado utilizador
     * @param username
     * @return
     * @throws RemoteException 
     */
    @Override
    public SubjectRI getSubjectUser(String username) throws RemoteException {
        for (SubjectRI s : subjectRI) {
            if (s.getUsername().equals(username)) {

                return s;
            }
        }
        return null;
    }

    /**
     * Retorna um ArrayList com todos os subjects do utilizador
     * @return
     * @throws RemoteException 
     */
    public ArrayList getSubjectsFromDb() throws RemoteException {
        return factoryImpl.getDb().getSubjectsPartilhas(this.user);
    }

    /**
     * @return the user
     */
    @Override
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    @Override
    public void setUser(User user) {
        this.user = user;
    }

    
    /**
     * Partilha a pasta do utilizador com outro utilizador , ambos online
     * @param username
     * @return
     * @throws RemoteException 
     */
    @Override
    public boolean partilhaPasta(String username) throws RemoteException {

        SessionRI session = factoryImpl.db.getSession(username);
        if (session != null) {
            if (this.factoryImpl.db.adicionarPartilha(username, this.user)) {
                
                this.factoryImpl.db.getSubject(this.user.getUsername());
                SubjectRI sub = this.factoryImpl.db.getSubject(this.getUser().getUsername());
                SubjectRI sub_2 = this.factoryImpl.db.getSubject(username);
                ObserverRI ob = sub_2.getObserver(username);
                
                ob.update(new CreateFolderVisitor(this.getUser().getUsername()));
                sub.attach(ob);
                return true;
            }
            return false;
        } else {
            return false;
        }

    }

    
    /**
     * Partilha a pasta de um utilizador , com esse utilizador offline
     * @param username
     * @throws RemoteException 
     */
    @Override
    public void partilhaPastaOffline(String username) throws RemoteException {
        if (this.factoryImpl.db.adicionarPartilha(username, this.user)) {

            File diretorio = new File(CLIENTE_PATH + username + "\\" + this.getUser().getUsername());

            if (!diretorio.exists()) {
                diretorio.mkdir();
            } else {
                System.out.println("Já existe ficheiro");
            }
            System.out.println("Partilha efetuada");

        } else {
            System.out.println("partilha nao efetuada");
        }

    }
    
    
    /*
    public ObserverRI partilhaPasta(String username) throws RemoteException {

        SessionRI session = factoryImpl.db.getSession(username);
        if (session != null) {
            if (this.factoryImpl.db.adicionarPartilha(username, this.user)) {
                
                this.factoryImpl.db.getSubject(this.user.getUsername());
                SubjectRI sub = this.factoryImpl.db.getSubject(this.getUser().getUsername());
                File diretorio = new File(CLIENTE_PATH + username + "\\" + this.getUser().getUsername());
                
                ObserverRI ob = new ObserverImpl(username, sub, diretorio);
                
                
                
                if (!diretorio.exists()) {
                    diretorio.mkdir();
                    sub.attach(ob);
                    return ob;
                } else {
                    System.out.println("Já existe ficheiro");
                    return null;
                }
            }
            return null;
        } else {
            System.out.println("partilha nao efetuada");
            return null;
        }

    }
    */

}
