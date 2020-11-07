package main.core.guild;

import main.core.BigDiscordBot;
import main.core.guild.modules.commands.Executor;
import main.fileManagement.FileLoader;
import main.fileManagement.FileSaver;
import main.fileManagement.FileStringReader;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class ModuleController {

    private ModuleHolder moduleHolder;
    private String filepath;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public ModuleController(String filepath, ModuleHolder moduleHolder) {
        this.filepath = filepath;

        this.moduleHolder = moduleHolder;
        //TODO Add modules here

        BigDiscordBot.getInstance().getRegisteredModules().forEach(moduleAPI -> {
            try {
                Module module = moduleAPI.getConstructor().newInstance().getModule();
                moduleHolder.addModule(module);
                logger.info("Loaded module {}", module.getName());
            } catch (NoSuchMethodException noMethod){
                logger.error("Loading module {} failed", moduleAPI.getName());
                logger.error("No valid constructor found");
            } catch (Exception e){
                e.printStackTrace();
            }
        });



        initModules();
        logger.info("Modules loaded");

    }

    public Boolean getActiveStatus(String name){
        String path = filepath + File.separator + name;
        try {
            FileLoader.getInstance().loadFileFromClasspath(path);
        } catch (IOException e){
            e.printStackTrace();
        }
        String content  = FileStringReader.getInstance().getFileContentAsString(path);
        JSONObject jsonContent;
        try{
            jsonContent = new JSONObject(content);
            Boolean result = jsonContent.getBoolean(name);
            logger.info("Found isActive key in {}", name);
            return result;
        }catch (JSONException e){
            logger.info("Coulnd't found is active key in {}", name);
            return false;
        }
    }

    public int safeConfig() {
        int exitcode = 0;
        logger.info("Set of modules shutdown");
        moduleHolder.getModuleList().forEach((module -> {
            if (module.getModuleData() == null) {
                FileSaver.getInstance().saveContentToFile(
                        filepath + File.separator + module.getName(),
                        "{\n"+module.getName()+":false\n}");
            } else {
                JSONObject config = new JSONObject();
                config.put(module.getName(), module.isOnline());

                config.put("ModuleData",module.getModuleData().saveConfig());
                FileSaver.getInstance().saveContentToFile(filepath + File.separator + module.getName(), config.toString());
            }
        }));
        return exitcode;
    }


    public HashMap<String, Executor> getExecutorsForAllModules(){
        HashMap<String, Executor> executors = new HashMap<>();
        moduleHolder.getModuleList().forEach((module -> executors.put(module.getName(),new Executor(module.getController(), module.getName()))));
        return executors;
    }



    private void initModules(){
        moduleHolder.getModuleList().forEach((module -> {
            String configContent;
            module.setOnline(getActiveStatus(module.getName()));
            String path = filepath + File.separator + module.getName();
            configContent =  FileStringReader.getInstance().getFileContentAsString(path);
            logger.debug("Loaded config {}",configContent);
            try {
                module.getModuleData().loadConfig(new JSONObject(configContent).getString("ModuleData"));
            } catch (JSONException e){
                logger.info(e.getMessage());
                logger.info("Initiating new config");
                module.getModuleData().loadConfig("");
            }
        }));
    }

    public void shutdown(){
        safeConfig();

    }
}
