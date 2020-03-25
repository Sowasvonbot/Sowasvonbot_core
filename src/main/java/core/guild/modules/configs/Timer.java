package core.guild.modules.configs;

import botcore.Bot;
import net.dv8tion.jda.api.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Timer {

    private int idleMinutes;
    private User user;
    private final ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
    private ConfigListener listener;
    private ConfigController myController;
    private final int idleTime = 10;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public Timer(User user, ConfigListener listener, ConfigController myController) {
        this.user = user;
        this.listener = listener;
        this.myController = myController;

        logger.info("Timer start");
        timer.scheduleAtFixedRate(raiseCounter(), 0, 1, TimeUnit.MINUTES);
    }


    protected void reset() {
        idleMinutes = 0;
    }

    private Runnable raiseCounter() {
        return () -> {

            logger.info("Timer runnning");
            idleMinutes++;
            if (idleMinutes >= idleTime) {
                user.openPrivateChannel().queue((channel) -> channel.sendMessage("Es sind " + idleTime + " Minuten vergangen. Diese Session wird geschlossen." +
                        "\nUm mehr zu konfigurieren bitte eine neue Session öffnen").queue());
                Bot.removeListener(listener);
                myController = null;
                timer.shutdownNow();
            }
        };


    }

}
