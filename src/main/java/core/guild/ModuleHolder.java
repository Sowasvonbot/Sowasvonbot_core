package core.guild;

import core.guild.modules.CommandController;

import java.util.ArrayList;
import java.util.List;

public class ModuleHolder {


    private List<Module> moduleList;

    public ModuleHolder() {
        moduleList = new ArrayList<>();
    }

    public Module addModule(Module module){
        moduleList.add(module);
        return module;
    }

    public List<Module> getModuleList() {
        return moduleList;
    }

    public List<String> getModuleNamesToLowerCase(){
        List<String> names = new ArrayList<>();
        moduleList.forEach((module -> names.add(module.getName().toLowerCase())));
        return names;
    }

    public CommandController getCommandControllerByName(String name){
        for (Module module: moduleList) {
            if (module.getName().toLowerCase().contains(name.toLowerCase())) return module.getController();
        }
        return null;
    }
}
