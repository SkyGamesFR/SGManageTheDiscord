package fr.skygames.managethediscord.commands.music;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class JoinCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if(event.getName().equals("join")) {
            final TextChannel channel = event.getChannel().asTextChannel();
            final GuildVoiceState selfVoiceState = Objects.requireNonNull(event.getGuild()).getSelfMember().getVoiceState();

            assert selfVoiceState != null;
            if (selfVoiceState.inAudioChannel()) {
                channel.sendMessage("Je suis deja dans un channel vocal").queue();
                return;
            }

            final Member member = event.getMember();
            assert member != null;
            final GuildVoiceState memberVoiceState = member.getVoiceState();

            assert memberVoiceState != null;
            if (!memberVoiceState.inAudioChannel()) {
                channel.sendMessage("Vous devez être dans un channel vocal afin d'executer cette commande").queue();
                return;
            }

            final AudioManager audioManager = event.getGuild().getAudioManager();
            final AudioChannel memberChannel = memberVoiceState.getChannel();

            audioManager.openAudioConnection(memberChannel);
            channel.sendMessageFormat("Connection a `\uD83D\uDD0A %s`", memberChannel.getName()).queue();
        }
    }
}
