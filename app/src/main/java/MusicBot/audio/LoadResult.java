package MusicBot.audio;

import MusicBot.MusicBot;
import MusicBot.command.CommandEvent;
import MusicBot.command.CommandUtil;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.concurrent.Future;

@Getter
@Slf4j
public class LoadResult implements AudioLoadResultHandler {
    private final AudioPlayer player;
    private final TrackScheduler scheduler;
    private final String guildID;
    private final String outputChannelID;

    public LoadResult(CommandEvent e) {
        log.trace("Constructing LoadListener");
        guildID = e.getOrigEvent().getGuild().getId();
        MusicUtil util = MusicBot.musicBot.getMusicUtil();
        player = util.getPlayerFromID(guildID);
        scheduler = util.getSchedulerFromID(guildID);
        outputChannelID = MusicBot.musicBot.getSettingsManager().getSettingsFromGuildID(guildID).getCommandChannelID();
        log.trace("Done constructing LoadListener");
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
    }

    @Override
    public void loadFailed(FriendlyException exception) {
        EmbedBuilder resultingEmbed = new EmbedBuilder(CommandUtil.BASE_EMBED).addField("Command failed with exception: ", exception.getMessage(), true);
        sendMessageToOriginalChannel(resultingEmbed.build());
    }
    private void sendMessageToOriginalChannel(MessageEmbed m) {
        MusicBot.musicBot.getJda().getGuildById(guildID).getTextChannelById(outputChannelID).sendMessage(m).queue();
    }
}
