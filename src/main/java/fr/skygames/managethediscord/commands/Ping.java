package fr.skygames.managethediscord.commands;

import fr.skygames.managethediscord.utils.embeds.EmbedUtils;
import fr.skygames.managethediscord.utils.embeds.ProgressBar;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Ping extends ListenerAdapter {

    private final ProgressBar progressBar = new ProgressBar();
    private long ping = 0;

    public Ping() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Future<Long> future = executorService.submit(() -> {
            long startTime = System.currentTimeMillis();
            long endTime = System.currentTimeMillis();
            return endTime - startTime;
        });

        try {
            long pingTime = future.get();
            ping = pingTime;

            progressBar.update(pingTime);

            executorService.shutdown();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getInteraction().getName().equalsIgnoreCase("ping")) {
            EmbedUtils embedUtils = new EmbedUtils();
            int ping = (int) event.getJDA().getGatewayPing();

            if(ping > 100) {
                EmbedBuilder embedError = embedUtils.errorEmbed("Pong! ??", "Le ping est de " + ping + "ms.");
                embedError.addField("Ping", String.format("Ping time: %dms%n", ping), false);
                embedError.addField("Progression", progressBar.update(ping), false);

                event.getInteraction().replyEmbeds(embedError.build()).setEphemeral(true).queue();
            } else if(ping > 50) {
                EmbedBuilder embedWarn = embedUtils.warningEmbed("Pong! ?", "Le ping est de " + ping + "ms.");
                embedWarn.addField("Ping", String.format("Ping time: %dms%n", ping), false);
                embedWarn.addField("Progression", progressBar.update(ping), false);
                event.getInteraction().replyEmbeds(embedWarn.build()).setEphemeral(true).queue();
            } else if(ping < 50) {
                EmbedBuilder embedSuccess = embedUtils.successEmbed("Pong! ?", "Le ping est de " + ping + "ms.");
                embedSuccess.addField("Ping", String.format("Ping time: %dms%n", ping), false);
                embedSuccess.addField("Progression", progressBar.update(ping), false);
                event.getInteraction().replyEmbeds(embedSuccess.build()).setEphemeral(true).queue();
            }
        }
    }
}
