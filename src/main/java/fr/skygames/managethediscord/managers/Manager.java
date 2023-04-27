package fr.skygames.managethediscord.managers;

import fr.skygames.managethediscord.commands.Help;
import fr.skygames.managethediscord.commands.mod.Clear;
import fr.skygames.managethediscord.commands.music.*;
import fr.skygames.managethediscord.commands.owner.AlphaCommand;
import fr.skygames.managethediscord.commands.RolesCommand;
import fr.skygames.managethediscord.listeners.LeaveJoinListener;
import fr.skygames.managethediscord.listeners.ReadyListener;
import fr.skygames.managethediscord.listeners.TempChannels;
import fr.skygames.managethediscord.sql.SqlConnector;
import fr.skygames.managethediscord.utils.SlashCommandRegistry;
import fr.skygames.managethediscord.utils.exception.MissingPropertyException;
import fr.skygames.managethediscord.utils.files.GlobalProperties;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;

public class Manager {

    public Logger logger = LoggerFactory.getLogger(Manager.class);
    public GlobalProperties config;
    public JDA jda;
    public SqlConnector connector;

    public void init() {
        try {
            initJDA();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error while loading JDA");
        }
    }

    public void initJDA() throws Exception {
        jda = JDABuilder.createDefault(config.getBotToken())
                .setAutoReconnect(true)
                .setBulkDeleteSplittingEnabled(false)
                .setActivity(Activity.streaming("ManageTheDiscord", "https://www.youtube.com/watch?v=dQw4w9WgXcQ"))
                .addEventListeners(
                        new ReadyListener(this.logger, this),
                        new LeaveJoinListener(config),

                        new TempChannels(),

                        new JoinCommand(),
                        new NowPlayingCommand(),
                        new PlayCommand(),
                        new QueueCommand(),
                        new RepeatCommand(),
                        new SkipCommand(),
                        new StopCommand(),
                        new VolumeCommand(),

                        new RolesCommand(connector),
                        new AlphaCommand(),
                        new Help(),
                        new Clear()
                )
                .enableCache(CacheFlag.VOICE_STATE, CacheFlag.ACTIVITY, CacheFlag.CLIENT_STATUS, CacheFlag.EMOTE, CacheFlag.MEMBER_OVERRIDES, CacheFlag.ONLINE_STATUS, CacheFlag.ROLE_TAGS, CacheFlag.VOICE_STATE)
                .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_PRESENCES)
                .build();
    }

    public void initConfig() {
        logger.debug("Config init...");
        config = new GlobalProperties();
    }

    public void initSQL() {
        logger.debug("SQL init...");
        try {
            connector = new SqlConnector();
        } catch (ClassNotFoundException | MissingPropertyException | SQLException | IOException e) {
            logger.error("SqlConnector Exception !", e);
            System.exit(1);
            return;
        }
    }

    public void initCommands() {
        logger.debug("Commands init...");

        Guild guild = jda.getGuildById("723615515171487827");

        if(guild != null) {
            logger.info("Registering commands...");
            SlashCommandRegistry.register(jda);
        }

        logger.info("Command roles registered");
    }

}
