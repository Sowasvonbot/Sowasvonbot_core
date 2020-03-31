# Sowasvonbot

This is the core of my own Discord Bot.

The idea of this project is a module based Bot, so I'm able to develop modules and functionality for this Bot in other projects.

If you want to develop your own module and use this bot, so you don't need to write things like config procedures or multi-server support, then just clone this repo or import the project from Maven Central:

namespace: com.github.sowasvonbot \
project: sowasvonbot-core \
version: 0.0.2

Now you only need to extend from ModuleApi and implement the interfaces CommandController and MiscModuleData \
These are my interfaces to new modules

If you have done this, just register your modules and start the Bot in the main method with: \
BigDiscordBot.getInstance().registerModule(YourClassHere.class extends ModuleApi); \
BigDiscordBot.getInstance().startBot();

Otherwise start the Bot and a folder plugins will appear at project level. Copy your module as a jar in there.

Be aware if your jar has extra dependencies different from the bot ones, they have to be included in the jar.