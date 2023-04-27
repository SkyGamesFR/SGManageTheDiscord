package fr.skygames.managethediscord.listeners;

import fr.skygames.managethediscord.utils.Constants;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Date;

public class GuildListener extends ListenerAdapter {

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        if(event.getJDA().getGuilds().size() > 1 || !event.getGuild().getId().equals(Constants.GUILD_ID)) {
            Guild guild = event.getJDA().getGuildById(event.getGuild().getId());

            if(guild != null) {
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setTitle("Erreur");
                embedBuilder.setDescription("Le bot SkyGames est strictement **PRIVÉ**.");
                embedBuilder.addField("SkyGames", "Rejoignez nous sur notre site internet pour plus d'informations.", false);
                embedBuilder.addField("Site Internet", "https://skygames.fr", false);
                embedBuilder.setColor(0xff0000);
                embedBuilder.setFooter("SkyGames | " + Constants.DATE_FORMAT());

                event.getGuild().getTextChannels().get(0).sendMessageEmbeds(embedBuilder.build()).queue();
            }

            event.getGuild().leave().queue();
        }
    }
}
