package MusicBot.audio;

import MusicBot.MusicBot;
import MusicBot.command.base.CommandEvent;
import MusicBot.command.base.reaction.ReactionCommand;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;

@Slf4j
@Getter
public abstract class BaseLoadResult implements AudioLoadResultHandler {
    private final CommandEvent origEvent;
    private final String guildID;
    private final String outputChannelID;
    public BaseLoadResult(CommandEvent e) {
        log.trace("Constructing BaseLoadResult");
        guildID = e.getOrigEvent().getGuild().getId();
        MusicUtil util = MusicBot.musicBot.getMusicUtil();
        outputChannelID = MusicBot.musicBot.getSettingsManager().getSettingsFromGuildID(guildID).getCommandChannelID();
        origEvent = e;
        log.trace("Done constructing LoadListener");
    }
    protected void sendMessageToOriginalChannel(MessageEmbed m) {
        MusicBot.musicBot.getJda().getGuildById(this.getGuildID()).getTextChannelById(this.getOutputChannelID()).sendMessage(m).queue();
    }
    //Adds the message sent to ReactionUtil
    protected void respondOrigChannelAndReact(MessageEmbed m, ReactionCommand c) {
        Message reactMessage = MusicBot.musicBot.getJda().getGuildById(this.getGuildID()).getTextChannelById(this.getOutputChannelID()).sendMessage(m).complete();
        c.setResultMessageID(reactMessage.getId());
        MusicBot.musicBot.getReactionUtil().addReactionMessage(reactMessage.getId(), c);
    }
}
