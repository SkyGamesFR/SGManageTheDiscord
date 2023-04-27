package fr.skygames.managethediscord.utils;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.Event;

import java.util.ArrayList;
import java.util.Collection;

public class Constants {

    public static final String SERVER_NAME = "SkyGames";
    public static final String OWNER_ID = "216936486875037699";
    public static final String GUILD_ID = "723615515171487827";
    public static final String MEMBER_COUNT_CHANNEL_ID = "1100836436804120686";

    public static final String TEMP_VOICE_CHANNEL_CATEGORY_ID = "1007033245415768114";
    public static final String CREATE_VOICE_CHANNEL_ID = "1007033365385445497";

    public static void updateMemberCount(Event event) {
        Guild guild = event.getJDA().getGuildById(GUILD_ID);
        assert guild != null;
        VoiceChannel channel = guild.getVoiceChannelById(MEMBER_COUNT_CHANNEL_ID);
        assert channel != null;
        channel.getManager().setName("🎉 Membres | " + guild.getMemberCount()).queue();
    }

    public static long getVoiceAllowPermissions() {
        Collection<Permission> allowPermissions = new ArrayList<>();
        allowPermissions.add(Permission.VOICE_CONNECT);
        allowPermissions.add(Permission.VOICE_SPEAK);
        allowPermissions.add(Permission.VOICE_USE_VAD);
        allowPermissions.add(Permission.VOICE_STREAM);
        allowPermissions.add(Permission.VOICE_MUTE_OTHERS);
        allowPermissions.add(Permission.VOICE_DEAF_OTHERS);
        allowPermissions.add(Permission.VOICE_MOVE_OTHERS);

        return allowPermissions.size();
    }

    public static long getVoiceDenyPermissions() {
        Collection<Permission> denyPermissions = new ArrayList<>();
        denyPermissions.add(Permission.VOICE_CONNECT);
        denyPermissions.add(Permission.VOICE_SPEAK);
        denyPermissions.add(Permission.VOICE_USE_VAD);
        denyPermissions.add(Permission.VOICE_STREAM);
        denyPermissions.add(Permission.VOICE_MUTE_OTHERS);
        denyPermissions.add(Permission.VOICE_DEAF_OTHERS);
        denyPermissions.add(Permission.VOICE_MOVE_OTHERS);

        return denyPermissions.size();
    }

}
