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
public class RenameFileVisitor implements VisitorI , Serializable{
    
    String nomeAntigo;
    String nomeNovo;
    
    
    public RenameFileVisitor(String antigo , String novo ){
        this.nomeAntigo = antigo;
        this.nomeNovo = novo;
    }
    
    
    @Override
    public Object visit(SubjectObserverRI subject) {
        Object reply = null;
        if (subject instanceof SubjectImpl){
            reply = ((SubjectImpl)subject).getSingletonFolder().renameFile(nomeAntigo, nomeNovo);
        } if (subject instanceof ObserverImpl){
            reply = ((ObserverImpl)subject).getSingletonFolder().renameFile(nomeAntigo, nomeNovo);
        }
        return reply;
    }
}