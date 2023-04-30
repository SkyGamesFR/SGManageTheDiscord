package fr.skygames.managethediscord.listeners;

import fr.skygames.managethediscord.utils.Constants;
import fr.skygames.managethediscord.utils.embeds.EmbedUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;

import java.util.List;

public class AutoMod extends ListenerAdapter {

    private List<String> badWords;
    private final JDA jda;

    public AutoMod(List<String> badWords, JDA jda) {
        this.badWords = badWords;
        this.jda = jda;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot() || !event.isFromGuild()) {
            return;
        }

        Message message = event.getMessage();
        Guild guild = event.getGuild();
        TextChannel channel = event.getChannel().asTextChannel();
        String content = message.getContentRaw().toLowerCase();
        EmbedUtils embedUtils = new EmbedUtils();

        for (String word : badWords) {
            if (content.contains(word)) {
                message.delete().queue();

                String warningMessage = "Hey " + event.getAuthor().getAsMention() + ", veuillez vous abstenir d'utiliser ce langage dans ce serveur.";
                MessageCreateAction action = channel.sendMessageEmbeds(embedUtils.errorEmbed("AutoMod 🚓", warningMessage).build());

                action.queue(response -> {
                    response.delete().queueAfter(10, java.util.concurrent.TimeUnit.SECONDS);
                });

                String logMessage = event.getAuthor().getAsMention() + " a utilisé le mot \"" + word + "\" (" + message.getContentRaw() + ") sur " + guild.getName() + " (" + guild.getId() + ")";


                event.getGuild().getTextChannelById(Constants.LOG_CHANNEL_ID).sendMessageEmbeds(embedUtils.warningEmbed("AutoMod 🚓", logMessage).build()).queue();
                break;
            }
        }
    }
}