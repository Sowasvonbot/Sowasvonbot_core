package core.guild.modules;

import core.guild.Module;

public abstract class ModuleAPI {

    private CommandController commandController;
    private MiscModuleData miscModuleData;
    private String name;



    public ModuleAPI(){

    }

    public Module getModule(){
        return new Module(commandController,miscModuleData,name);
    }


    public CommandController getCommandController() {
        return commandController;
    }

    public void setCommandController(CommandController commandController) {
        this.commandController = commandController;
    }

    public MiscModuleData getMiscModuleData() {
        return miscModuleData;
    }

    public void setMiscModuleData(MiscModuleData miscModuleData) {
        this.miscModuleData = miscModuleData;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
