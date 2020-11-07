package main.core;

import java.util.ArrayList;
import java.util.List;

public class GuildHolder {


    private List<Guild> guilds;

    public GuildHolder() {
        guilds = new ArrayList<>();
    }

    public GuildHolder(List<Guild> guilds) {
        if (!guilds.isEmpty()) this.guilds = guilds;
        else this.guilds = new ArrayList<>();
    }

    public List<Guild> getGuilds() {
        return guilds;
    }

    public Guild addGuild(Guild guild){
        guilds.add(guild);
        return guild;
    }

    public Guild getGuildAt(int index){
        if (index < guilds.size())  return guilds.get(index);
        return null;
    }

    public Guild getGuildWithID(long guildID){
        for (Guild guild:guilds) if(guild.getGuildID() == guildID) return guild;
        return null;
    }

}
