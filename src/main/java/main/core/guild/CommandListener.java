package main.core.guild;

import main.botcore.Bot;
import main.core.guild.modules.commands.Executor;
import main.core.guild.modules.configs.ConfigController;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CommandListener extends ListenerAdapter {

    private long myGuildID;
    private Map<String, Executor> executorList;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private ExecutorService configService;

    public CommandListener(long myGuildID, HashMap<String, Executor> executorList) {
        super();
        this.myGuildID = myGuildID;
        this.executorList = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        this.executorList.putAll(executorList);
        configService = Executors.newSingleThreadExecutor();
        configService.submit(() -> Thread.currentThread().setName("config service"));
        Bot.addListener(this);

    }

    @Override
    public void onGuildMessageUpdate(@Nonnull GuildMessageUpdateEvent event) {
        handleInput(
                event.getAuthor(),
                event.getGuild(),
                event.getMessage()
                );
    }

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        handleInput(
                event.getAuthor(),
                event.getGuild(),
                event.getMessage()
        );
    }

    private void handleInput(User author, Guild guild, Message message){
        if (author.isBot() || guild.getIdLong() != myGuildID) return;


        String messageContent = message.getContentRaw();

        logger.debug("Incoming message {}", messageContent);


        String[] parameters = messageContent.split(" ");
        if (parameters.length == 0) return;

        if (message.getMentionedUsers().contains(Bot.getBotUser())){
            if (message.getContentRaw().toLowerCase().contains("config")){
                //TODO instance of new ConfigController(Executor, UserID, Guild
                configService.submit(() -> new ConfigController(author.getIdLong(),guild.getIdLong(), executorList));
            }
            return;
        }
        if (parameters[0].length() < 2 || parameters[0].charAt(0) != '!') return;
        String[] args = null;
        if (parameters.length >= 2) {
            args = new String[parameters.length -1];
            System.arraycopy(parameters, 1, args, 0, parameters.length - 1);
        }
        for (Map.Entry<String, Executor> entry :executorList.entrySet()) {
            entry.getValue().executeCommand(
                    parameters[0].replace("!",""),
                    args,
                    message.getChannel().getIdLong());
        }
    }

    public void shutdown(){
        logger.info("Shutdown of command listener");
        Bot.removeListener(this);
        executorList.values().forEach(executor -> executor.shutdown());
    }
}
