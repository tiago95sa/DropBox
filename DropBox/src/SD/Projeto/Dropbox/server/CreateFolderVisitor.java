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
public class CreateFolderVisitor implements VisitorI , Serializable{
    
    String nome;
    
    
    public CreateFolderVisitor(String nome){
        this.nome = nome;
    }
    
    
    

    @Override
    public Object visit(SubjectObserverRI subject) {
        Object reply = null;
        if (subject instanceof SubjectImpl){
            reply = ((SubjectImpl)subject).getSingletonFolder().createFolder(nome);
        } if (subject instanceof ObserverImpl){
            reply = ((ObserverImpl)subject).getSingletonFolder().createFolder(nome);
        }
        return reply;
    }
}
