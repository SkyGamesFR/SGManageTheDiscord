package fr.skygames.managethediscord.commands.music;

import fr.skygames.managethediscord.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.URISyntaxException;

public class PlayCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("play")) {
            if (event.getInteraction().getOptions().isEmpty()) {
                event.reply("You need to specify a music. Usage: '/play <music>'").setEphemeral(true).queue();
                return;
            }

            if (!event.getInteraction().getMember().getVoiceState().inAudioChannel()) {
                event.reply("You must be in an audio channel to perform that command.").setEphemeral(true).queue();
                return;
            }

            event.deferReply();

            if (!event.getGuild().getSelfMember().getVoiceState().inAudioChannel()) {
                final AudioManager audioManager = event.getGuild().getAudioManager();
                final VoiceChannel memberChannel = (VoiceChannel) event.getMember().getVoiceState().getChannel();

                audioManager.openAudioConnection(memberChannel);
            }

            String link = event.getOptions().get(0).getAsString();

            if (!isUrl(link)) {
                link = "ytsearch:" + event.getOptions().get(0).getAsString() + " music";
            }
            try {
                PlayerManager.getInstance().loadAndPlay(event.getChannel().asTextChannel(), event, link);
                event.getHook().deleteOriginal().queue();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isUrl(String input) {
        try {
            new URI(input);
            return true;
        } catch (URISyntaxException e) {
            return false;
        }
    }
}
