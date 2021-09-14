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

import java.util.concurrent.Future;

@Getter
@Slf4j
public class LoadResult implements AudioLoadResultHandler {
    private final AudioPlayer player;
    private final CommandEvent event;
    @Setter
    private Future<Void> loadFuture;
    public LoadResult(AudioPlayer player, CommandEvent e) {
        this.player = player;
        event = e;
    }
    @Override
    public void trackLoaded(AudioTrack track) {
        final AudioTrackInfo trackInfo = track.getInfo();
        final long trackSeconds = trackInfo.length / 1000;
        final String trackLength = (trackSeconds / 60) + ":" + (trackSeconds % 60);
        EmbedBuilder resultingEmbed = new EmbedBuilder(CommandUtil.BASE_EMBED)
                .setTitle("Now Playing: ", track.getInfo().uri)
                .addField("Title", trackInfo.title, true)
                .addField("Artist", trackInfo.author, true)
                .addField("Length", trackLength, true);
        event.getOrigEvent().getChannel().sendMessage(resultingEmbed.build()).queue();
        player.playTrack(track);
    }

    @Override
    public void playlistLoaded(AudioPlaylist playlist) {
        final AudioTrack track = playlist.getTracks().get(0);
        final AudioTrackInfo trackInfo = track.getInfo();
        final long trackSeconds = trackInfo.length / 1000;
        final String trackLength = (trackSeconds / 60) + ":" + (trackSeconds % 60);
        EmbedBuilder resultingEmbed = new EmbedBuilder(CommandUtil.BASE_EMBED)
                .setTitle("Now Playing: ", track.getInfo().uri)
                .addField("Title", trackInfo.title, true)
                .addField("Artist", trackInfo.author, true)
                .addField("Length", trackLength, true);

        event.getOrigEvent().getChannel().sendMessage(resultingEmbed.build()).queue();
        player.playTrack(track);
    }

    @Override
    public void noMatches() {
        EmbedBuilder resultingEmbed = new EmbedBuilder(CommandUtil.BASE_EMBED).setDescription("Couldn't find a song at that link!");
        event.getOrigEvent().getChannel().sendMessage(resultingEmbed.build()).queue();
    }

    @Override
    public void loadFailed(FriendlyException exception) {
        EmbedBuilder resultingEmbed = new EmbedBuilder(CommandUtil.BASE_EMBED).addField("Command failed with exception: ", exception.getMessage(), true);
        event.getOrigEvent().getChannel().sendMessage(resultingEmbed.build()).queue();
    }
}
