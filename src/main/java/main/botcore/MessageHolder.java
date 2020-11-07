package main.botcore;

import net.dv8tion.jda.api.entities.Message;

public class MessageHolder {

    private Message message;


    public void setMessage(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    @Override
    public String toString() {
        if (message == null) return "null";
        return "MessageContent: " +  message.getContentRaw() + "\nID: " + message.getIdLong();
    }
}
