package fr.skygames.managethediscord.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import fr.skygames.managethediscord.lavaplayer.GuildMusicManager;
import fr.skygames.managethediscord.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;


public class QueueCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("queue")) {

            final TextChannel channel = event.getTextChannel();
            final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
            final BlockingQueue<AudioTrack> queue = musicManager.scheduler.queue;

            if (queue.isEmpty()) {
                channel.sendMessage("The queue is currently empty").queue();
                return;
            }

            final int trackCount = Math.min(queue.size(), 20);
            final List<AudioTrack> trackList = new ArrayList<>(queue);
            final MessageAction messageAction = channel.sendMessage("**Current Queue:**\n");

            for (int i = 0; i <  trackCount; i++) {
                final AudioTrack track = trackList.get(i);
                final AudioTrackInfo info = track.getInfo();

                messageAction.append('#')
                        .append(String.valueOf(i + 1))
                        .append(" `")
                        .append(String.valueOf(info.title))
                        .append(" by ")
                        .append(info.author)
                        .append("` [`")
                        .append(formatTime(track.getDuration()))
                        .append("`]\n");
            }

            if (trackList.size() > trackCount) {
                messageAction.append("And `")
                        .append(String.valueOf(trackList.size() - trackCount))
                        .append("` more...");
            }

            messageAction.queue();
        }
    }

    private String formatTime(long timeInMillis) {
        final long hours = timeInMillis / TimeUnit.HOURS.toMillis(1);
        final long minutes = timeInMillis / TimeUnit.MINUTES.toMillis(1);
        final long seconds = timeInMillis % TimeUnit.MINUTES.toMillis(1) / TimeUnit.SECONDS.toMillis(1);

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
