package fr.skygames.managethediscord.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Help extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equalsIgnoreCase("help")) {
            System.out.println("Command: " + event.getName());
            event.deferReply().queue();
            event.getJDA().retrieveCommands().queue(commands -> {
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle("Help")
                        .setDescription("This bot has the following commands:")
                        .setColor(0x00FF00);
                for (Command command : commands) {
                    String commandName = command.getName();
                    String commandDescription = command.getDescription();
                    String commandSummary = commandDescription != null ? commandDescription : "No description provided";
                    if (command.getSubcommands().isEmpty()) {
                        embed.addField("/" + commandName, commandSummary, false);
                    } else {
                        StringBuilder subcommands = new StringBuilder();
                        for (Command.Subcommand subcommand : command.getSubcommands()) {
                            subcommands.append("/").append(commandName).append(" ").append(subcommand.getName());
                            if (subcommand.getDescription() != null) {
                                subcommands.append(": ").append(subcommand.getDescription());
                            }
                            subcommands.append("\n");
                        }
                        embed.addField("/" + commandName, commandSummary + ":\n" + subcommands.toString(), false);
                    }
                }
                embed.setFooter("SkyGames | "+ new SimpleDateFormat("'le' dd/MM/yyyy '\u00E0' kk:mm:ss").format(new Date()));
                event.getHook().sendMessageEmbeds(embed.build()).queue();
            });
        }
    }

}