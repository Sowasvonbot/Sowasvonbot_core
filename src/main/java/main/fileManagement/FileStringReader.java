package main.fileManagement;

import java.io.BufferedReader;
import java.util.Optional;

public class FileStringReader {


    private static FileStringReader instance;


    public String getFileContentAsString(String fileName){
        try{
            BufferedReader reader = new BufferedReader(new java.io.FileReader(FileLoader.getInstance().getFileContainsName(fileName)));
            Optional<String> result;
            result = reader.lines().reduce(String::concat);
            return result.orElse("ERROR");
        } catch (Exception e){
            return "ERROR";
        }
    }

    public static FileStringReader getInstance(){
        if (instance == null) instance = new FileStringReader();
        return instance;
    }
}
