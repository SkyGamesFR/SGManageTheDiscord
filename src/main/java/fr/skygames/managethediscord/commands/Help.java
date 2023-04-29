package fr.skygames.managethediscord.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fr.skygames.managethediscord.utils.embeds.PaginatedEmbed;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

import javax.activation.CommandInfo;

public class Help extends ListenerAdapter {

    private static final int PAGE_SIZE = 5;

    public void onSlashCommand(SlashCommandInteraction event) {
        if (event.getName().equals("help")) {
            List<Command> commands = event.getJDA().retrieveCommands().complete();
            List<Command> filteredCommands = filterCommands(commands, event);
            List<MessageEmbed> embeds = this.buildEmbeds(filteredCommands);
            PaginatedEmbed paginatedEmbed = new PaginatedEmbed(embeds);
            Collection<ItemComponent> actionRows = paginatedEmbed.getActionRow();
            event.replyEmbeds(paginatedEmbed.getCurrentPage()).setActionRow(actionRows).queue();
        }
    }

    private static List<Command> filterCommands(List<Command> commands, SlashCommandInteraction event) {
        String subcommand = event.getSubcommandName();
        if (subcommand == null || subcommand.equals("all")) {
            return commands;
        }

        String group = event.getSubcommandGroup();
        List<Command> filteredCommands = new ArrayList<>();
        for (Command command : commands) {
            if (command.getSubcommands() == null) {
                continue;
            }

            boolean hasGroup = group != null && command.getSubcommandGroups().equals(group);
            boolean hasSubcommand = command.getName().equals(subcommand);
            if (hasGroup || hasSubcommand) {
                filteredCommands.add(command);
            }
        }

        return filteredCommands;
    }

    private List<MessageEmbed> buildEmbeds(List<Command> commands) {
        List<MessageEmbed> embeds = new ArrayList<>();
        int i = 0;
        while (i < commands.size()) {
            PaginatedEmbed page = new PaginatedEmbed(embeds);
            StringBuilder descriptionBuilder = new StringBuilder();
            for (; i < commands.size(); i++) {
                Command command = commands.get(i);
                descriptionBuilder
                        .append("**/").append(command.getName()).append("**")
                        .append("\n")
                        .append(command.getDescription()).append("\n\n");
                if ((i + 1) % 10 == 0) {
                    break;
                }
            }

            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("Help Menu");
            builder.setDescription(descriptionBuilder.toString());
            builder.setFooter("Page " + (embeds.size() + 1));
            embeds.add(builder.build());
        }
        return embeds;
    }

}