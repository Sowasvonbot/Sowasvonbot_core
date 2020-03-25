package core;

import botcore.Bot;
import core.guild.modules.ModuleAPI;
import jar_handling.JarLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BigDiscordBot {

    private static List<Class<? extends ModuleAPI>> registeredModules;
    private static ExecutorService mainBotExecutor;


    private static BigDiscordBot instance;

    private BigDiscordBot(){
        registeredModules = new ArrayList<>();
        mainBotExecutor = Executors.newSingleThreadExecutor();
        mainBotExecutor.submit(()->Thread.currentThread().setName("main bot"));
    }


    public static BigDiscordBot getInstance(){
        if (instance == null) instance = new BigDiscordBot();
        return instance;
    }


    public void registerModule(Class<? extends  ModuleAPI> moduleAPI){registeredModules.add(moduleAPI);}

    public List<Class<? extends  ModuleAPI>> getRegisteredModules(){return registeredModules;}

    public void startBot(){
        mainBotExecutor.submit(()-> {
            GuildHandler guildHandler = GuildHandler.getInstance();
            setupScanner(guildHandler);
        });

    }

    public void clearModules(){
        registeredModules.clear();
    }

    private static void setupScanner(GuildHandler guildHandler){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() ->{
            Thread.currentThread().setName("Scanner");
            String line;
            Scanner in = new Scanner(System.in);
            while ((line = in.next())!= null){
                switch (line){
                    case "quit":
                    case "stop":
                    case "exit":
                        Bot.shutdown();
                        int exitCode = guildHandler.shutdown();
                        System.exit(exitCode);
                    case "reload":
                        System.out.println("Reloading guild");
                        mainBotExecutor.submit(()->GuildHandler.reloadGuildHandler());
                        break;
                    case "reloadJars":
                        mainBotExecutor.submit(()-> JarLoader.getInstance().reload());
                        break;
                    case "loadPlugins":
                        try {
                            JarLoader.getInstance().loadAllClasses();
                        } catch (IOException e){
                            e.printStackTrace();
                        }
                    default:
                        System.out.println("Don't know: " + line);
                }
            }
        });
    }

}
