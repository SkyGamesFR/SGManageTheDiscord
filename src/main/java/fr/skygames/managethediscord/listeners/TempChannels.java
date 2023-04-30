package fr.skygames.managethediscord.listeners;

import fr.skygames.managethediscord.utils.Constants;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Objects;


public class TempChannels extends ListenerAdapter {

    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {

        if(event.getChannelJoined() != null && event.getChannelJoined().asVoiceChannel().getId().equals(Constants.CREATE_VOICE_CHANNEL_ID)) {
            Member member = event.getMember();
            VoiceChannel channel = Objects.requireNonNull(event.getGuild().getCategoryById(Constants.TEMP_VOICE_CHANNEL_CATEGORY_ID)).createVoiceChannel("⌛ | " + member.getEffectiveName()).complete();

            channel.getManager().putMemberPermissionOverride(event.getMember().getIdLong(), Constants.getVoiceAllowPermissions(), Constants.getVoiceDenyPermissions()).queue();

            event.getGuild().moveVoiceMember(member, channel).queue();
        }

        if(event.getChannelLeft() != null && event.getChannelLeft().asVoiceChannel().getName().startsWith("⌛ |")) {
            if(event.getChannelLeft().getMembers().size() == 0) {
                event.getChannelLeft().delete().queue();
            } else {
                event.getChannelLeft().getManager().setName("⌛ | " + event.getChannelLeft().getMembers().get(0).getEffectiveName()).queue();
            }
        }
    }
}
