package fr.skygames.managethediscord.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import fr.skygames.managethediscord.lavaplayer.GuildMusicManager;
import fr.skygames.managethediscord.lavaplayer.PlayerManager;
import fr.skygames.managethediscord.utils.embeds.MusicEB;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;


public class QueueCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("queue")) {

            final TextChannel channel = event.getChannel().asTextChannel();
            final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
            final BlockingQueue<AudioTrack> queue = musicManager.scheduler.queue;

            if (queue.isEmpty()) {
                event.reply("La file d'attente est vide").setEphemeral(true).queue();
                return;
            }

            final int trackCount = Math.min(queue.size(), 20);
            final List<AudioTrack> trackList = new ArrayList<>(queue);

            MusicEB musicEB = new MusicEB();
            musicEB.getBuilder().setDescription("Liste d'attente:");


            for (int i = 0; i <  trackCount; i++) {
                final AudioTrack track = trackList.get(i);
                final AudioTrackInfo info = track.getInfo();


                musicEB.getBuilder().addField("Musique", info.title, false);
                musicEB.getBuilder().addField("Auteur", info.author, false);
                musicEB.getBuilder().addField("Durée", formatTime(track.getDuration()), false);
            }

            if (trackList.size() > trackCount) {

                Integer trackListCount = trackList.size() - trackCount;
                musicEB.getBuilder().addField("", "Et `" + trackListCount + " ` en plus...", false);
            }

            event.replyEmbeds(musicEB.getBuilder().build()).queue();
        }
    }

    private String formatTime(long timeInMillis) {
        final long hours = timeInMillis / TimeUnit.HOURS.toMillis(1);
        final long minutes = timeInMillis / TimeUnit.MINUTES.toMillis(1);
        final long seconds = timeInMillis % TimeUnit.MINUTES.toMillis(1) / TimeUnit.SECONDS.toMillis(1);

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
