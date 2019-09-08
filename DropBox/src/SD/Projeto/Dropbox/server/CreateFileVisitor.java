/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SD.Projeto.Dropbox.server;

import SD.Projeto.Dropbox.client.ObserverImpl;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

/**
 *
 * @author Tiago
 */
public class CreateFileVisitor implements VisitorI , Serializable{
    
    String nome;
    
    
    public CreateFileVisitor(String nome){
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

    
    /**
     * Verifica se é uma instancia de Subject. Se sim chama o singleton e cria no lado do servidor , se não no lado do cliente.
     * @param subject
     * @return 
     */
    @Override
    public Object visit(SubjectObserverRI subject) {
        Object reply = null;
        if (subject instanceof SubjectImpl){
            System.out.println("visit server");
            reply = ((SubjectImpl)subject).getSingletonFolder().createFile(nome);
        } if (subject instanceof ObserverImpl){
            reply = ((ObserverImpl)subject).getSingletonFolder().createFile(nome);
        }
        return reply;
    }
}
