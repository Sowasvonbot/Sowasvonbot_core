package main.fileManagement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileSaver {


    private static FileSaver instance;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());



    public boolean saveContentToFile(String filename, String content){
        logger.info("FileSaver: Writing content to file {}", filename);
        BufferedWriter writer = getWriter(filename);
        if (writer == null) return false;
        try {
            writer.write(content);
            writer.flush();
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }



    }

    private BufferedWriter getWriter(String fileName){
        try{
            return new BufferedWriter(new FileWriter(
                    FileLoader.getInstance().getFileContainsName(fileName)
            ));

        } catch (IOException e){
            logger.error("File: {} couldn't be found.", fileName);
            return null;
        }

    }


    public static FileSaver getInstance() {
        if (instance == null) instance = new FileSaver();
        return instance;
    }
}
