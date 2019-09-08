/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SD.Projeto.Dropbox.server;

import SD.Projeto.Dropbox.client.ObserverImpl;
import java.io.Serializable;

/**
 *
 * @author Tiago
 */
public class DeleteFileVisitor implements VisitorI , Serializable{
    
    String nome;
    
    
    public DeleteFileVisitor(String nome){
        this.nome = nome;
    }
    
    /*
    @Override
    public boolean criarFicheiro(String path){
        try {
            File newFile = new File(path + "/" + nome);
            return newFile.createNewFile();
        } catch (IOException ex) {
           // Logger.getLogger(SingletonFolderOperationsMagazines.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }*/

    @Override
    public Object visit(SubjectObserverRI subject) {
        Object reply = null;
        if (subject instanceof SubjectImpl){
            reply = ((SubjectImpl)subject).getSingletonFolder().deleteFile(nome);
        } if (subject instanceof ObserverImpl){
            reply = ((ObserverImpl)subject).getSingletonFolder().deleteFile(nome);
        }
        return reply;
    }
}
