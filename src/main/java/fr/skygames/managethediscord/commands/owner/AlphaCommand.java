package fr.skygames.managethediscord.commands.owner;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.Objects;

public class AlphaCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("alpha")) {
            EnumSet<Permission> permissions = EnumSet.of(Permission.ADMINISTRATOR);

            if (Objects.requireNonNull(event.getMember()).hasPermission(permissions)) {
                EmbedBuilder embedBuilder = new EmbedBuilder()
                        .setAuthor("SkyGames", "https://skygames.fr", event.getJDA().getSelfUser().getAvatarUrl())
                        .setTitle("**\uD83C\uDF89 Alpha privée de SkyGames ! \uD83D\uDE80**")
                        .addField("", "Nous sommes ravis de vous annoncer que l'alpha privée de notre serveur approche à grands pas ! Pendant cette période, les joueurs inscrits auront la possibilité de découvrir le serveur avant sa sortie officielle et de nous aider à le tester et à le peaufiner.", false)
                        .addBlankField(false)
                        .addField("**\uD83D\uDCDD Comment s'inscrire ?**", "Les inscriptions pour l'alpha privée sont désormais ouvertes ! Pour participer, rendez-vous sur notre site web et inscrivez vous. Vous serez alors ajouté a la whitelist de l'alpaha privée.", false)
                        .addBlankField(false)
                        .addField("**\uD83D\uDCE5 Comment télécharger le modpack ?**", "Le modpack de SkyGames est essentiel pour jouer sur notre serveur. Vous pouvez le télécharger via le lien suivant : https://skygames.fr/launcher. Assurez-vous de télécharger la version la plus récente pour éviter tout problème de compatibilité.", false)
                        .addBlankField(false)
                        .addField("**\uD83D\uDD0D La progression de l'alpha privée**", "Pendant l'alpha privée, nous vous demandons de jouer activement sur le serveur et de nous faire part de tous vos retours. Cela nous aidera à améliorer l'expérience globale du jeu. Veuillez noter que toutes les données seront réinitialisées à la fin de l'alpha et de la phase beta.", false)
                        .addBlankField(false)
                        .addField("**\uD83D\uDCB0 Sauvegarde des achats boutique**", "Tous les achats effectués sur notre boutique pendant l'alpha privée seront sauvegardés et transférés sur le serveur officiel. Vous pouvez donc acheter en toute sécurité sans craindre de perdre vos objets une fois l'alpha terminée.", false)
                        .addBlankField(false)
                        .addField("", "Nous avons hâte de voir les joueurs rejoindre notre alpha privée et de recevoir leurs commentaires et suggestions. Merci de votre soutien continu ! \uD83D\uDE4C", false)
                        .addField("", "**\uD83C\uDF10 Site web : https://skygames.fr**", false)
                        .addField("", Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(event.getGuild()).getRoleById("972124392475738152")).getAsMention()), false);

                MessageEmbed embed = embedBuilder.build();
                event.replyEmbeds(embed)
                        .setAllowedMentions(EnumSet.of(Message.MentionType.ROLE))
                        .mentionRoles("972124392475738152")
                        .queue();
            } else {
                MessageEmbed embed = new EmbedBuilder()
                        .setTitle("Alpha")
                        .setDescription("You don't have permission to use this command.")
                        .build();

                event.replyEmbeds(embed).setEphemeral(true).queue();
            }

        }
    }
}
