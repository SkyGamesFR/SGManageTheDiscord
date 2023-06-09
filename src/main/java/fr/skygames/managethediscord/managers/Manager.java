package fr.skygames.managethediscord.managers;

import fr.skygames.managethediscord.commands.Help;
import fr.skygames.managethediscord.commands.Ping;
import fr.skygames.managethediscord.commands.mod.Clear;
import fr.skygames.managethediscord.commands.music.*;
import fr.skygames.managethediscord.commands.owner.AlphaCommand;
import fr.skygames.managethediscord.commands.owner.RolesCommand;
import fr.skygames.managethediscord.listeners.*;
import fr.skygames.managethediscord.sql.SqlConnector;
import fr.skygames.managethediscord.utils.SlashCommandRegistry;
import fr.skygames.managethediscord.utils.exception.MissingPropertyException;
import fr.skygames.managethediscord.utils.files.GlobalProperties;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
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

    public void initJDA() {
        jda = JDABuilder.createDefault(config.getBotToken())
                .setAutoReconnect(true)
                .setBulkDeleteSplittingEnabled(false)
                .setActivity(Activity.streaming("ManageTheDiscord", "https://www.youtube.com/watch?v=dQw4w9WgXcQ"))
                .setAutoReconnect(true)
                .addEventListeners(
                        new ReadyListener(this.logger, this),
                        new GuildListener(),
                        new LeaveJoinListener(config),

                        new TempChannels(),
                        new AutoMod(config.getBadWords(),this.jda),

                        new JoinCommand(),
                        new NowPlayingCommand(),
                        new PlayCommand(),
                        new QueueCommand(),
                        new RepeatCommand(),
                        new SkipCommand(),
                        new StopCommand(),
                        new VolumeCommand(),

                        new ButtonListener(),

                        new AlphaCommand(),
                        new RolesCommand(),
                        new Ping(),
                        new Help(),
                        new Clear()
                )
                .enableCache(CacheFlag.VOICE_STATE, CacheFlag.ACTIVITY, CacheFlag.CLIENT_STATUS, CacheFlag.EMOJI, CacheFlag.MEMBER_OVERRIDES, CacheFlag.ONLINE_STATUS, CacheFlag.ROLE_TAGS, CacheFlag.VOICE_STATE)
                .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_PRESENCES, GatewayIntent.MESSAGE_CONTENT)
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
            SlashCommandRegistry.register(guild);
        }

        logger.info("Command roles registered");
    }

}
