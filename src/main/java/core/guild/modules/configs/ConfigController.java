package core.guild.modules.configs;

import botcore.Bot;
import core.GuildHandler;
import core.guild.modules.CommandController;
import core.guild.modules.commands.Executor;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

import java.util.Map;
import java.util.concurrent.ExecutorService;

public class ConfigController {

    private long UserID;
    private long prChannelID;
    private ExecutorService executorService;
    private User user;
    private Map<String,Executor> executors;
    private long guildID;
    private Timer timer;
    private Status currStatus;
    private CommandController commandController;


    public ConfigController(long userID, long guildID, Map<String,Executor> executors) {
        UserID = userID;
        this.guildID = guildID;
        this.executors = executors;
        user = Bot.getUser(userID);
        if (this.hasAdminPermission(userID,guildID)) sendMessage("Du bist Admin auf dem Server "+ Bot.getGuild(guildID).getName());
        else {
            sendMessage("Du bist kein Admin auf dem Server " + Bot.getGuild(guildID).getName());
            return;
        }

        sendMessage("Config session started. Could be exited at any time with \"stop config\". " +
                "\nOtherwise the session will close after 10 minutes of inactivity.");
        selectModule();
        timer = new Timer(user,new ConfigListener(userID, this), this);


    }

    protected void incomingMessage(String message){
        timer.reset();
        if (message.toLowerCase().contains("stop config")) {
            timer.closeConfigSession();
            return;
        }
        switch (currStatus){
            case onModuleSelect:
                if (executors.containsKey(message)){
                    commandController = executors.get(message).getCommandController();
                    sendMessage("Successfully selected module " + message);
                    sendMessage(commandController.getConfigDescription());
                    sendMessage("values returns the current chat values");
                    currStatus = Status.moduleSelected;
                    return;
                }
                sendMessage("Don't know module: " + message);
                break;
            case moduleSelected:
                switch (message.toLowerCase()){
                    case "exit":
                    case "stop":
                    case "quit":
                        sendMessage("Quit editing module");
                        selectModule();
                        return;
                    case "values":
                        StringBuilder res = new StringBuilder();
                        for (Map.Entry<String,String> entry:commandController.getConfigVariables().entrySet()) {
                            res.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
                        }
                        sendMessage(res.toString());
                        break;
                    default:
                        String[] args = message.split(" ");
                        if (args.length < 2){
                            sendMessage("Missing parameter or command in " + message);
                            return;
                        }
                        sendMessage(commandController.setConfigVariable(args[0],argsToString(args),guildID));
                        GuildHandler.getInstance().saveConfigs(guildID);
                }
        }
    }

    private String argsToString(String[] args){
        StringBuilder res = new StringBuilder();
        for (int i = 1; i < args.length; i++) res.append(args[i]);
        return res.toString();
    }

    private void selectModule(){
        currStatus = Status.onModuleSelect;
        sendMessage("Which module do you want to edit?");
        sendMessage(getModulesToString());
    }

    private Boolean hasAdminPermission(long clientID, long guildID){
        Member member = Bot.getGuild(guildID).getMemberById(clientID);
        return member.hasPermission(Permission.ADMINISTRATOR);
    }

    protected void sendMessage(String content){
        user.openPrivateChannel().queue((channel) -> channel.sendMessage(content).queue());

    }



    private String getModulesToString(){
        String allModules = "";
        for (String name : executors.keySet()) {
            allModules = allModules.concat(name + ", ");
        }
        if (allModules.equals("")) allModules = "No modules found";
        return allModules.substring(0,allModules.length()-2);
    }


    private enum Status{
        onModuleSelect, moduleSelected
    }


}
