/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SD.Projeto.Dropbox.server;

import java.io.Serializable;

/**
 *
 * @author Tiago
 */
public interface VisitorI extends Serializable {
    public Object visit(SubjectObserverRI subject);
    //public boolean criarFicheiro(String path);
}