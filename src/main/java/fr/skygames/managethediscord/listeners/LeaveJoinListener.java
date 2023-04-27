package fr.skygames.managethediscord.listeners;

import fr.skygames.managethediscord.utils.Constants;
import fr.skygames.managethediscord.utils.EmbedUtils;
import fr.skygames.managethediscord.utils.files.GlobalProperties;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class LeaveJoinListener extends ListenerAdapter {

    private final GlobalProperties config;

    public LeaveJoinListener(GlobalProperties config) {
        super();
        this.config = config;
    }

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        event.getGuild().addRoleToMember(event.getMember(), Objects.requireNonNull(event.getGuild().getRoleById("972124392475738152"))).queue();
        if (!event.getUser().isBot()) {
            final EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setThumbnail(event.getUser().getAvatarUrl() == null ? event.getUser().getDefaultAvatarUrl() : event.getUser().getAvatarUrl());
            embedBuilder.setTitle("Bienvenue \u00E0 " + event.getUser().getName());
            embedBuilder.setColor(Color.GREEN);
            embedBuilder.addField("Merci d'avoir rejoint et de faire grandir la communaut\u00E9 <3", "", false);
            embedBuilder.setFooter(Constants.SERVER_NAME + " | " + new SimpleDateFormat("'le' dd/MM/yyyy '\u00E0' kk:mm:ss").format(new Date()));
            Objects.requireNonNull(event.getGuild().getTextChannelById("972125539177492571")).sendMessageEmbeds(embedBuilder.build()).queue();
        }

        Constants.updateMemberCount(event);
    }

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        if (!event.getUser().isBot()) {
            final EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setThumbnail(event.getUser().getAvatarUrl() == null ? event.getUser().getDefaultAvatarUrl() : event.getUser().getAvatarUrl());
            embedBuilder.setTitle("Au revoir \u00E0 " + event.getUser().getName());
            embedBuilder.setColor(Color.RED);
            embedBuilder.addField("Merci d'avoir fait parti de la communaut\u00E9 :(", "", false);
            embedBuilder.setFooter(Constants.SERVER_NAME + " | " + new SimpleDateFormat("'le' dd/MM/yyyy '\u00E0' kk:mm:ss").format(new Date()));
            Objects.requireNonNull(event.getGuild().getTextChannelById("972125539177492571")).sendMessageEmbeds(embedBuilder.build()).queue();
        }

        Constants.updateMemberCount(event);
    }
}
