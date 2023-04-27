package fr.skygames.managethediscord.commands.music;

import fr.skygames.managethediscord.lavaplayer.AudioPlayerSendHandler;
import fr.skygames.managethediscord.lavaplayer.GuildMusicManager;
import fr.skygames.managethediscord.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class VolumeCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if(event.getName().equals("volume")) {
            GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(Objects.requireNonNull(event.getGuild()));
            int volume = Objects.requireNonNull(event.getOption("volume")).getAsInt();

            musicManager.audioPlayer.setVolume(volume);
            event.replyFormat("Le volume a été réglé sur `%d`", volume).queue();
        }
    }
}
