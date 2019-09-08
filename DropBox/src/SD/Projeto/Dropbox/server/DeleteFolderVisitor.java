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
public class DeleteFolderVisitor implements VisitorI , Serializable{
    
    String nome;
    
    
    public DeleteFolderVisitor(String nome){
        this.nome = nome;
    }
    
    
    @Override
    public Object visit(SubjectObserverRI subject) {
        Object reply = null;
        if (subject instanceof SubjectImpl){
            reply = ((SubjectImpl)subject).getSingletonFolder().deleteFolder(nome);
        } if (subject instanceof ObserverImpl){
            reply = ((ObserverImpl)subject).getSingletonFolder().deleteFolder(nome);
        }
        return reply;
    }
}
