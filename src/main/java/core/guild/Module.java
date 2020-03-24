package core.guild;

import core.guild.modules.CommandController;
import core.guild.modules.MiscModuleData;
import core.guild.modules.commands.Executor;

import javax.annotation.Nonnull;

public class Module {


    private CommandController controller;
    private MiscModuleData moduleData;
    private String name;
    private boolean online;
    private Executor executor;


    public Module(@Nonnull CommandController controller, @Nonnull MiscModuleData moduleData, String name) {
        this.controller = controller;
        this.moduleData = moduleData;
        this.name = name;
        executor = new Executor(controller,name);
    }

    public CommandController getController() {
        return controller;
    }

    public MiscModuleData getModuleData() {
        return moduleData;
    }

    public String getName() {
        return name;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }
}
