package MusicBot.audio;

import MusicBot.MusicBot;
import MusicBot.command.base.CommandUtil;
import com.google.common.collect.Lists;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;

@Slf4j
public class TrackScheduler extends AudioEventAdapter {

    private final AudioPlayer player;
    private final String guildID;
    @Getter
    private ArrayList<AudioTrack> tracks;

    @Getter
    private boolean isPlaying;
    public TrackScheduler(String id) {
        tracks = Lists.newArrayList();
        guildID = id;
        player = MusicBot.musicBot.getMusicUtil().getPlayerFromID(id);
        isPlaying = false;
    }


    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        super.onTrackEnd(player, track, endReason);
        tracks.remove(track);
        log.warn("Num tracks: " + tracks.size());
        log.debug(track.getInfo().title + " ended, playing next.");
        if (tracks.size() == 0) {
            log.debug("No tracks left to play!");
            isPlaying = false;
        }
        else {
            final AudioTrack newTrack = tracks.get(0);
            player.playTrack(newTrack);
            sendSongMessage(newTrack.getInfo());
        }

    }

    @Override
    public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
        super.onTrackException(player, track, exception);
        log.warn("Track exception!");
    }

    @Override
    public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
        super.onTrackStuck(player, track, thresholdMs);
        log.warn("Track stuck!");
    }

    public void addTrackToQueue(AudioTrack track) {
        tracks.add(track);
    }

    public void playFirstTrackInQueue() {
        log.debug("Playing first track in queue for guild " + guildID);
        isPlaying = true;
        final AudioTrack track = tracks.get(0);
        final AudioTrackInfo trackInfo = track.getInfo();
        player.playTrack(tracks.get(0));
        sendSongMessage(trackInfo);
    }
    private void sendSongMessage(AudioTrackInfo trackInfo) {
        Guild guild = MusicBot.musicBot.getJda().getGuildById(guildID);
        String channelID = MusicBot.musicBot.getSettingsManager().getSettingsFromGuildID(guildID).getCommandChannelID();
        TextChannel channel = guild.getTextChannelById(channelID);
        final long trackSeconds = trackInfo.length / 1000;
        final StringBuilder trackLength = new StringBuilder((trackSeconds / 60) + ":" + (trackSeconds % 60));
        //Adds a zero if the number of seconds is only one digit
        if (trackLength.toString().length() == 3) {
            trackLength.insert(2, "0");
        }
        EmbedBuilder resultingEmbed = new EmbedBuilder(CommandUtil.BASE_EMBED)
                .setTitle("Now Playing: ", trackInfo.uri)
                .addField("Title", trackInfo.title, true)
                .addField("Artist", trackInfo.author, true)
                .addField("Length", trackLength.toString(), true);
        channel.sendMessage(resultingEmbed.build()).queue();
    }
    public void clearAllTracks() {
        log.info("Clearing all tracks for " + guildID);
        tracks = Lists.newArrayList();
    }
}
