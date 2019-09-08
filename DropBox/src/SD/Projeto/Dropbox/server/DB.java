/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SD.Projeto.Dropbox.server;

import java.io.File;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


// falta mudar os caminhos para a macro

/**
 *
 * @author Tiago
 */
public class DB {
    public static final String BASE_PATH_SERVERS = "C:\\Users\\Tiago\\Documents\\NetBeansProjects\\DropBox\\data\\servers\\";
    private final HashMap<User, File> userFile;
    private final HashMap<User, SubjectRI> userSubject;
    private final HashMap<SubjectRI, ArrayList<User>> partilhas;
    private final HashMap<User , SessionRI> userSession;
    
   
    
            
    public DB() throws RemoteException{
        this.userFile = new HashMap();
        this.userSubject = new HashMap();
        this.partilhas = new HashMap();
        this.userSession = new HashMap();
    }
    
    
   public void loadBD() throws RemoteException{
       
       
       
        User n1 = new User("Tiago", getMd5("tiago"));
        User n2 = new User("Diogo", getMd5("diogo"));
        User n3 = new User("Ufp",   getMd5("ufp"));
        
        
        String path1 =BASE_PATH_SERVERS + "DropBoxTiago";
        String path2 = BASE_PATH_SERVERS + "DropBoxDiogo";
        String path3 = BASE_PATH_SERVERS + "DropBoxUfp";
        
        
        File nova1 = new File(path1);
        nova1.mkdir();
        File nova2 = new File(path2);
        nova2.mkdir();
        File nova3 = new File(path3);
        nova3.mkdir();
        
          
        File novaPasta = new File(path1+"\\Tiago");
        novaPasta.mkdir();
        File novaPasta2 = new File(path2+"\\Diogo");
        novaPasta2.mkdir();
        File novaPasta3 = new File(path3+"\\Ufp");
        novaPasta3.mkdir();
        
        SubjectRI s1 = new SubjectImpl(path1+"\\Tiago" , n1.getUsername() ,novaPasta);
        SubjectRI s2 = new SubjectImpl(path2+"\\Diogo", n2.getUsername() , novaPasta2);
        SubjectRI s3 = new SubjectImpl(path3+"\\Ufp", n3.getUsername(), novaPasta3);
        
        userFile.put(n1, novaPasta);
        userFile.put(n2, novaPasta2);
        userFile.put(n3, novaPasta3);
        
        userSubject.put(n1, s1);
        userSubject.put(n2, s2);
        userSubject.put(n3, s3);
        
        
        ArrayList<User> a1 = new ArrayList();
        ArrayList<User> a2 = new ArrayList();
        ArrayList<User> a3 = new ArrayList();
        
        a1.add(n1);
        a1.add(n2);
        a2.add(n2);
        a3.add(n3);
        
        partilhas.put(s1, a1);
        partilhas.put(s2, a2);
        partilhas.put(s3, a3);
        
        /*
        userPartilhas.put(s1,a1);
        userPartilhas.put(s2,a2);
        userPartilhas.put(s3,a3);
        
        /*
        File novaPasta4 = new File("C:\\Users\\Tiago\\Documents\\NetBeansProjects\\DropBox\\data\\clients\\tiago");
        novaPasta4.mkdir();
        File novaPasta5 = new File("C:\\Users\\Tiago\\Documents\\NetBeansProjects\\DropBox\\data\\clients\\diogo");
        novaPasta5.mkdir();
        File novaPasta6 = new File("C:\\Users\\Tiago\\Documents\\NetBeansProjects\\DropBox\\data\\clients\\ufp");
        novaPasta6.mkdir();*/
        
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
   
   public SessionRI getSession(String username){
       
       for (Map.Entry<User, SessionRI> user : userSession.entrySet()) {
           if(user.getKey().getUsername().compareTo(username) == 0){
               return user.getValue();
           }
        }
       return null;
   }
   
   public boolean login(String u, String p) {
        for (Map.Entry<User, File> user : userFile.entrySet()) {
            if (user.getKey().getUsername().compareTo(u) == 0 && user.getKey().getPassword().compareTo(p) == 0) {
                return true;
            }
        }
        return false;
    }
   
   public boolean exists(String u) {
        for (Map.Entry<User, File> user : userFile.entrySet()) {
            if (user.getKey().getUsername().compareTo(u) == 0) {
                return true;
            }
        }
        return false;
    }
   
   public void register(String u , String p) throws RemoteException{
       User n1 = new User(u, p);
       String path = BASE_PATH_SERVERS + "DropBox" + u;
       
       File novaPasta = new File(path);
       novaPasta.mkdir();
       
       path = path + "//" + u;
       File novaPasta1 = new File(path);
       novaPasta1.mkdir();
       
       SubjectRI s1 = new SubjectImpl(path,u, novaPasta1);
            
       userFile.put(n1, novaPasta);
       userSubject.put(n1, s1);
       
       ArrayList<User> a1 = new ArrayList();
       a1.add(n1);
       partilhas.put(s1, a1);
      
   }
   
   
   public User getUser(String username){
       for (Map.Entry<User, File> user : userFile.entrySet()) {
            if (user.getKey().getUsername().compareTo(username) == 0) {
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "User = " + user.getKey().getUsername());
                return user.getKey();
            }
        }
        return null;
   }
    
   public SubjectRI getSubject(String username){
       User user = this.getUser(username);
       return userSubject.get(user);
   }
   
   public ArrayList getPartilhas(SubjectRI s){
       return this.partilhas.get(s);
   }
   
   public ArrayList getSubjectsPartilhas(User u) throws RemoteException{
       ArrayList<SubjectRI> ar = new ArrayList();
       
       for (Map.Entry<SubjectRI, ArrayList<User>> subUser : partilhas.entrySet()) { 
           for (User temp : subUser.getValue()) {
               if(temp.getUsername().equals(u.getUsername())){
                   SubjectRI s = subUser.getKey();
                   ar.add(s);
                   System.out.println("Pasta de " + s.getUsername() + " partilhada com " + u.getUsername());
                   //break;
               }
	   } 
       }
       
       return ar;
   }
   
   
   public File getFile(String username){
      User u = this.getUser(username);
      return this.userFile.get(u);
   }
   
   public boolean adicionarPartilha(String username , User user){
       SubjectRI s = this.getSubject(user.getUsername());
       User u = this.getUser(username);
       if(u!= null){
        ArrayList ar = this.getPartilhas(s);
        ar.add(u);
        return true;
       }
      return false;
   }
   
   /*
   public boolean existePartilha(String username , User user){
       SubjectRI s = this.getSubject(user.getUsername());
       User u = this.getUser(username);
       
       while()
   }*/
   
   public void partilhasDebug() throws RemoteException{
       for (Map.Entry<SubjectRI, ArrayList<User>> subUser : partilhas.entrySet()) { 
           for (User temp : subUser.getValue()) {
               System.out.println("Para a parta de " + subUser.getKey().getUsername()+ "e partilhada com "
               + temp.getUsername());
	   } 
       }
   }
   
   
   //por acabar
   public ArrayList listarAcessosConteudo(String username){
       User u = this.getUser(username);
       SubjectRI s = this.getSubject(username);
       File f = this.getFile(username);
       
       ArrayList<String> partilha = new ArrayList();
       
       
       
       return partilha;
   }

    /**
     * @return the userSession
     */
    public HashMap<User , SessionRI> getUserSession() {
        return userSession;
    }
   
   
   
   
   
}
