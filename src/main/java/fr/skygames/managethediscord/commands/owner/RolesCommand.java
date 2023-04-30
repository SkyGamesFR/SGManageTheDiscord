package fr.skygames.managethediscord.commands.owner;

import fr.skygames.managethediscord.utils.Constants;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class RolesCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("roles")) {
            if (!event.getGuild().getId().equals(Constants.GUILD_ID)) return;

            if (event.getInteraction().getMember().getId().equals(Constants.OWNER_ID)) {
                TextChannel channel = event.getGuild().getTextChannelById(Constants.ROLES_CHANNEL_ID);
                assert channel != null;

                Guild guild = event.getGuild();

                EmbedBuilder embed = new EmbedBuilder();
                embed.setTitle("Roles disponibles sur le serveur");
                embed.setDescription("Pour Montrer aux autres membres sur quel serveur vous êtes, veuillez réagir avec les réactions ci-dessous.");
                embed.addField("Rôles disponibles", "Σ - " + event.getGuild().getRoleById(Constants.SIGMA_ROLE).getAsMention() + "\n" +
                        "Τ - " + event.getGuild().getRoleById(Constants.TAU_ROLE).getAsMention() + "\n" +
                        "Υ - " + event.getGuild().getRoleById(Constants.UPSILON_ROLE).getAsMention() + "\n" +
                        "Φ - " + event.getGuild().getRoleById(Constants.PHI_ROLE).getAsMention(), false);

                event.getGuild().getTextChannelById(Constants.ROLES_CHANNEL_ID).sendMessageEmbeds(embed.build()).queue(message -> {
                    message.addReaction(Emoji.fromCustom("sigma", Long.parseLong("1102004131339718697"), false)).queue();
                    message.addReaction(Emoji.fromCustom("tau", Long.parseLong("1102004132618981396"), false)).queue();
                    message.addReaction(Emoji.fromCustom("upsilon", Long.parseLong("1102004128017809469"), false)).queue();
                    message.addReaction(Emoji.fromCustom("phi", Long.parseLong("1102004129380978799"), false)).queue();
                });


            } else {
                event.getInteraction().reply("You don't have the permission to use this command.").queue();
            }
        }
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        switch (event.getReaction().getEmoji().getName()) {
            case "sigma":
                event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRoleById(Constants.SIGMA_ROLE)).queue();
                break;
            case "tau":
                event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRoleById(Constants.TAU_ROLE)).queue();
                break;
            case "upsilon":
                event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRoleById(Constants.UPSILON_ROLE)).queue();
                break;
            case "phi":
                event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRoleById(Constants.PHI_ROLE)).queue();
                break;
            default:
                event.getReaction().removeReaction(event.getUser()).queue();
                break;
        }
    }

    @Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent event) {
        switch (event.getReaction().getEmoji().getName()) {
            case "sigma":
                event.getGuild().removeRoleFromMember(event.getMember(), event.getGuild().getRoleById(Constants.SIGMA_ROLE)).queue();
                break;
            case "tau":
                event.getGuild().removeRoleFromMember(event.getMember(), event.getGuild().getRoleById(Constants.TAU_ROLE)).queue();
                break;
            case "upsilon":
                event.getGuild().removeRoleFromMember(event.getMember(), event.getGuild().getRoleById(Constants.UPSILON_ROLE)).queue();
                break;
            case "phi":
                event.getGuild().removeRoleFromMember(event.getMember(), event.getGuild().getRoleById(Constants.PHI_ROLE)).queue();
                break;
            default:
                event.getReaction().removeReaction(event.getUser()).queue();
                break;
        }    }
}
