package fr.skygames.managethediscord.commands;

import java.util.ArrayList;
import java.util.List;

import fr.skygames.managethediscord.utils.embeds.PaginatedEmbed;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

import javax.activation.CommandInfo;

public class Help extends ListenerAdapter {

    private static final int PAGE_SIZE = 5;

    public static void registerCommand(CommandListUpdateAction commands) {
        CommandData helpCommand = new CommandData("help", "Displays the help menu.")
                .addSubcommands(
                        new SubcommandGroupData("category", "The category of commands to display.")
                                .addSubcommands(
                                        new SubcommandData("fun", "Display fun commands."),
                                        new SubcommandData("music", "Display music commands."),
                                        new SubcommandData("moderation", "Display moderation commands.")
                                ),
                        new SubcommandData("all", "Display all available commands.")
                );

        commands.addCommands(helpCommand);
    }

    public static void onSlashCommand(SlashCommandEvent event) {
        if (event.getName().equals("help")) {
            List<Command> commands = event.getJDA().retrieveCommands().complete();
            List<Command> filteredCommands = filterCommands(commands, event);
            List<EmbedBuilder> embeds = buildEmbeds(filteredCommands, event);
            PaginatedEmbed paginatedEmbed = new PaginatedEmbed(embeds, PAGE_SIZE);
            paginatedEmbed.send(event.getInteraction().getHook());
        }
    }

    private static List<Command> filterCommands(List<Command> commands, SlashCommandEvent event) {
        String subcommand = event.getSubcommandName();
        if (subcommand == null || subcommand.equals("all")) {
            return commands;
        }

        String group = event.getSubcommandGroup();
        List<Command> filteredCommands = new ArrayList<>();
        for (Command command : commands) {
            if (command.getCategory() == null) {
                continue;
            }

            boolean hasGroup = group != null && command.getCategory().equals(group);
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
            PaginatedEmbed.EmbedPage page = new PaginatedEmbed.EmbedPage();
            StringBuilder descriptionBuilder = new StringBuilder();
            for (; i < commands.size(); i++) {
                Command command = commands.get(i);
                String usage = command.getUsage() == null ? "" : " " + command.getUsage();
                descriptionBuilder.append("**/").append(command.getName()).append("**").append(usage)
                        .append("\n").append(command.getDescription()).append("\n\n");
                if ((i + 1) % 10 == 0) {
                    break;
                }
            }
            page.setDescription(descriptionBuilder.toString());
            embeds.add(page.build());
        }
        return embeds;
    }

}