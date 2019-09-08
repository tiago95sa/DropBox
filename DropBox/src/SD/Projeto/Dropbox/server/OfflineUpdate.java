/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SD.Projeto.Dropbox.server;

import SD.Projeto.Dropbox.client.ObserverRI;
import java.io.File;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tiago
 */
public class OfflineUpdate implements Runnable {

    private SubjectRI subjectRI;
    private ObserverRI observerRI;
    private HashMap<File, Long> last;
    private HashMap<File, Long> current;

    
    public OfflineUpdate(SubjectRI subjectRI, ObserverRI observer ) throws RemoteException {
        this.subjectRI = subjectRI;
        this.observerRI = observer;
        this.last = new HashMap();
        this.current = observer.getFicheiros();
    }

    
    /**
     * Corre o thread preenchendo as duas hashtables com os ficheiros e timestamps. De seguida verifica se existe o ficheiro , se nao e
     * existir cria , se existir compara timestamps e ve qual o maior.
     */
    @Override
    public void run(){
        try {
            // preencher as hashMap
            File filesub = subjectRI.getFile();
            
            
            
            for (File fileLast : filesub.listFiles()) {
                
                last.put(fileLast, (Long)fileLast.lastModified());
            }
            
            for (Map.Entry<File, Long> f : current.entrySet()) {
                System.out.println(f.getKey().getName());
            }
            
            
            
            //comparar TimeStamps
            int state = 0;
            for (Map.Entry<File, Long> fora : last.entrySet()) {
                for (Map.Entry<File, Long>  dentro : current.entrySet()) {
                    if(fora.getKey().getName().compareTo(dentro.getKey().getName())== 0){
                        if(fora.getValue().compareTo(dentro.getValue()) > 0){
                            observerRI.update(new DeleteFileVisitor(fora.getKey().getName()));
                            observerRI.update(new CreateFileVisitor(fora.getKey().getName()));
                            state = 1;
                        }
                        break;
                    }
                }
                if(state != 1){
                observerRI.update(new CreateFileVisitor(fora.getKey().getName()));
                }
                state = 0;
            }
            
        } catch (RemoteException ex) {
            Logger.getLogger(OfflineUpdate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
   
}
