package jar_handling;

import core.BigDiscordBot;
import core.GuildHandler;
import core.guild.modules.ModuleAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;

public class JarLoader {

    private static JarLoader instance;
    private List<JarFile> jarList;

    private Logger logger;

    private final String filepath = System.getProperty("user.dir");


    private JarLoader(){
        logger = LoggerFactory.getLogger("JarLoader");
        jarList = new ArrayList<>();
    }

    public void loadJars(){
        File directory = new File(filepath + File.separator + "plugins" + File.separator);
        if(!directory.exists()) directory.mkdir();
        if (!directory.isDirectory()){
            logger.error("{} is not a directory",directory.getPath());
            return;
        }



        FilenameFilter filenameFilter = new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                if (s.toLowerCase().endsWith(".jar")) return true;
                return false;
            }
        };

        Arrays.asList(directory.listFiles(filenameFilter)).forEach(file -> {
            try {
                JarFile jarFile = new JarFile(file);
                if (!jarList.contains(jarFile)) jarList.add(new JarFile(file));
                logger.info("Loaded {} to plugin jars",file.getName());
            } catch (IOException e) {
                logger.error("{} is not a jar file",file.getName());
            }
        });
    }

    public void cleanJars(){
        jarList.clear();
    }


    public void loadAllClasses(){
        String jarPath = filepath+File.separator+"plugins";
        int classLength = String.valueOf(".class").length();
        String className = "Test";

        for (JarFile jarFile: jarList) {
            if (jarFile.isLoaded()) continue;
            Enumeration<JarEntry> entries = jarFile.entries();
            try {
                logger.info("Loading jar file: {}", jarFile.getName());
                URL[] urls = { new URL("jar:file:" + jarFile.getName() + "!/") };
                URLClassLoader classLoader = URLClassLoader.newInstance(urls);


                while (entries.hasMoreElements()){
                    JarEntry jarEntry = entries.nextElement();

                    //Skip directories and not class files
                    if (jarEntry.isDirectory() || !jarEntry.getName().endsWith(".class")) continue;


                    logger.info("Loading class {}",jarEntry.getName());
                    className = jarEntry.getName().substring(0, jarEntry.getName().length()-classLength);
                    className =  className.replace("/",".");
                    Class c = classLoader.loadClass(className);
                    if (c.getSuperclass().equals(ModuleAPI.class)) BigDiscordBot.getInstance().registerModule(c);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException classNotFound){
                classNotFound.printStackTrace();
                logger.warn("Class {} was not found in jar file {}", className, jarFile.getName());
            } finally {
                jarFile.setLoaded(true);
                try {
                    jarFile.close();
                } catch (IOException ex){
                    logger.error(ex.getLocalizedMessage());
                }
            }
        }
    }

    public void reload(){
        logger.info("Reloading jars");
        try {
            cleanJars();
            loadJars();
            BigDiscordBot.getInstance().clearModules();
            loadAllClasses();
            GuildHandler.reloadGuildHandler();
        }catch (Exception e){
            e.printStackTrace();
        }
        logger.info("Reloading done");
    }





    public static JarLoader getInstance(){
        if (instance == null) instance = new JarLoader();
        return instance;
    }
}
