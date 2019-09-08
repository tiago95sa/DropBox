/**
 * <p>Title: Projecto SD</p>
 *
 * <p>Description: Projecto apoio aulas SD</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: UFP & INESC Porto</p>
 *
 * @author Rui Moreira
 * @version 1.0
 */
package SD.Projeto.Dropbox.server;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SingletonFolderOperationsUser implements SingletonFoldersOperationsI {
    
    private static SingletonFolderOperationsUser singletonFolderOperationsUser;
    private final File folderUser;

    /** private - Avoid direct instantiation
     * @param folder */
    public SingletonFolderOperationsUser(String folder) {
        folderUser = new File(folder);
    }
    
    public synchronized static SingletonFolderOperationsUser createSingletonFolderOperationsBooks(String folder){
        if (singletonFolderOperationsUser==null){
            singletonFolderOperationsUser = new SingletonFolderOperationsUser(folder);
        }
        return singletonFolderOperationsUser;
    }
    
    
    /**
     * Função para criar ficheiro
     * @param fname
     * @return 
     */
    @Override
    public Boolean createFile(String fname) {
        try {
            File newFile = new File(this.folderUser.getAbsolutePath() + "/" + fname);
            return newFile.createNewFile();
        } catch (IOException ex) {
           // Logger.getLogger(SingletonFolderOperationsMagazines.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    /**
     * Função para apagar ficheiros
     * @param fname
     * @return 
     */
    @Override
    public Boolean deleteFile(String fname) {
            File existingFile = new File(this.folderUser.getAbsolutePath() + "/" + fname);
            return existingFile.delete();
    }

    
    /**
     * Função para criar Pastas
     * @param folder
     * @return 
     */
    @Override
    public Boolean createFolder(String folder) {
        File novodiretorio = new File(this.folderUser.getAbsolutePath() + "/" + folder);
        //System.out.println("createFolder");
        if (!novodiretorio.exists()) {
                //System.out.println("entre no if");
                novodiretorio.mkdirs();
                System.out.println("Diretorio criado com sucesso");
                return true;
        }else{
        System.out.println("Diretorio não criado");
        return false;
        }
    }
    
    
    /**
     * Função para apagar pasta
     * @param folder
     * @return 
     */
    @Override
    public Boolean deleteFolder(String folder) {

        File diretorioexistente = new File(this.folderUser.getAbsolutePath() + "/" + folder);

        if (diretorioexistente.isDirectory()) {
                diretorioexistente.delete();
                System.out.println("Diretorio apagado com sucesso");
                return true;
        }else{
        System.out.println("Diretorio não apagado");
        return false;
        }
    }
    
    
    /**
     * Funcao para renomear ficheiro
     * @param nome nome antigo
     * @param novoNome novo nome
     * @return 
     */
    @Override
    public Boolean renameFile(String nome, String novoNome) {

        File existente = new File(this.folderUser.getAbsolutePath() + "/" + nome);
        File novo = new File(this.folderUser.getAbsolutePath() + "/" + novoNome);
        if (existente.isFile()) {

            return existente.renameTo(novo);

        }
        return false;

    }
    
    
    /**
     * Função para renomear uma pasta
     * @param pasta nome antigo da pasta
     * @param novoNome novo nome
     * @return 
     */
    @Override
    public Boolean renameFolder(String pasta, String novoNome) {

        File existente= new File(this.folderUser.getAbsolutePath() + "/" + pasta);
        File novo = new File(this.folderUser.getAbsolutePath() + "/" + novoNome);

        if (existente.isDirectory()) {

            return existente.renameTo(novo);
        }

        return false;
    }
    
    
}

