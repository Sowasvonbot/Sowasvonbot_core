package core.guild.modules;

import core.guild.modules.commands.Command;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;


/**
 * Import this interface if you make a new modul
 * It is higly recommend to use this interface, so all modules controllers are able to be easily loaded
 */
public interface CommandController {

    /**
     * Returns the name of the module, maybe just Twitch for the Twitch API or Trollchat for a module that spams commands randomly
     * @return The name of the module as String
     */
    String getName();

    /**
     * This method will return a list of all command with the required data. See the class CommandData for an overview of the return
     * @return A list of CommandData's with all public methods
     */
    List<Command> getCommands();

    /**
     * First you have to get all commands names via getCommands and then you are able to execute one of them with this method
     * @param command The command to be executed as String
     * @return The return value specified in getMethods
     * @param args All required inputs for the given command, null will be accepted if no inputs are required
     */
    CommandReturn executeCommand(@Nonnull String command, String[] args);


    /**
     * This method will return the whole config description of the module. So all variables and all options have to be listed in this String
     * Also a how to use would be great.
     * @return the String with all config options
     */
    String getConfigDescription();

    /**
     * returns all ConfigVariables for the module and their values.
     * @return The Hashmap with the data. First the name, second the value
     */
    HashMap<String, String> getConfigVariables();

    /**
     * Sets the value for the given variable. Must be SUCCESS, when operation was successful
     * @param variable The name of the variable to be changed
     * @param value The value to be set
     * @return The response, maybe variable not found or value not accepted on failure, SUCCESS when successful
     */
    String setConfigVariable(String variable, String value, long guildID);


}
