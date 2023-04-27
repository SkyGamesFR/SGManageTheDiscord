package fr.skygames.managethediscord.listeners;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import core.GLA;
import fr.skygames.managethediscord.lavaplayer.GuildMusicManager;
import fr.skygames.managethediscord.lavaplayer.PlayerManager;
import fr.skygames.managethediscord.utils.embeds.MusicEB;
import genius.SongSearch;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.io.IOException;
import java.util.Objects;

public class ButtonListener extends ListenerAdapter {

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if(event.getComponentId().equals("pause")) {
            GuildMusicManager guildMusicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());

            if(guildMusicManager.scheduler.player.getPlayingTrack() == null) {
                MusicEB musicEB = new MusicEB();
                musicEB.getBuilder().setDescription("Il n'y a pas de musique en cours de lecture.");
                event.replyEmbeds(musicEB.getBuilder().build()).setActionRow(
                        Button.link("https://skygames.fr", "SkyGames").withEmoji(Emoji.fromUnicode("✨"))
                ).queue();
                return;
            }

            guildMusicManager.scheduler.player.setPaused(!guildMusicManager.scheduler.player.isPaused());
            MusicEB musicEB = new MusicEB();
            musicEB.getBuilder().addField("Musique actuelle", guildMusicManager.scheduler.player.getPlayingTrack().getInfo().title, false);
            if(guildMusicManager.scheduler.player.isPaused()) {
                musicEB.getBuilder().setDescription("La musique en cours a été mise en pause.");
                musicEB.getBuilder().addField("Mis en pause par", event.getMember().getAsMention(), false);
            }else {
                musicEB.getBuilder().setDescription("La musique en cours a été mise en pause.");
                musicEB.getBuilder().addField("Désactiver la pause par", event.getMember().getAsMention(), false);
            }
            event.editMessageEmbeds(musicEB.getBuilder().build()).setActionRow(musicEB.getActionRow()).queue();
        } else if (event.getComponentId().equals("stop")) {
            MusicEB musicEB = new MusicEB();
            if(!event.getMember().getVoiceState().inAudioChannel()){
                musicEB.getBuilder().setDescription("Vous devez être dans un Channel vocal pour que cette commande fonctionne..");
                event.replyEmbeds(musicEB.getBuilder().build()).setEphemeral(true).queue();
                return;
            }

            if(!event.getGuild().getSelfMember().getVoiceState().inAudioChannel()){
                musicEB.getBuilder().setDescription("Je dois être dans un canal vocal ou je dois jouer de la musique pour que cette commande fonctionne..");
                event.replyEmbeds(musicEB.getBuilder().build()).setEphemeral(true).queue();
                return;
            }

            if(Objects.equals(event.getMember().getVoiceState().getChannel(), event.getGuild().getSelfMember().getVoiceState().getChannel())){
                PlayerManager.getInstance().getMusicManager(event.getGuild()).scheduler.player.stopTrack();
                PlayerManager.getInstance().getMusicManager(event.getGuild()).scheduler.queue.clear();
                event.getGuild().getAudioManager().closeAudioConnection();
                musicEB.getBuilder().setDescription("Le lecteur a été arrêté et la file d'attente a été vidée.");
                musicEB.getBuilder().addField("Arrêtée par", event.getMember().getAsMention(), false);
                event.editMessageEmbeds(musicEB.getBuilder().build()).setActionRow(
                        Button.link("https://skygames.fr", "SkyGames").withEmoji(Emoji.fromUnicode("✨"))
                ).queue();
            }
        } else if (event.getComponentId().equals("skip")) {
            final Member self = event.getGuild().getSelfMember();
            final GuildVoiceState selfVoiceState = self.getVoiceState();

            MusicEB musicEB = new MusicEB();

            if(selfVoiceState == null || !selfVoiceState.inAudioChannel()){
                musicEB.getBuilder().setDescription("Je dois être dans un Channel vocal pour que cela fonctionne..");
                event.replyEmbeds(musicEB.getBuilder().build()).setEphemeral(true).queue();
                return;
            }

            final GuildVoiceState memberVoiceState = event.getMember().getVoiceState();

            if(!memberVoiceState.inAudioChannel()){
                musicEB.getBuilder().setDescription("Vous devez être dans un Channel vocal pour que cette commande fonctionne..");
                event.replyEmbeds(musicEB.getBuilder().build()).setEphemeral(true).queue();
                return;
            }

            if(!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())){
                musicEB.getBuilder().setDescription("Vous devez être dans le même Channel vocal que moi pour que cela fonctionne.");
                event.replyEmbeds(musicEB.getBuilder().build()).setEphemeral(true).queue();
                return;
            }

            final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
            final AudioPlayer audioPlayer = musicManager.audioPlayer;

            if(audioPlayer.getPlayingTrack() == null){
                musicEB.getBuilder().setDescription("Aucune piste n'est en cours de lecture.");
                event.replyEmbeds(musicEB.getBuilder().build()).setEphemeral(true).queue();
                return;
            }

            musicManager.scheduler.nextTrack();

            musicEB.getBuilder().setDescription("Sauter la musique en cours.");
            musicEB.getBuilder().addField("En cours de lecture", musicManager.audioPlayer.getPlayingTrack().getInfo().title, false);
            musicEB.getBuilder().addField("Par", musicManager.audioPlayer.getPlayingTrack().getInfo().author, false);
            musicEB.getBuilder().addField("Sauté par", event.getMember().getAsMention(), false);

            event.editMessageEmbeds(musicEB.getBuilder().build()).setActionRow(musicEB.getActionRow()).queue();
        } else if (event.getComponentId().equals("lyrics")) {
            final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
            final AudioPlayer audioPlayer = musicManager.audioPlayer;

            MusicEB musicEB = new MusicEB();

            if(audioPlayer.getPlayingTrack() == null){
                musicEB.getBuilder().setDescription("Il n'y a pas de musique en cours de lecture.");
                event.replyEmbeds(musicEB.getBuilder().build()).setEphemeral(true).queue();
                return;
            }

            String title = audioPlayer.getPlayingTrack().getInfo().title;

            GLA gla = new GLA();
            try {

                SongSearch search = gla.search(title.toLowerCase().replaceAll("official", "").replaceAll("music", "").replaceAll("video", "").replaceAll("audio", ""));

                String url = search.getHits().isEmpty() ? "" : search.getHits().getFirst().getUrl();
                if(url.isEmpty()){
                    musicEB.getBuilder().setDescription("Désolé, je n'ai pas trouvé de paroles pour cette chanson.");
                    event.replyEmbeds(musicEB.getBuilder().build()).setEphemeral(true).queue();
                    return;
                }
                musicEB.getBuilder().setDescription("Voici ce que j'ai trouvé: " + url);
                event.replyEmbeds(musicEB.getBuilder().build()).setEphemeral(true).queue();
            } catch (IOException e) {
                musicEB.getBuilder().setDescription("Désolé, je n'ai pas trouvé de paroles pour cette chanson.");
                event.replyEmbeds(musicEB.getBuilder().build()).setEphemeral(true).queue();
                e.printStackTrace();
            }
        }
    }
}
