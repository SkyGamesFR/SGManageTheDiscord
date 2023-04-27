package fr.skygames.managethediscord.utils.embeds;

import fr.skygames.managethediscord.utils.Constants;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MusicEB {

    private final EmbedBuilder builder;
    private final List<Button> actionRow;

    public MusicEB() {
        builder = new EmbedBuilder();
        builder.setTitle("Music Player");
        builder.setColor(new Color(166, 101, 186));
        builder.setFooter(Constants.SERVER_NAME);
        this.actionRow = new ArrayList<>();
        actionRow.add(Button.primary("pause", "Pause").withEmoji(Emoji.fromUnicode("⏸")));
        actionRow.add(Button.primary("skip", "Skip").withEmoji(Emoji.fromUnicode("⏭")));
        actionRow.add(Button.primary("lyrics", "Lyrics").withEmoji(Emoji.fromUnicode("📄")));
        actionRow.add(Button.danger("stop", "Stop").withEmoji(Emoji.fromUnicode("⏹")));
    }

    public EmbedBuilder getBuilder() {
        return builder;
    }

    public List<Button> getActionRow() {
        return actionRow;
    }
}