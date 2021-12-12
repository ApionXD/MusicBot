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
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.*;

@Slf4j
//TODO Add settings, like voteTime, percentage of thumbs up, custom reactions
public class Request extends ReactionCommand {
    private final static String NAME = "rq";
    public static final String SHORT_HELP_DESC = "Request a song/playlist";
    public static final String LONG_HELP_DESC = "Allows user to request a song or a playlist, and other users to vote on the request";
    private final static String THUMBS_DOWN = "U+1f44e";
    private final static String THUMBS_UP = "U+1f44d";

    public Request() {
        this.setCommandName(NAME);
        this.addValidArgNum(1);
        this.setShortHelpDesc(SHORT_HELP_DESC);
        this.setLongHelpDesc(LONG_HELP_DESC);
    }
    @Override
    public void executeCommand(CommandEvent e) {
        super.executeCommand(e);
        final MusicUtil util = MusicBot.musicBot.getMusicUtil();
        final TextChannel channel =  e.getOrigEvent().getGuild().getTextChannels().get(0);
        util.sendSongInfo(e);
        log.debug("Message ID of song info message: " + getResultMessageID());
        channel.addReactionById(getResultMessageID(), THUMBS_UP).queue();
        channel.addReactionById(getResultMessageID(), THUMBS_DOWN).queue();
        final TimerTask task = new TimerTask() {
            @Override
            public void run() {
                HashMap<String, Integer> reactions = getReactionCount();
                MusicBot.musicBot.getReactionUtil().removeReactionMessage(getResultMessageID());
                log.debug("Finished listening for reactions on message: " + getResultMessageID());
                reactions.entrySet().forEach(emote -> {
                    log.debug(emote.getKey() + ": " + emote.getValue());
                });
                if (reactions.get(THUMBS_UP) > reactions.get(THUMBS_DOWN)) {
                    util.queueSong(e.getOrigEvent().getGuild().getId(), e.getWords().get(1), e);
                }
            }
        };
        final Timer timer = new Timer(true);
        timer.schedule(task, 30000);
    }
}
