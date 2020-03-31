package core.guild.modules.commands;

import botcore.EmbedWithPicture;
import botcore.MessageHolder;
import botcore.Output;
import core.guild.modules.CommandController;
import core.guild.modules.CommandReturn;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Executor {

    private ExecutorService executorService;
    private CommandHolder commandHolder;
    private CommandController commandController;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private String moduleName;

    public Executor(CommandController controller, String name) {
        executorService = Executors.newSingleThreadExecutor();
        executorService.submit(()->Thread.currentThread().setName(name));
        this.commandController = controller;
        commandHolder = new CommandHolder();
        moduleName = name;
    }

    public void executeCommand(String command, String[] args, long channelID) {
        executorService.submit(() ->{

            if(command.equalsIgnoreCase("commands")) {
                Output.sendMessageToChannel(channelID,getCommands());
                return;
            }
            logger.info("Executing command {} with parameters", command);
            try{
                CommandReturn commandReturn = commandController.executeCommand(command, args);
                Object o = commandReturn.getContent();
                MessageHolder messageHolder = null;
                messageHolder = commandReturn.getMessageHolder();
                if (o instanceof Message) {
                    Output.sendMessageToChannel(channelID, (Message) o, messageHolder);
                } else if (o instanceof MessageEmbed) {
                    Output.sendMessageToChannel(channelID, (MessageEmbed) o, messageHolder);
                } else if (o instanceof EmbedWithPicture) {
                    Output.sendMessageToChannel(channelID, (EmbedWithPicture) o, messageHolder);
                } else {
                    Output.sendMessageToChannel(channelID, o.toString());
                }
            } catch (Exception e){
                logger.error("{} while executing {}",e.getMessage(), command);
            }
        });
    }

    public MessageEmbed getCommands() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        if (commandController.getCommands() == null){
            return embedBuilder.setTitle("No commands :(").build();
        }
        embedBuilder.setTitle(moduleName);
        commandController.getCommands().forEach(command->{
            embedBuilder.addField(command.getName(),command.getDescription(),true);
        });
        return embedBuilder.build();
    }

    public CommandController getCommandController() {
        return commandController;
    }

    public void shutdown() {
        logger.info("shutdown of {} executor",moduleName);
        executorService.shutdown();
    }
}
