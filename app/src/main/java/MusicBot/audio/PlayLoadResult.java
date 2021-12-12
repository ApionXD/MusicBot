package MusicBot.audio;

import MusicBot.MusicBot;
import MusicBot.command.base.CommandEvent;
import MusicBot.command.base.CommandUtil;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

@Getter
@Slf4j
public class PlayLoadResult extends BaseLoadResult {
    private final AudioPlayer player;
    private final TrackScheduler scheduler;

    public PlayLoadResult(CommandEvent e) {
        super(e);
        log.trace("Constructing LoadListener");
        MusicUtil util = MusicBot.musicBot.getMusicUtil();
        player = util.getPlayerFromID(this.getGuildID());
        scheduler = util.getSchedulerFromID(this.getGuildID());
        log.trace("Done constructing PlayLoadResult");
    }
    @Override
    public void trackLoaded(AudioTrack track) {
        log.info("Track loaded!");
        scheduler.addTrackToQueue(track);
        if (!scheduler.isPlaying()) {
            scheduler.playFirstTrackInQueue();
        }
    }

    @Override
    public void playlistLoaded(AudioPlaylist playlist) {
        log.debug("Loading playlist!");
        playlist.getTracks().forEach(p -> {
            scheduler.addTrackToQueue(p);
        });
        if (!scheduler.isPlaying()) {
            scheduler.playFirstTrackInQueue();
        }
    }

    @Override
    public void noMatches() {
        EmbedBuilder resultingEmbed = new EmbedBuilder(CommandUtil.BASE_EMBED).setDescription("Couldn't find a song at that link!");
        sendMessageToOriginalChannel(resultingEmbed.build());
        if (scheduler.getTracks().size() == 0) {
            log.info("No tracks to play for guild " + this.getGuildID());
            MusicBot.musicBot.getJda().getGuildById(this.getGuildID()).getAudioManager().closeAudioConnection();
        }

    }

    @Override
    public void loadFailed(FriendlyException exception) {
        EmbedBuilder resultingEmbed = new EmbedBuilder(CommandUtil.BASE_EMBED).addField("Command failed with exception: ", exception.getMessage(), true);
        sendMessageToOriginalChannel(resultingEmbed.build());
    }
}
