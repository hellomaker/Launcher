package io.github.hellomaker.launcher.verify.storage;

import io.github.hellomaker.launcher.common.SafeFileWriter;
//import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * @author hellomaker
 */
//@Slf4j
public class SaferStorageImpl implements SaferStorage {

    static Logger log = LoggerFactory.getLogger(SaferStorageImpl.class);

    @Override
    public String getTextFromFile(String path) {
        File dataDir = new File(path);
        try {
            return FileUtils.readFileToString(dataDir);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void saveTextToFile(String content, String path) {
        File dataDir = new File(path);
        if (!dataDir.getParentFile().exists()) {
            try {
                FileUtils.forceMkdir(dataDir.getParentFile());
            } catch (IOException e) {

            }
        }
        try {
            SafeFileWriter.writeDataSafely("active/temp.dat", path, content);
        } catch (IOException e) {
            log.error("保存激活文件失败", e);
        }
    }

}
