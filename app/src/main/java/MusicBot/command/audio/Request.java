package MusicBot.command.audio;

import MusicBot.MusicBot;
import MusicBot.audio.MusicUtil;
import MusicBot.command.base.CommandEvent;
import MusicBot.command.base.CommandUtil;
import MusicBot.command.base.reaction.ReactionCommand;
import MusicBot.command.base.reaction.ReactionEvent;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageReaction;

import java.util.*;

@Slf4j
public class Request extends ReactionCommand {
    private final static String NAME = "rq";

    //Maps a messageID to a map that gives the count of an Emote given the emote

    public Request() {
        this.setCommandName(NAME);
        this.addValidArgNum(1);
    }
    @Override
    public void executeCommand(CommandEvent e) {
        MusicUtil util = MusicBot.musicBot.getMusicUtil();
        util.sendSongInfo(e);
        log.debug("Message ID of song info message: " + getResultMessageID());
        e.getOrigEvent().getGuild().getTextChannels().get(0).addReactionById(getResultMessageID(), "U+1f44d").queue();
        e.getOrigEvent().getGuild().getTextChannels().get(0).addReactionById(getResultMessageID(), "U+1f44e").queue();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                HashMap<String, Integer> reactions = getReactionCount();
                MusicBot.musicBot.getReactionUtil().removeReactionMessage(getResultMessageID());
                log.debug("Finished listening for reactions on message: " + getResultMessageID());
                reactions.entrySet().forEach(emote -> {
                    log.debug(emote.getKey() + ": " + emote.getValue());
                });
                if (reactions.get("U+1f44d") > reactions.get("U+1f44e")) {
                    util.queueSong(e.getOrigEvent().getGuild().getId(), e.getWords().get(1), e);
                }
            }
        };
        Timer timer = new Timer(true);
        timer.schedule(task, 30000);
    }
}
