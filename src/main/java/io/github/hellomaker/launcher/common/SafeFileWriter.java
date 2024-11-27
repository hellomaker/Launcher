package io.github.hellomaker.launcher.common;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author xianzhikun
 * @version 1.0.0
 **/
public class SafeFileWriter {

    public static void writeDataSafely(String tempFileName, String finalFileName, String content) throws IOException {
        try (FileChannel channel = FileChannel.open(Paths.get(tempFileName), StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {
            // Write data to the channel
            channel.write(ByteBuffer.wrap(content.getBytes()));

            // Force the data to be written to disk
            channel.force(true);
        }

        // Rename the temporary file to the final file name
        Files.move(Paths.get(tempFileName), Paths.get(finalFileName),
                java.nio.file.StandardCopyOption.ATOMIC_MOVE);
    }
}
