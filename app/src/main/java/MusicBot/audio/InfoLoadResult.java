package MusicBot.audio;

import MusicBot.MusicBot;
import MusicBot.command.base.Command;
import MusicBot.command.base.CommandEvent;
import MusicBot.command.base.CommandUtil;
import MusicBot.command.base.reaction.ReactionCommand;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;

@Slf4j
public class InfoLoadResult extends BaseLoadResult{
    boolean isDone;
    public InfoLoadResult(CommandEvent e) {
        super(e);
        isDone = false;
    }

    @Override
    public void trackLoaded(AudioTrack track) {
        AudioTrackInfo info = track.getInfo();
        final long trackSeconds = info.length / 1000;
        final StringBuilder trackLength = new StringBuilder((trackSeconds / 60) + ":" + (trackSeconds % 60));
        if (trackLength.toString().length() == 3) {
            trackLength.insert(2, "0");
        }
        EmbedBuilder builder = new EmbedBuilder(CommandUtil.BASE_EMBED)
                .setTitle("Song Info:", info.uri)
                .addField("Title", info.title, true)
                .addField("Artist", info.author, true)
                .addField("Length", trackLength.toString(), true);
        if (this.getOrigEvent().getCommandCalled() instanceof ReactionCommand) {
            respondOrigChannelAndReact(builder.build(), (ReactionCommand) this.getOrigEvent().getCommandCalled());
        }
        isDone = true;
    }

    @Override
    public void playlistLoaded(AudioPlaylist playlist) {

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
}
