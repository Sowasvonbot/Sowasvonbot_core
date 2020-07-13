package main.core.guild.modules;

import main.botcore.EmbedWithPicture;
import main.botcore.MessageHolder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class CommandReturn {

    private Message message;
    private MessageEmbed embed;
    private MessageHolder messageHolder;
    private EmbedWithPicture embedWithPicture;

    public CommandReturn(Message message, MessageHolder messageHolder) {
        this.message = message;
        this.messageHolder = messageHolder;
    }

    public CommandReturn(String content, MessageHolder messageHolder){
        this.messageHolder = messageHolder;
        this.message = new MessageBuilder().append(content).build();
    }

    public CommandReturn(MessageEmbed embed, MessageHolder messageHolder) {
        this.embed = embed;
        this.messageHolder = messageHolder;
    }

    public CommandReturn(EmbedWithPicture embedWithPicture, MessageHolder messageHolder) {
        this.embedWithPicture = embedWithPicture;
        this.messageHolder = messageHolder;
    }

    public CommandReturn(String content){
        this.message = new MessageBuilder().append(content).build();
    }

    public CommandReturn(Message message) {
        this.message = message;
    }

    public CommandReturn(MessageEmbed embed) {
        this.embed = embed;
    }

    public CommandReturn(EmbedWithPicture embedWithPicture) {
        this.embedWithPicture = embedWithPicture;
    }

    public MessageHolder getMessageHolder() {
        return messageHolder;
    }

    public CommandReturn setMessageHolder(MessageHolder messageHolder) {
        this.messageHolder = messageHolder;
        return this;
    }

    public Object getContent(){
        if (message != null) return message;
        if (embed != null) return embed;
        if (embedWithPicture != null) return embedWithPicture;
        return null;
    }
}
