package fr.skygames.managethediscord.listeners;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import core.GLA;
import fr.skygames.managethediscord.commands.Help;
import fr.skygames.managethediscord.lavaplayer.GuildMusicManager;
import fr.skygames.managethediscord.lavaplayer.PlayerManager;
import fr.skygames.managethediscord.utils.Constants;
import fr.skygames.managethediscord.utils.embeds.MusicEB;
import genius.SongSearch;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

public class ButtonListener extends ListenerAdapter {

    private final MusicEB musicEB = new MusicEB();
    private final Help help = new Help();

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        help.onSlashCommand(event);
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {

        help.onButtonInteraction(event);

        switch (event.getComponentId()) {
            case "pause":
                GuildMusicManager guildMusicManager = PlayerManager.getInstance().getMusicManager(Objects.requireNonNull(event.getGuild()));

                if(guildMusicManager.scheduler.player.getPlayingTrack() == null) {
                    musicEB.getBuilder().setDescription("Il n'y a pas de musique en cours de lecture.");
                    event.replyEmbeds(musicEB.getBuilder().build()).setActionRow(
                            Button.link("https://skygames.fr", "SkyGames").withEmoji(Emoji.fromUnicode("✨"))
                    ).queue();
                    return;
                }

                guildMusicManager.scheduler.player.setPaused(!guildMusicManager.scheduler.player.isPaused());
                musicEB.getBuilder().addField("Musique actuelle", guildMusicManager.scheduler.player.getPlayingTrack().getInfo().title, false);
                if(guildMusicManager.scheduler.player.isPaused()) {
                    musicEB.getBuilder().setDescription("La musique en cours a été mise en pause.");
                    musicEB.getBuilder().addField("Mis en pause par", Objects.requireNonNull(event.getMember()).getAsMention(), false);
                }else {
                    musicEB.getBuilder().setDescription("La musique en cours a été mise en pause.");
                    musicEB.getBuilder().addField("Désactiver la pause par", Objects.requireNonNull(event.getMember()).getAsMention(), false);
                }
                event.editMessageEmbeds(musicEB.getBuilder().build()).setActionRow(musicEB.getActionRow()).queue();
                break;
            case "stop":
                if(!Objects.requireNonNull(Objects.requireNonNull(event.getMember()).getVoiceState()).inAudioChannel()){
                    musicEB.getBuilder().setDescription("Vous devez être dans un Channel vocal pour que cette commande fonctionne..");
                    event.replyEmbeds(musicEB.getBuilder().build()).setEphemeral(true).queue();
                    return;
                }

                if(!Objects.requireNonNull(Objects.requireNonNull(event.getGuild()).getSelfMember().getVoiceState()).inAudioChannel()){
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
                break;
            case "skip":
                final Member self = Objects.requireNonNull(event.getGuild()).getSelfMember();
                final GuildVoiceState selfVoiceState = self.getVoiceState();

                if(selfVoiceState == null || !selfVoiceState.inAudioChannel()){
                    musicEB.getBuilder().setDescription("Je dois être dans un Channel vocal pour que cela fonctionne..");
                    event.replyEmbeds(musicEB.getBuilder().build()).setEphemeral(true).queue();
                    return;
                }

                final GuildVoiceState memberVoiceState = Objects.requireNonNull(event.getMember()).getVoiceState();

                assert memberVoiceState != null;
                if(!memberVoiceState.inAudioChannel()){
                    musicEB.getBuilder().setDescription("Vous devez être dans un Channel vocal pour que cette commande fonctionne..");
                    event.replyEmbeds(musicEB.getBuilder().build()).setEphemeral(true).queue();
                    return;
                }

                if(!Objects.equals(memberVoiceState.getChannel(), selfVoiceState.getChannel())){
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
                break;
            case "lyrics":
                final GuildMusicManager musicManager2 = PlayerManager.getInstance().getMusicManager(Objects.requireNonNull(event.getGuild()));
                final AudioPlayer audioPlayer2 = musicManager2.audioPlayer;

                MusicEB musicEB = new MusicEB();

                if(audioPlayer2.getPlayingTrack() == null){
                    musicEB.getBuilder().setDescription("Il n'y a pas de musique en cours de lecture.");
                    event.replyEmbeds(musicEB.getBuilder().build()).setEphemeral(true).queue();
                    return;
                }

                String title = audioPlayer2.getPlayingTrack().getInfo().title;

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
                break;
            case "sigma":
                if (Constants.hasRole(Objects.requireNonNull(event.getGuild()), event.getUser().getId(), Constants.SIGMA_ROLE)) {
                    event.getGuild().removeRoleFromMember(Objects.requireNonNull(event.getMember()), Objects.requireNonNull(event.getGuild().getRoleById(Constants.SIGMA_ROLE))).queue();
                } else {
                    event.getGuild().addRoleToMember(Objects.requireNonNull(event.getMember()), Objects.requireNonNull(event.getGuild().getRoleById(Constants.SIGMA_ROLE))).queue();

                }
                break;
            case "tau":
                if (Constants.hasRole(Objects.requireNonNull(event.getGuild()), event.getUser().getId(), Constants.TAU_ROLE)) {
                    event.getGuild().removeRoleFromMember(Objects.requireNonNull(event.getMember()), Objects.requireNonNull(event.getGuild().getRoleById(Constants.TAU_ROLE))).queue();
                } else {
                    event.getGuild().addRoleToMember(Objects.requireNonNull(event.getMember()), Objects.requireNonNull(event.getGuild().getRoleById(Constants.TAU_ROLE))).queue();
                }
                break;
            case "upsilon":
                if (Constants.hasRole(Objects.requireNonNull(event.getGuild()), event.getUser().getId(), Constants.UPSILON_ROLE)) {
                    event.getGuild().removeRoleFromMember(Objects.requireNonNull(event.getMember()), Objects.requireNonNull(event.getGuild().getRoleById(Constants.UPSILON_ROLE))).queue();
                } else {
                    event.getGuild().addRoleToMember(Objects.requireNonNull(event.getMember()), Objects.requireNonNull(event.getGuild().getRoleById(Constants.UPSILON_ROLE))).queue();
                }
                break;
            case "phi":
                if (Constants.hasRole(Objects.requireNonNull(event.getGuild()), event.getUser().getId(), Constants.PHI_ROLE)) {
                    event.getGuild().removeRoleFromMember(Objects.requireNonNull(event.getMember()), Objects.requireNonNull(event.getGuild().getRoleById(Constants.PHI_ROLE))).queue();
                } else {
                    event.getGuild().addRoleToMember(Objects.requireNonNull(event.getMember()), Objects.requireNonNull(event.getGuild().getRoleById(Constants.PHI_ROLE))).queue();
                }
                break;
            default:
                break;
        }
    }
}
