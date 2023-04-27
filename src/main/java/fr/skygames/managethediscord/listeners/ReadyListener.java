package fr.skygames.managethediscord.listeners;

import fr.skygames.managethediscord.managers.Manager;
import fr.skygames.managethediscord.utils.Constants;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

public class ReadyListener extends ListenerAdapter {

    private final Logger logger;
    private final Manager manager;

    public ReadyListener(Logger logger, Manager manager) {
        super();
        this.logger = logger;
        this.manager = manager;
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        super.onReady(event);

        logger.info("Bot is ready!");

        manager.initCommands();

        try {
            logger.info("\n" +
                    "::::::::  :::    ::: :::   :::  ::::::::      :::     ::::    ::::  :::::::::: ::::::::  \n" +
                    ":+:    :+: :+:   :+:  :+:   :+: :+:    :+:   :+: :+:   +:+:+: :+:+:+ :+:       :+:    :+: \n" +
                    "+:+        +:+  +:+    +:+ +:+  +:+         +:+   +:+  +:+ +:+:+ +:+ +:+       +:+        \n" +
                    "+#++:++#++ +#++:++      +#++:   :#:        +#++:++#++: +#+  +:+  +#+ +#++:++#  +#++:++#++ \n" +
                    "       +#+ +#+  +#+      +#+    +#+   +#+# +#+     +#+ +#+       +#+ +#+              +#+ \n" +
                    "#+#    #+# #+#   #+#     #+#    #+#    #+# #+#     #+# #+#       #+# #+#       #+#    #+# \n" +
                    " ########  ###    ###    ###     ########  ###     ### ###       ### ########## ########  ");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Constants.updateMemberCount(event);
    }
}
