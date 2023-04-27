package fr.skygames.managethediscord.utils;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class SlashCommandRegistry {

    public static void register(JDA jda) {
        jda.upsertCommand("ping", "Ping the bot").queue();

        jda.upsertCommand("help", "Get help about the bot")
                .addOption(OptionType.STRING, "command", "The command you want help about", false)
                .queue();

        jda.upsertCommand("clear", "Clear messages")
                .addOption(OptionType.INTEGER, "amount", "The amount of messages to delete", true)
                .queue();

        jda.upsertCommand(Commands.slash("alpha", "Send alpha infomations")).queue();

        jda.updateCommands()
                .addCommands(Commands.slash("roles", "Manage the roles of the server")
                        .addSubcommands(
                                new SubcommandData("add", "Add a role to a user")
                                        .addOption(OptionType.STRING, "name", "The role name", true)
                                        .addOption(OptionType.STRING, "id", "The role id", true)
                                        .addOption(OptionType.BOOLEAN, "isstaff", "Is the role a staff role ?", true),
                                new SubcommandData("remove", "Remove a role from a user")
                                        .addOption(OptionType.STRING, "id", "The role id", true),
                                new SubcommandData("list", "List all the roles of the server")
                        ))
                .queue();

        jda.upsertCommand("play", "Play a song")
                .addOption(OptionType.STRING, "url", "The url of the song", true)
                .queue();

        jda.upsertCommand("join", "Join a voice channel").queue();

        jda.upsertCommand("nowplaying", "Get the current song").queue();

        jda.upsertCommand("queue", "Get the queue").queue();

        jda.upsertCommand("repeate", "Repeat the current song").queue();

        jda.upsertCommand("skip", "Skip the current song").queue();

        jda.upsertCommand("stop", "Stop the current song").queue();

        jda.upsertCommand("volume", "Change the volume of the bot")
                .addOption(OptionType.INTEGER, "volume", "The volume you want to set", true)
                .queue();
    }

}
