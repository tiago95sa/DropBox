/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SD.Projeto.Dropbox.client;

import SD.Projeto.Dropbox.server.CreateFileVisitor;
import SD.Projeto.Dropbox.server.CreateFolderVisitor;
import SD.Projeto.Dropbox.server.DeleteFileVisitor;
import SD.Projeto.Dropbox.server.DeleteFolderVisitor;
import SD.Projeto.Dropbox.server.FactoryRI;
import SD.Projeto.Dropbox.server.RenameFileVisitor;
import SD.Projeto.Dropbox.server.RenameFolderVisitor;
import SD.Projeto.Dropbox.server.SessionRI;
import SD.Projeto.Dropbox.server.SubjectRI;
import SD.Projeto.util.rmisetup.SetupContextRMI;
import java.io.File;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tiago
 */
public class DropboxClient {

    // Caminho para pasta cliente
    public static final String CLIENTE_PATH = "C:\\Users\\Tiago\\Documents\\NetBeansProjects\\DropBox\\data\\clients\\DropBox";

    /**
     * Context for connecting a RMI client to a RMI Servant
     */
    private SetupContextRMI contextRMI;
    /**
     * Remote interface that will hold the Servant proxy
     */
    private FactoryRI factoryRI;

    public static void main(String[] args) throws InterruptedException, RemoteException {
        if (args != null && args.length < 2) {
            System.err.println("usage: java [options] edu.ufp.sd.helloworld.server.HelloWorldClient <rmi_registry_ip> <rmi_registry_port> <service_name>");
            System.exit(-1);
        } else {
            //1. ============ Setup client RMI context ============
            DropboxClient hwc = new DropboxClient(args);
            //2. ============ Lookup service ============
            hwc.lookupService();
            //3. ============ Play with service ============
            hwc.playService(args);
        }
    }

    public DropboxClient(String args[]) {
        try {
            //List ans set args
            printArgs(args);
            String registryIP = args[0];
            String registryPort = args[1];
            String serviceName = args[2];

            //Create a context for RMI setup
            contextRMI = new SetupContextRMI(this.getClass(), registryIP, registryPort, new String[]{serviceName});
        } catch (RemoteException e) {
            Logger.getLogger(DropboxClient.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private Remote lookupService() {
        try {
            //Get proxy to rmiregistry
            Registry registry = contextRMI.getRegistry();
            //Lookup service on rmiregistry and wait for calls
            if (registry != null) {
                //Get service url (including servicename)
                String serviceUrl = contextRMI.getServicesUrl(0);
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "going to lookup service @ {0}", serviceUrl);

                //============ Get proxy to HelloWorld service ============
                factoryRI = (FactoryRI) registry.lookup(serviceUrl);

            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "registry not bound (check IPs). :(");
                //registry = LocateRegistry.createRegistry(1099);
            }
        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return factoryRI;
    }

    private void playService(String args[]) throws InterruptedException, RemoteException {

        String username = args[3];
        String password = args[4];
        String quinto = args[5];

        //registo(username,password);
        try {

            runCenario1(args); //Criar ficheiro e partilhar com users autorizados
            //runCenario2(args); //Criar ficheiro com o utilizador DIogo que nao partilha a pasta com ninguem
            //runCenario5(args); // Registar utilizador
            //runCenario6(args); // Partilhar pasta com outro utilizador online ou offline
            //runCenario4(args); // Apagar ficheiro
            //runCenario3(args); // criar pasta
            //runCenario7(args); // Apagar pasta
            //runCenario8(args); // Renomear ficheiro
            //runCenario9(args); // Renomear Pasta
        } catch (RemoteException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void printArgs(String args[]) {
        for (int i = 0; args != null && i < args.length; i++) {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "args[{0}] = {1}", new Object[]{i, args[i]});
        }
    }

    // Criar ficheiro com Tiago e Diogo logados
    public void runCenario1(String args[]) throws RemoteException {
        String username = args[3];
        String password = args[4];
        String folderToCreate = args[5];

        SessionRI sessionRI = factoryRI.login(username, password);

        if (sessionRI != null) {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "User {0} autenticado", username);

        } else {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "User {0} nao autenticado", username);
            return;
        }

        ArrayList<SubjectRI> subjectRI = sessionRI.getSubjects();

        for (SubjectRI s : subjectRI) {
            String path = CLIENTE_PATH + username + "\\" + s.getUsername();
            File f = new File(path);
            System.out.println("DropBoxClient " + username + " :Path = " + path);
            ObserverImpl observerRI = new ObserverImpl(username, s, f);
            s.attach(observerRI);
            //Thread t = new Thread((Runnable) observerRI);
            //t.start();

        }

        if (folderToCreate.compareTo("P1") != 0) {
            SubjectRI sub = null;
            if ((sub = sessionRI.getSubjectUser("Tiago")) != null) {

                Object reply = sub.acceptVisitor(new CreateFileVisitor(folderToCreate));
                System.out.println("reply = " + reply);
            } else {
                System.out.println("O utilizador nao partilhou a pasta");
            }

        }
        //String path = "C:\\Users\\Tiago\\Documents\\NetBeansProjects\\DropBox\\data\\clients\\DropBox" + username+"\\"+username;

        /*
        String path = CLIENTE_PATH + username + "\\" + username;
        ObserverImpl observerRI = new ObserverImpl(username, subjectRI, path);

        subjectRI.attach(observerRI);
        System.out.println("folderToCreate = " + folderToCreate);
         */
    }

    
    
    // Criar com o utilizador Diogo
    public void runCenario2(String args[]) throws RemoteException {
        String username = args[3];
        String password = args[4];
        String folderToCreate = args[5];

        SessionRI sessionRI = factoryRI.login(username, password);

        if (sessionRI != null) {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "User {0} autenticado", username);

        } else {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "User {0} nao autenticado", username);
            return;
        }

        ArrayList<SubjectRI> subjectRI = sessionRI.getSubjects();

        for (SubjectRI s : subjectRI) {
            String path = CLIENTE_PATH + username + "\\" + s.getUsername();
            File f = new File(path);
            System.out.println("DropBoxClient " + username + " :Path = " + path);
            ObserverImpl observerRI = new ObserverImpl(username, s, f);
            s.attach(observerRI);
            //Thread t = new Thread((Runnable) observerRI);
            //t.start();

        }

        if (folderToCreate.compareTo("P1") != 0) {
            SubjectRI sub = null;
            if ((sub = sessionRI.getSubjectUser("Diogo")) != null) {

                Object reply = sub.acceptVisitor(new CreateFileVisitor(folderToCreate));
                System.out.println("reply = " + reply);
            } else {
                System.out.println("O utilizador nao partilhou a pasta");
            }

        }

    }

    
    
    //Criar Pasta no utilizador Tiago que partilha com Diogo
    public void runCenario3(String args[]) throws RemoteException {
        String username = args[3];
        String password = args[4];
        String folderToCreate = args[5];

        SessionRI sessionRI = factoryRI.login(username, password);

        if (sessionRI != null) {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "User {0} autenticado", username);

        } else {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "User {0} nao autenticado", username);
            return;
        }

        ArrayList<SubjectRI> subjectRI = sessionRI.getSubjects();

        for (SubjectRI s : subjectRI) {
            String path = CLIENTE_PATH + username + "\\" + s.getUsername();
            File f = new File(path);
            System.out.println("DropBoxClient " + username + " :Path = " + path);
            ObserverImpl observerRI = new ObserverImpl(username, s, f);
            s.attach(observerRI);
            

        }

        if (folderToCreate.compareTo("P1") != 0) {
            SubjectRI sub = null;
            if ((sub = sessionRI.getSubjectUser("Tiago")) != null) {

                Object reply = sub.acceptVisitor(new CreateFolderVisitor(folderToCreate));
                System.out.println("reply = " + reply);
            } else {
                System.out.println("O utilizador nao partilhou a pasta");
            }

        }

    }

    
    //Apagar ficheiro
    public void runCenario4(String args[]) throws RemoteException {
        String username = args[3];
        String password = args[4];
        String folderToCreate = args[5];

        SessionRI sessionRI = factoryRI.login(username, password);

        if (sessionRI != null) {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "User {0} autenticado", username);

        } else {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "User {0} nao autenticado", username);
            return;
        }

        ArrayList<SubjectRI> subjectRI = sessionRI.getSubjects();

        for (SubjectRI s : subjectRI) {
            String path = CLIENTE_PATH + username + "\\" + s.getUsername();
            File f = new File(path);
            System.out.println("DropBoxClient " + username + " :Path = " + path);
            ObserverImpl observerRI = new ObserverImpl(username, s, f);
            s.attach(observerRI);
            //Thread t = new Thread((Runnable) observerRI);
            //t.start();

        }

        if (folderToCreate.compareTo("P1") != 0) {
            SubjectRI sub = null;
            if ((sub = sessionRI.getSubjectUser("Tiago")) != null) {

                Object reply = sub.acceptVisitor(new DeleteFileVisitor(folderToCreate));
                System.out.println("reply = " + reply);
            } else {
                System.out.println("O utilizador nao partilhou a pasta");
            }

        }

    }

    
    //REGISTO
    public void runCenario5(String args[]) throws RemoteException {
        String username = args[3];
        String password = args[4];
        String folderToCreate = args[5];

        registo(username, password);
    }

    //PARTILHA DE DADOS
    public void runCenario6(String args[]) throws RemoteException {
        String username = args[3];
        String password = args[4];
        String userParaPartilhar = args[5];

        SessionRI sessionRI = factoryRI.login(username, password);

        if (sessionRI != null) {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "User {0} autenticado", username);

        } else {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "User {0} nao autenticado", username);
            return;
        }

        ArrayList<SubjectRI> subjectRI = sessionRI.getSubjects();

        for (SubjectRI s : subjectRI) {
            String path = CLIENTE_PATH + username + "\\" + s.getUsername();
            File f = new File(path);
            System.out.println("DropBoxClient " + username + " :Path = " + path);
            ObserverImpl observerRI = new ObserverImpl(username, s, f);
            s.attach(observerRI);
            //Thread t = new Thread((Runnable) observerRI);
            //t.start();

        }
        
        if(userParaPartilhar.compareTo("criar")==0) {
            SubjectRI sub = null;
            if ((sub = sessionRI.getSubjectUser("Tiago")) != null) {

                Object reply = sub.acceptVisitor(new CreateFileVisitor("novo"));
                System.out.println("reply = " + reply);
            } else {
                System.out.println("O utilizador nao partilhou a pasta");
            }
        }

        if(userParaPartilhar.compareTo("nada")!=0 && userParaPartilhar.compareTo("criar")==0){
            if(sessionRI.partilhaPasta(userParaPartilhar)){
                System.out.println("Partilha efetuada com sucesso");
            }else{
                System.out.println("Partilha não efetuada");
            }
            
        }
        //Online
        //if(userParaPartilhar.compareTo("nada")!=0){
        //    sessionRI.partilhaPastaOffline(userParaPartilhar);
        //}
        
    }

    //APAGAR PASTA
    public void runCenario7(String args[]) throws RemoteException {
        String username = args[3];
        String password = args[4];
        String folderToCreate = args[5];

        SessionRI sessionRI = factoryRI.login(username, password);

        if (sessionRI != null) {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "User {0} autenticado", username);

        } else {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "User {0} nao autenticado", username);
            return;
        }

        ArrayList<SubjectRI> subjectRI = sessionRI.getSubjects();

        for (SubjectRI s : subjectRI) {
            String path = CLIENTE_PATH + username + "\\" + s.getUsername();
            File f = new File(path);
            System.out.println("DropBoxClient " + username + " :Path = " + path);
            ObserverImpl observerRI = new ObserverImpl(username, s, f);
            s.attach(observerRI);
            //Thread t = new Thread((Runnable) observerRI);
            //t.start();

        }

        if (folderToCreate.compareTo("P1") != 0) {
            SubjectRI sub = null;
            if ((sub = sessionRI.getSubjectUser("Tiago")) != null) {

                Object reply = sub.acceptVisitor(new DeleteFolderVisitor(folderToCreate));
                System.out.println("reply = " + reply);
            } else {
                System.out.println("O utilizador nao partilhou a pasta");
            }

        }

    }
    
    // Renomear ficheiro , passo com argumentos apenas o nome novo(tenho que ter um ficheiro "teste" presente)
    public void runCenario8(String args[]) throws RemoteException {
        String username = args[3];
        String password = args[4];
        String folderToCreate = args[5];

        SessionRI sessionRI = factoryRI.login(username, password);

        if (sessionRI != null) {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "User {0} autenticado", username);

        } else {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "User {0} nao autenticado", username);
            return;
        }

        ArrayList<SubjectRI> subjectRI = sessionRI.getSubjects();

        for (SubjectRI s : subjectRI) {
            String path = CLIENTE_PATH + username + "\\" + s.getUsername();
            File f = new File(path);
            System.out.println("DropBoxClient " + username + " :Path = " + path);
            ObserverImpl observerRI = new ObserverImpl(username, s, f);
            s.attach(observerRI);
            //Thread t = new Thread((Runnable) observerRI);
            //t.start();

        }

        if (folderToCreate.compareTo("P1") != 0) {
            SubjectRI sub = null;
            if ((sub = sessionRI.getSubjectUser("Tiago")) != null) {

                Object reply = sub.acceptVisitor(new RenameFileVisitor("teste",folderToCreate));
                System.out.println("reply = " + reply);
            } else {
                System.out.println("O utilizador nao partilhou a pasta");
            }

        }

    }
    
    
    // Renomar uma pasta, passo com argumentos apenas o nome novo(tenho que ter um ficheiro "teste" presente)
    public void runCenario9(String args[]) throws RemoteException {
        String username = args[3];
        String password = args[4];
        String folderToCreate = args[5];

        SessionRI sessionRI = factoryRI.login(username, password);

        if (sessionRI != null) {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "User {0} autenticado", username);

        } else {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "User {0} nao autenticado", username);
            return;
        }

        ArrayList<SubjectRI> subjectRI = sessionRI.getSubjects();

        for (SubjectRI s : subjectRI) {
            String path = CLIENTE_PATH + username + "\\" + s.getUsername();
            File f = new File(path);
            System.out.println("DropBoxClient " + username + " :Path = " + path);
            ObserverImpl observerRI = new ObserverImpl(username, s, f);
            s.attach(observerRI);
            //Thread t = new Thread((Runnable) observerRI);
            //t.start();

        }

        if (folderToCreate.compareTo("P1") != 0) {
            SubjectRI sub = null;
            if ((sub = sessionRI.getSubjectUser("Tiago")) != null) {

                Object reply = sub.acceptVisitor(new RenameFolderVisitor("teste",folderToCreate));
                System.out.println("reply = " + reply);
            } else {
                System.out.println("O utilizador nao partilhou a pasta");
            }

        }

    }
    
    private void registo(String username, String password) throws RemoteException {
        if (this.factoryRI.register(username, password)) {
            System.out.println("entrou");
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "User {0} registado", username);
            String path = CLIENTE_PATH + username;
            File novaPasta = new File(path);
            novaPasta.mkdir();
            path = path + "\\" + username;
            File novaPasta2 = new File(path);
            novaPasta2.mkdir();
        } else {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "User {0} nao registado", username);
        }
    }

}
