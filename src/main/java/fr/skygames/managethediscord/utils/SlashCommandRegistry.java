package fr.skygames.managethediscord.utils;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class SlashCommandRegistry {

    public static void register(Guild guild) {
            guild.updateCommands()
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

                    .addCommands(Commands.slash("alpha", "Send alpha infomations"))

                    .addCommands(Commands.slash("ping", "Get the bot ping"))
                    .addCommands(Commands.slash("help", "Get help about the bot")

                            .addOption(OptionType.STRING, "command", "The command you want help about", false))
                    .addCommands(Commands.slash("clear", "Clear messages")
                            .addOption(OptionType.INTEGER, "amount", "The amount of messages to delete", true))

                    .addCommands(Commands.slash("join", "Join a voice channel"))
                    .addCommands(Commands.slash("nowplaying", "Get the current song"))
                    .addCommands(Commands.slash("play", "Play a song")
                            .addOption(OptionType.STRING, "url", "The url of the song", true))
                    .addCommands(Commands.slash("queue", "Get the queue"))
                    .addCommands(Commands.slash("repeate", "Repeat the current song"))
                    .addCommands(Commands.slash("skip", "Skip the current song"))
                    .addCommands(Commands.slash("stop", "Stop the current song"))
                    .addCommands(Commands.slash("volume", "Change the volume of the bot")
                            .addOption(OptionType.INTEGER, "volume", "The volume you want to set", true))

                    .queue();
    }

}
