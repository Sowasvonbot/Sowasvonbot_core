package main.jar_handling;

import java.io.File;
import java.io.IOException;

public class JarFile extends java.util.jar.JarFile {


    private boolean isLoaded;


    public JarFile(File file) throws IOException {
        super(file);
        isLoaded = false;
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public void setLoaded(boolean loaded) {
        isLoaded = loaded;
    }
}
