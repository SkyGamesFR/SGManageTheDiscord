package fr.skygames.managethediscord.commands.mod;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;


public class Clear extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if(event.getInteraction().getName().equalsIgnoreCase("clear")) {
            if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.MESSAGE_MANAGE)){
                int amount = event.getOptions().get(0).getAsInt();
                final EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setColor(Color.GREEN);
                embedBuilder.setFooter("SkyGames | " + new SimpleDateFormat("'le' dd/MM/yyyy '\u00E0' kk:mm:ss").format(new Date()));

                if (amount > 100){
                    embedBuilder.setTitle("Erreur");
                    embedBuilder.setDescription("Vous ne pouvez pas supprimer plus de cent messages.");
                }else{
                    embedBuilder.setTitle("Erreur");
                    embedBuilder.setDescription("Référez-vous a la commande help.");
                }

                event.getChannel().getHistory().retrievePast((int) amount).queue(
                        (List<Message> messages) -> {
                            event.getChannel().asTextChannel().deleteMessages(messages).queue();
                        }
                );

                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
            }
        }
    }
}
