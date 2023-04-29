package fr.skygames.managethediscord.utils;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class SlashCommandRegistry {

    public static void register(Guild guild) {
            guild.upsertCommand("roles", "Manage the roles of the server")
                    .addSubcommands(
                            new SubcommandData("add", "Add a role to a user")
                                    .addOption(OptionType.STRING, "name", "The role name", true)
                                    .addOption(OptionType.STRING, "id", "The role id", true)
                                    .addOption(OptionType.BOOLEAN, "isstaff", "Is the role a staff role ?", true),
                            new SubcommandData("remove", "Remove a role from a user")
                                    .addOption(OptionType.STRING, "id", "The role id", true),
                            new SubcommandData("list", "List all the roles of the server")
                    ).queue();

            guild.upsertCommand("alpha", "Send alpha infomations").queue();

            guild.upsertCommand("ping", "Get the bot ping").queue();

            guild.upsertCommand("help", "Get help about the bot").queue();

            guild.upsertCommand("clear", "Clear messages")
                    .addOption(OptionType.INTEGER, "amount", "The amount of messages to delete", true)
                    .queue();

            guild.upsertCommand("roles", "SOON").queue();

            guild.upsertCommand("join", "Join a voice channel").queue();

            guild.upsertCommand("nowplaying", "Get the current song").queue();

            guild.upsertCommand("play", "Play a song")
                    .addOption(OptionType.STRING, "url", "The url of the song", true)
                    .queue();

            guild.upsertCommand("queue", "Get the queue").queue();

            guild.upsertCommand("repeate", "Repeat the current song").queue();

            guild.upsertCommand("skip", "Skip the current song").queue();

            guild.upsertCommand("stop", "Stop the current song").queue();

            guild.upsertCommand("pause", "Pause the current song").queue();

            guild.upsertCommand("resume", "Resume the current song").queue();

            guild.upsertCommand("volume", "Change the volume of the bot")
                    .addOption(OptionType.INTEGER, "volume", "The volume you want to set", true)
                    .queue();
    }

}
