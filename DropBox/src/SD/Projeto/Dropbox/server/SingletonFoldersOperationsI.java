package SD.Projeto.Dropbox.server;

/**
 *
 * @author rui
 */
public interface SingletonFoldersOperationsI {
    public Boolean createFile(String fname);
    public Boolean deleteFile(String fname);
    public Boolean deleteFolder(String folder);
    public Boolean createFolder(String fname);
    public Boolean renameFile(String nome, String novoNome);
    public Boolean renameFolder(String pasta, String novoNome);
}
