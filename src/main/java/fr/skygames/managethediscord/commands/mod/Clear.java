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
                embedBuilder.setFooter("SkyGames | " + new SimpleDateFormat("'le' dd/MM/yyyy '\u00E0' kk:mm:ss").format(new Date()));

                if (amount > 100 || amount <= 1){
                    embedBuilder.setTitle("Erreur");
                    embedBuilder.setColor(Color.RED);
                    embedBuilder.setDescription("Vous ne pouvez pas supprimer plus de 100 messages ou moins de 1 message.");
                }else{

                    embedBuilder.setTitle("Succ\u00E8s");
                    embedBuilder.setColor(Color.GREEN);
                    embedBuilder.setDescription("Vous avez supprim\u00E9 " + amount + " messages.");

                    event.getChannel().getHistory().retrievePast((int) amount).queue(
                            (List<Message> messages) -> {
                                event.getChannel().asTextChannel().deleteMessages(messages).queue();
                            }
                    );
                }
                event.getInteraction().replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();
            }
        }
    }
}
