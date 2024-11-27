package io.github.hellomaker.launcher.verify.storage;

/**
 * @author hellomaker
 */
public interface SaferStorage {

    String getTextFromFile(String path);

    void saveTextToFile(String content, String path);

}
