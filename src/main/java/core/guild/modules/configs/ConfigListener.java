package core.guild.modules.configs;


import botcore.Bot;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
public class ConfigListener extends ListenerAdapter {


    private long clientID;
    private ConfigController configController;

    public ConfigListener(long clientID, ConfigController configController) {
        this.clientID = clientID;
        this.configController = configController;
        Bot.addListener(this);
    }

    @Override
    public void onPrivateMessageReceived(@Nonnull PrivateMessageReceivedEvent event) {
        if( event.getAuthor().getIdLong() != clientID) return;

        configController.incomingMessage(event.getMessage().getContentRaw());
    }
}


