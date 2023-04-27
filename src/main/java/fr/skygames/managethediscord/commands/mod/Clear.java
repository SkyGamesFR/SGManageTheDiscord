package fr.skygames.managethediscord.commands.mod;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
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
                String[] commandArgs = Objects.requireNonNull(event.getOption("amount")).getAsString().split(" ");
                final EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setColor(Color.GREEN);
                embedBuilder.setFooter("SkyGames | " + new SimpleDateFormat("'le' dd/MM/yyyy '\u00E0' kk:mm:ss").format(new Date()));

                if (1 < commandArgs.length){
                    int amount = Integer.parseInt(commandArgs[1]);

                    if (amount < 1){
                        embedBuilder.setTitle("Erreur");
                        embedBuilder.setDescription("Vous ne pouvez pas supprimer moins d'un message.");
                    }else if (amount > 100){
                        embedBuilder.setTitle("Erreur");
                        embedBuilder.setDescription("Vous ne pouvez pas supprimer plus de cent messages.");
                    }else{
                        List<Message> msgs = event.getChannel().getHistory().retrievePast(amount).complete();
                        event.getChannel().purgeMessages(msgs);
                        embedBuilder.setTitle("Succès");
                        embedBuilder.setDescription(String.format("Vous avez bien supprimé %s messages !", amount));
                    }
                }else{
                    embedBuilder.setTitle("Erreur");
                    embedBuilder.setDescription("Référez-vous a la commande help.");
                }
                event.getTextChannel().sendMessageEmbeds(embedBuilder.build()).queue();
            }
        }
    }
}
