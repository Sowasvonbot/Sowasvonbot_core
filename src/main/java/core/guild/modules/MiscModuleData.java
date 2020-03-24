package core.guild.modules;


import java.io.BufferedWriter;

/**
 * In this interface are some misc datas defined
 * Every module gets his own data file (without knowing its path or type)
 * The methods below will me called at the start and stop of the bot
 */
public interface MiscModuleData {


    /**
     * Will be called at the start. fileContent is the complete content found in the file as string.
     * @param fileContent the complete content of his data file
     * @return True, when the file content is in the correct format
     */
    boolean loadConfig(String fileContent);


    /**
     * Will be called, when the program terminates. The return is all data, which should be saved in one string. The format is freely selectable
     * @return All data to be saved as one String
     */
    String saveConfig();


}
