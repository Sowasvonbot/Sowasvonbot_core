package botcore;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * This class is responsible for the sending and editing of messages to Discord.
 * Most of the methods are non-blocking.
 */
public abstract class Output {


    private final static Logger logger = LoggerFactory.getLogger("Output");


    /**
     * Sending a message, build from a String, to the given text channel ID.
     * Returns true, when the operation was successful. This method is performed async, so non-blocking
     * @param channelID The channel ID as long
     * @param message A string to be sent to the channel.
     * @return true, if successful sent.
     */
    public static boolean sendMessageToChannel(long channelID, String message){
        Message messageObject = new  MessageBuilder().append(message).build();
        return sendMessageToChannel(channelID,messageObject);
    }

    /**
     * Sending a message with a given message object
     * {@link Message Message}
     * to the given text channel ID. This method is performed async, so non-blocking
     * Returns true, when the message was sent.
     * @param channelID The text channel ID as long
     * @param message The message instance from {@link Message Message}
     * @return True, if successful sent.
     */
    public static boolean sendMessageToChannel(long channelID, Message message){return sendMessageToChannel(channelID, message, null);}

    /**
     * Sending an embed message to the given text channel ID
     * {@link MessageEmbed MessageEmbed} .This method is performed async, so non-blocking
     * @param channelID The text channel ID as long
     * @param message The embed message. Must be already builded
     * @return True, if successful sent.
     */
    public static boolean sendMessageToChannel(long channelID, MessageEmbed message){return sendMessageToChannel(channelID, message, null);}

    /**
     * Sending an
     * {@link EmbedWithPicture embed with picture}
     * to the given text channel ID. \n This method is performed async, so non-blocking
     * @param channelID the channel ID as long
     * @param message The embed with picture
     * @return True, if successfully sent.
     */
    public static boolean sendMessageToChannel(long channelID, EmbedWithPicture message){return sendMessageToChannel(channelID, message, null);}


    /**
     * Basically the same like {@link Output#sendMessageToChannel(long, Message)} \n
     * Just with the {@link MessageHolder MessageHolder} at the end. Use this method to receive data from the sent message. \n
     * The disadvantage from this method is the blocking of the current thread. \n
     * The data of the message will be in the MessageHolder
     * @param channelID The text channel ID as long
     * @param message The message instance from {@link Message Message}
     * @param messageHolder The holder of the returned message data.
     * @return True, if successful sent.
     */
    public static boolean sendMessageToChannel(long channelID, Message message, MessageHolder messageHolder){
        try {
            return sendMessageAction(
                    Bot.getInstance().getMyJDA().getTextChannelById(channelID).sendMessage(message),
                    messageHolder);
        } catch (NullPointerException nullPointer){
            logger.info(nullPointer.getMessage());
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Basically the same like {@link Output#sendMessageToChannel(long, MessageEmbed)} \n
     * Just with the {@link MessageHolder MessageHolder} at the end. Use this method to receive data from the sent message. \n
     * The disadvantage from this method is the blocking of the current thread. \n
     * The data of the message will be in the MessageHolder
     * @param channelID The text channel ID as long
     * @param message The MessageEmbed instance from {@link MessageEmbed MessageEmbed}
     * @param messageHolder The holder of the returned message data.
     * @return True, if successful sent.
     */
    public static boolean sendMessageToChannel(long channelID, MessageEmbed message, MessageHolder messageHolder){
        try {
            return sendMessageAction(
                    Bot.getInstance().getMyJDA().getTextChannelById(channelID).sendMessage(message),
                    messageHolder);
        } catch (NullPointerException nullPointer){
            logger.info(nullPointer.getMessage());
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Basically the same like {@link Output#sendMessageToChannel(long, EmbedWithPicture)} \n
     * Just with the {@link MessageHolder MessageHolder} at the end. Use this method to receive data from the sent message. \n
     * The disadvantage from this method is the blocking of the current thread. \n
     * The data of the message will be in the MessageHolder
     * @param channelID The text channel ID as long
     * @param message The EmbedMessage instance from {@link EmbedWithPicture EmbedWithPicture}
     * @param messageHolder The holder of the returned message data.
     * @return True, if successful sent.
     */
    public static boolean sendMessageToChannel(long channelID, EmbedWithPicture message, MessageHolder messageHolder) {
        try{
            return sendMessageAction(buildMessage(channelID,message), messageHolder);
        } catch (Exception e){
            logger.info(e.getMessage());
            return false;
        }
    }

    private static boolean sendMessageAction(MessageAction action, MessageHolder holder){
        if(holder == null) {action.queue(); return true;}
        try {
            Message message = action.complete();
            holder.setMessage(message);
            return true;
        } catch (Exception e){logger.warn(e.getMessage()); return false;}
    }

    private static MessageAction buildMessage(long channelID, EmbedWithPicture message){
        try{
            MessageAction currAction = null;
            InputStream  file = null;
            InputStream thumbnail = null;

            TextChannel channel = Bot.getInstance().getMyJDA().getTextChannelById(channelID);
            EmbedBuilder embedBuilder = message.getEmbedBuilder();
            if(message.getPicture() != null) {
                file = message.getPicture().openStream();
                embedBuilder.setImage("attachment://picture.jpg");
            }

            if (message.getThumbnail() != null){
                thumbnail = message.getThumbnail().openStream();
                embedBuilder.setThumbnail("attachment://thumbnail.jpg");
            }
            if (file != null) currAction =  channel.sendFile(file, "picture.jpg");

            if (currAction != null && thumbnail != null) currAction.addFile(thumbnail, "thumbnail.jpg");

            else if (currAction == null && thumbnail != null) currAction = channel.sendFile(thumbnail, "thumbnail.jpg");

            else if (currAction != null && thumbnail == null) {return channel.sendMessage(embedBuilder.build()); }

            return currAction.embed(embedBuilder.build());


        } catch (NullPointerException nullPointer){
            logger.info(nullPointer.getMessage());
            return null;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Deletes a message with the given ID in the given text channel. \n
     * This is performed a-sync, so non-blocking.
     * @param messageID The message to be deleted as long.
     * @param channelID The channel ID, in which the message is to be found.
     * @return True, if successfully deleted.
     */
    public static boolean deleteMessageByID(long messageID, long channelID){
        try {
            Bot.getInstance().getMyJDA().getTextChannelById(channelID).deleteMessageById(messageID).queue();
            return true;
        } catch (Exception e){
            logger.info("Tried to delete message: "+ messageID + " in Channel "+ channelID);
            return false;
        }
    }

    /**
     * Edit a message with given ID and the new content as {@link MessageEmbed embed}
     * This is performed a-sync, so non blocking
     * @param messageID The message ID as long.
     * @param channelID The channel ID, in which the message is to be found.
     * @param embed The new content as {@link MessageEmbed MessageEmbed}
     * @return True, if successfully edited.
     */
    public static boolean editEmbedMessageByID(long messageID, long channelID, MessageEmbed embed){
        try {
            TextChannel channel = Bot.getInstance().getMyJDA().getTextChannelById(channelID);
            channel.editMessageById(messageID, embed).queue();
            return true;
        } catch (Exception e){
            logger.info(e.getMessage());
            return false;
        }
    }
}
