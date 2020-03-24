package core.guild.modules.commands;

import java.util.HashMap;

public class Command {

    private String name;
    private HashMap<String,String> values;
    private returnValues returnValue;
    private String description;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public returnValues getReturnValue() {
        return returnValue;
    }

    public HashMap<String, String> getValues() {
        return values;
    }

    public void setValues(HashMap<String, String> values) {
        this.values = values;
    }

    public void setReturnValue(returnValues returnValue) {
        this.returnValue = returnValue;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Set the name of the command, all parameters(values) and the returnValue from the enumList returnValues e.g. returnValues.INT for example
     * @param name name of the command
     * @param values HashMap of first Value, then Description
     * @param returnValue The possible return, e.g for the twitch api a valid client id
     */
    public Command(String name, HashMap<String, String> values, returnValues returnValue, String description) {
        this.name = name;
        this.values = values;
        this.returnValue = returnValue;
        this.description = description;
    }

    @Override
    public String toString() {
        String values = "No values";

        if (this.values != null) values = this.values.keySet().toString();

        return "Command:  " + name;

    }

    public enum returnValues{
        INT, STRING, FUTURE, LONG, BOOLEAN, VOID
    }
}
