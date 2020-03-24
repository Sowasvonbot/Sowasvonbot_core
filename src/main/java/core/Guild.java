package core;

import core.guild.CommandListener;
import core.guild.ModuleController;

public class Guild {

    private long guildID;
    private String name;
    private long ownerID;
    private ModuleController moduleController;
    private CommandListener commandListener;


    public ModuleController getModuleController() {
        return moduleController;
    }

    public void setModuleController(ModuleController moduleController) {
        this.moduleController = moduleController;
    }

    public long getGuildID() {
        return guildID;
    }

    public void setGuildID(long guildID) {
        this.guildID = guildID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(long ownerID) {
        this.ownerID = ownerID;
    }

    public Guild(long guildID, String name, long ownerID) {
        this.guildID = guildID;
        this.name = name;
        this.ownerID = ownerID;
    }

    public CommandListener getCommandListener() {
        return commandListener;
    }

    public void setCommandListener(CommandListener commandListener) {
        this.commandListener = commandListener;
    }
}
