package fr.skygames.managethediscord.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import fr.skygames.managethediscord.lavaplayer.GuildMusicManager;
import fr.skygames.managethediscord.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SkipCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getInteraction().getName().equals("skip")) {
            final TextChannel channel = event.getChannel().asTextChannel();
            final GuildVoiceState selfVoiceState = Objects.requireNonNull(event.getGuild()).getSelfMember().getVoiceState();

            if (!selfVoiceState.inAudioChannel()) {
                channel.sendMessage("I need to be in a voice channel for this to work").queue();
                return;
            }

            final Member member = event.getMember();
            final GuildVoiceState memberVoiceState = member.getVoiceState();

            if (!memberVoiceState.inAudioChannel()) {
                channel.sendMessage("You need to be in a voice channel for this command to work").queue();
                return;
            }

            if (!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
                channel.sendMessage("You need to be in the same voice channel as me for this to work").queue();
                return;
            }

            final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
            final AudioPlayer audioPlayer = musicManager.audioPlayer;

            if (audioPlayer.getPlayingTrack() == null) {
                channel.sendMessage("There is no track playing currently").queue();
                return;
            }

            musicManager.scheduler.nextTrack();
            channel.sendMessage("Skipped the current track").queue();
        }
    }
}
