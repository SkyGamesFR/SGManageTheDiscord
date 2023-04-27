package fr.skygames.managethediscord.commands;

import fr.skygames.managethediscord.data.Roles;
import fr.skygames.managethediscord.data.RolesService;
import fr.skygames.managethediscord.sql.SqlConnector;
import fr.skygames.managethediscord.utils.exception.MissingPropertyException;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.Objects;

public class RolesCommand extends ListenerAdapter {

    private SqlConnector sqlConnector;
    private RolesService rolesService;

    public RolesCommand(SqlConnector sqlConnector) {
        super();
        this.sqlConnector = sqlConnector;

        rolesService = new RolesService(sqlConnector);
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("roles")) {
            switch (Objects.requireNonNull(event.getSubcommandName())) {
                case "add":
                    try {
                        rolesService.create(new Roles(event.getOption("id").getAsString(), event.getOption("name").getAsString(), event.getOption("isstaff").getAsBoolean()));
                        event.reply("Le rôle a bien été ajouté !").setEphemeral(true).queue();
                    } catch (SQLException | MissingPropertyException | ClassNotFoundException e) {
                        event.reply("Une erreur est survenue lors de l'ajout du rôle !").setEphemeral(true).queue();
                        throw new RuntimeException(e);
                    }
                    break;
                case "remove":
                    try {
                        rolesService.delete(event.getOption("id").getAsString());
                        event.reply("Le rôle a bien été supprimé !").setEphemeral(true).queue();
                    } catch (SQLException | MissingPropertyException | ClassNotFoundException e) {
                        event.reply("Une erreur est survenue lors de la suppression du rôle !").setEphemeral(true).queue();
                        throw new RuntimeException(e);
                    }
                    break;
                case "list":
                    try {
                        event.reply(rolesService.getAll().toString()).setEphemeral(true).queue();
                    } catch (SQLException | MissingPropertyException | ClassNotFoundException e) {
                        event.reply("Une erreur est survenue lors de la récupération des rôles !").setEphemeral(true).queue();
                        throw new RuntimeException(e);
                    }
                    break;
                default:
                    event.reply("[ROLES] Commande inconnue !").setEphemeral(true).queue();
                    break;
            }
        }
    }
}
