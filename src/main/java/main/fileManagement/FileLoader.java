package main.fileManagement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class FileLoader {

    private static FileLoader instance;
    private HashMap<String, File> myFiles;
    private final String filepath = System.getProperty("user.dir");
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private FileLoader(){
        myFiles = new HashMap<>();
        //Load all Files in Data
        File dataFolder = new File(filepath + File.separator + "data");
        if (!dataFolder.exists()) dataFolder.mkdir();

        for (File tempFile : dataFolder.listFiles()){
            try {
                if (tempFile.isFile()) loadFileFromClasspath("data" + File.separator + tempFile.getName());
            } catch (IOException e){
                logger.warn("File {} does not exist!",tempFile.getName());
            }
        }
    }

    public File loadFileFromClasspath (String name) throws IOException{
        File newFile ;
        URL fileUrl = getClass().getResource(name);
        try {
            newFile = new File(fileUrl.getFile());

        } catch (NullPointerException nullPointer){
            newFile = new File(filepath + File.separator + name);
            newFile.getParentFile().mkdirs();
            newFile.createNewFile();
        }
        myFiles.put(newFile.getPath(),newFile);
        return newFile;

    }

    public File createDir(String filepath){
        File newFile;
        URL fileURL = getClass().getResource(filepath);
        try {
            newFile = new File(fileURL.getPath());
            return newFile;
        } catch (NullPointerException nullPointer){
            newFile = new File(this.filepath + File.separator + filepath);
            if (newFile.isDirectory()) {
                newFile.mkdir();
                return newFile;
            }
            else {
                newFile.mkdirs();
                return newFile;
            }

        }
    }

    public File getFileContainsName(String name){
        for (Map.Entry<String, File> entry:myFiles.entrySet()) {
            if (entry.getKey().contains(name)){
                return entry.getValue();
            }
        }
        return null;
    }




    public static FileLoader getInstance(){
        if (instance == null) instance = new FileLoader();
        return instance;
    }

}
