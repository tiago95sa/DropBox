/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SD.Projeto.Dropbox.server;

import static SD.Projeto.Dropbox.server.DropboxServer.SERVER_PATH;
import java.math.BigInteger;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Tiago
 */
public class FactoryImpl extends UnicastRemoteObject implements FactoryRI{
    
    DB db = new DB();
    
    // Uses RMI-default sockets-based transport
    // Runs forever (do not passivates) - Do not needs rmid (activation deamon)
    // Constructor must throw RemoteException due to export()
    public FactoryImpl() throws RemoteException {
        // Invokes UnicastRemoteObject constructor which exports remote object
        super();
        db.loadBD();
        db.partilhasDebug();
    }
    
    
    
    /**
     * Faz login e retorna uma session. A password é encriptada
     * @param u
     * @param p
     * @return
     * @throws RemoteException 
     */
    @Override
    public SessionRI login(String u, String p) throws RemoteException {
        
        System.out.println("Login(FactoryImpl) username : " + u);
        System.out.println("Login(FactoryImpl) password : " + u);
        
        
        
        if (this.db.exists(u)) {
            String passwordEncriptada = getMd5(p);
            if(this.db.login(u, passwordEncriptada)){
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "entra");
            
                User n = getDb().getUser(u);
           
                SessionRI s = new SessionImpl(n,this);
            
                this.db.getUserSession().put(n, s);
            
                return s;
            }else {
                throw new RemoteException("User ou password errada");
            }
        } else {
            throw new RemoteException("User ou password não exite");
        }
    }
    
    /**
     * Regista um novo utilizador , com a password encriptada
     * @param username
     * @param password
     * @return
     * @throws RemoteException 
     */
    @Override
    public boolean register(String username, String password) throws RemoteException {
        String passwordEncriptada = getMd5(password);
        
       if (!this.db.exists(username)){
           this.getDb().register(username, passwordEncriptada);
           return true;
       }
       return false;
    }

    /**
     * @return the db
     */
    public DB getDb() {
        return db;
    }
 
    
    public static String getMd5(String input) {
        try {
            // Static getInstance method is called with hashing MD5 
            MessageDigest md = MessageDigest.getInstance("MD5");
            // digest() method is called to calculate message digest 
            // of an input digest() return array of byte 
            byte[] messageDigest = md.digest(input.getBytes());
            // Convert byte array into signum representation 
            BigInteger no = new BigInteger(1, messageDigest);
            // Convert message digest into hex value 
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } // For specifying wrong message digest algorithms 
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    
    
}
