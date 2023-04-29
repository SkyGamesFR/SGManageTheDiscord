package fr.skygames.managethediscord.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.skygames.managethediscord.utils.Constants;
import fr.skygames.managethediscord.utils.embeds.MusicEB;
import fr.skygames.managethediscord.utils.embeds.ProgressBar;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PlayerManager {

    private static PlayerManager INSTANCE;
    private final Map<Long, GuildMusicManager> musicManagers;
    private final AudioPlayerManager audioPlayerManager;

    public PlayerManager(){
        this.musicManagers = new HashMap<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();

        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);
    }

    public GuildMusicManager getMusicManager(Guild guild){
        return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
            final GuildMusicManager guildMusicManager = new GuildMusicManager(this.audioPlayerManager);
            guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());
            return guildMusicManager;
        });
    }

    public void loadAndPlay(TextChannel textChannel, SlashCommandInteraction data, String trackUrl){
        final GuildMusicManager musicManager = this.getMusicManager(textChannel.getGuild());
        this.audioPlayerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                musicManager.scheduler.queue(audioTrack);
                ProgressBar progressBar = new ProgressBar();
                MusicEB musicEB = new MusicEB();
                musicEB.getBuilder().setDescription("Une musique a été ajouté a la file d'attente.");
                musicEB.getBuilder().addField("Musique", audioTrack.getInfo().title, false);
                musicEB.getBuilder().addField("Auteur", audioTrack.getInfo().author, false);
                musicEB.getBuilder().addField("Durée", Constants.millisecondsToTime(audioTrack.getDuration()), false);

                System.out.println("AZERTY " + audioTrack.getPosition() + " " + audioTrack.getDuration());

                musicEB.getBuilder().addField(" ", progressBar.updateMusic(audioTrack.getDuration(), audioTrack.getPosition()), false);
                musicEB.getBuilder().addField("Ajouté par", Objects.requireNonNull(data.getMember()).getAsMention(), false);
                data.getHook().getInteraction().getMessageChannel().sendMessageEmbeds(musicEB.getBuilder().build()).addActionRow(musicEB.getActionRow()).queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                final List<AudioTrack> tracks = audioPlaylist.getTracks();
                if(!tracks.isEmpty()){
                    musicManager.scheduler.queue(tracks.get(0));
                    MusicEB musicEB = new MusicEB();
                    musicEB.getBuilder().setDescription("Une playlist a été ajouté a la file d'attente.");
                    musicEB.getBuilder().addField("Nom", String.valueOf(tracks.size()), false);
                    musicEB.getBuilder().addField("Auteur", audioPlaylist.getName(), false);
                    musicEB.getBuilder().addField("Ajouté par", Objects.requireNonNull(data.getMember()).getAsMention(), false);
                    data.getHook().getInteraction().getMessageChannel().sendMessageEmbeds(musicEB.getBuilder().build()).addActionRow(musicEB.getActionRow()).queue();
                }
            }

            @Override
            public void noMatches() {
                MusicEB musicEB = new MusicEB();
                musicEB.getBuilder().setDescription("Impossible de trouver la musique.");
                data.getHook().getInteraction().getMessageChannel().sendMessageEmbeds(musicEB.getBuilder().build()).addActionRow(
                        Button.link("https://skygames.fr", "SkyGames").withEmoji(Emoji.fromUnicode("✨"))
                ).queue();
            }

            @Override
            public void loadFailed(FriendlyException e) {
                MusicEB musicEB = new MusicEB();
                musicEB.getBuilder().setDescription("Impossible de charger la musique.");
                data.getHook().getInteraction().getMessageChannel().sendMessageEmbeds(musicEB.getBuilder().build()).addActionRow(
                        Button.link("https://skygames.fr", "SkyGames").withEmoji(Emoji.fromUnicode("✨"))
                ).queue();
            }

        });
    }

    public static PlayerManager getInstance(){
        if(INSTANCE == null) {
            INSTANCE = new PlayerManager();
        }
        return INSTANCE;
    }

}