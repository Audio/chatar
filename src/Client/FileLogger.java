package Client;

import java.io.IOException;
import java.nio.file.*;


public class FileLogger {

    private Path dir;
    private Path file;


    public FileLogger(String dirName, String fileName) {
        String path = "./log/" + dirName;
        this.dir = FileSystems.getDefault().getPath(path);
        this.file = FileSystems.getDefault().getPath(path, fileName + ".log");
    }

    public void log(String content) {
        try {
            if ( !Files.exists(dir, LinkOption.NOFOLLOW_LINKS) )
                Files.createDirectory(dir);

            content += System.getProperty("line.separator");
            Files.write(file, content.getBytes(), StandardOpenOption.CREATE,
                                                  StandardOpenOption.APPEND);
        } catch (IOException ex) {
            System.err.println("Nelze zapisovat do log souboru " + file);
        }
    }

}
