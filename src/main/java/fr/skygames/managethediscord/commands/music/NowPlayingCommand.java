package fr.skygames.managethediscord.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import fr.skygames.managethediscord.lavaplayer.GuildMusicManager;
import fr.skygames.managethediscord.lavaplayer.PlayerManager;
import fr.skygames.managethediscord.utils.embeds.MusicEB;
import fr.skygames.managethediscord.utils.embeds.ProgressBar;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class NowPlayingCommand extends ListenerAdapter {

    private final ProgressBar progressBar = new ProgressBar();

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getInteraction().getName().equalsIgnoreCase("nowplaying")) {
            final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(Objects.requireNonNull(event.getGuild()));
            final AudioPlayer audioPlayer = musicManager.audioPlayer;

            if(audioPlayer.getPlayingTrack() == null){
                event.reply("Aucune musique est en cours.").queue();
                return;
            }

            String title = audioPlayer.getPlayingTrack().getInfo().title;
            String author = audioPlayer.getPlayingTrack().getInfo().author;

            MusicEB musicEB = new MusicEB();
            musicEB.getBuilder().setDescription("Musique actuelle:");
            musicEB.getBuilder().addField("Musique", title, false);
            musicEB.getBuilder().addField("Auteur", author, false);
            musicEB.getBuilder().addField("Durée", Objects.requireNonNull(fr.skygames.managethediscord.utils.Constants.millisecondsToTime(audioPlayer.getPlayingTrack().getDuration())), false);
            musicEB.getBuilder().addField(" ", progressBar.updateMusic(audioPlayer.getPlayingTrack().getDuration(), audioPlayer.getPlayingTrack().getPosition()), false);
            musicEB.getBuilder().addField("Ajouté par", Objects.requireNonNull(event.getMember()).getAsMention(), false);

            event.replyEmbeds(musicEB.getBuilder().build()).queue();
        }
    }
}
