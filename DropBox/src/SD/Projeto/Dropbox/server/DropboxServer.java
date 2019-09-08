/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SD.Projeto.Dropbox.server;

import SD.Projeto.util.rmisetup.SetupContextRMI;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tiago
 */
public class DropboxServer {

    
    // Caminho para pasta servidor
    public static final String SERVER_PATH = "C:\\Users\\Tiago\\Documents\\NetBeansProjects\\DropBox\\data\\servers";
     public static final String CLIENTE_PATH = "C:\\Users\\Tiago\\Documents\\NetBeansProjects\\DropBox\\data\\clients\\DropBox";
    /**
     * Context for running a RMI Servant on a host
     */
    private SetupContextRMI contextRMI;
    /**
     * Remote interface that will hold reference to the Servant impl
     */
    private FactoryRI factoryRI;
    
    
   public static void main(String[] args) throws IOException {
        if (args != null && args.length < 3) {
            System.err.println("usage: java [options] edu.ufp.sd.dropbox.server.DropboxServer <rmi_registry_ip> <rmi_registry_port> <service_name>");
            System.exit(-1);
        } else {
            //1. ============ Create Servant ============
            DropboxServer hws = new DropboxServer(args);
            //2. ============ Rebind servant on rmiregistry ============
            hws.rebindService();
            
        }
    }
   
    /**
     *
     * @param args
     */
    public DropboxServer(String args[]) {
        try {
            //============ List and Set args ============
            printArgs(args);
            String registryIP = args[0];
            String registryPort = args[1];
            String serviceName = args[2];
            //============ Create a context for RMI setup ============
            contextRMI = new SetupContextRMI(this.getClass(), registryIP, registryPort, new String[]{serviceName});
        } catch (RemoteException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        
        }
        
    }
    
    private void rebindService() {
        try {
            //Get proxy to rmiregistry
            Registry registry = contextRMI.getRegistry();
            //Bind service on rmiregistry and wait for calls
            if (registry != null) {
                //============ Create Servant ============
                factoryRI = new FactoryImpl();

                //Get service url (including servicename)
                String serviceUrl = contextRMI.getServicesUrl(0);
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Servidor a ligar @ {0}", serviceUrl);

                //============ Rebind servant ============
                //Naming.bind(serviceUrl, helloWorldRI);
                registry.rebind(serviceUrl, factoryRI);
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Servidor a funcionar");
            } else {
                //System.out.println("HelloWorldServer - Constructor(): create registry on port 1099");
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "registry not bound (check IPs). :(");
                //registry = LocateRegistry.createRegistry(1099);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void printArgs(String args[]) {
        for (int i = 0; args != null && i < args.length; i++) {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "args[{0}] = {1}", new Object[]{i, args[i]});
        }
    }
    
        
        
}
