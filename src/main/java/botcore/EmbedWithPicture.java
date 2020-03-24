package botcore;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.net.URL;

public class EmbedWithPicture {

    private EmbedBuilder embedBuilder;
    private URL picture;
    private URL thumbnail;


    public EmbedWithPicture(EmbedBuilder embedBuilder, URL picture, URL thumbnail) {
        this.embedBuilder = embedBuilder;
        this.picture = picture;
        this.thumbnail = thumbnail;
    }

    public EmbedBuilder getEmbedBuilder() {
        return embedBuilder;
    }

    public URL getPicture() {
        return picture;
    }

    public URL getThumbnail() {return thumbnail;}
}
