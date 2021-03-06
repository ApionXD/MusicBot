package MusicBot.command.audio;

import MusicBot.MusicBot;
import MusicBot.audio.MusicUtil;
import MusicBot.command.base.Command;
import MusicBot.command.base.CommandEvent;
import MusicBot.command.base.CommandUtil;
import MusicBot.command.base.reaction.paged.PaginatedCommand;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class List extends PaginatedCommand {
    public static final String NAME = "list";
    public static final String SHORT_HELP_DESC = "Lists current songs in queue";
    public static final String LONG_HELP_DESC = "Gets a list of the the songs in queue and their positions";
    public List() {
        this.setCommandName(NAME);
        this.addValidArgNum(0);
        this.setShortHelpDesc(SHORT_HELP_DESC);
        this.setLongHelpDesc(LONG_HELP_DESC);
    }

    @Override
    public void executeCommand(CommandEvent e) {
        super.executeCommand(e);
        ArrayList<AudioTrack> tracks = MusicBot.musicBot.getMusicUtil().getSchedulerFromID(e.getOrigEvent().getGuild().getId()).getTracks();
        EmbedBuilder builder = new EmbedBuilder(CommandUtil.BASE_EMBED).setTitle("Music Queue:");
        StringBuilder fieldTitle = new StringBuilder();
        int index = 1;
        for(int i = 1; i < tracks.size(); i++) {
            AudioTrackInfo info = tracks.get(i).getInfo();
            int trackSeconds = (int) (info.length / 1000);
            final StringBuilder trackLength = new StringBuilder((trackSeconds / 60) + ":" + (trackSeconds % 60));
            if (trackLength.toString().length() == 3) {
                trackLength.insert(2, "0");
            }
            String title = info.title;
            fieldTitle.append(i);
            fieldTitle.append(":");
            if (title.length() >= 30) {
                fieldTitle.append(info.title.substring(0, 30));
                fieldTitle.append("...");
            }
            else {
                fieldTitle.append(title);
            }
            fieldTitle.append('\n');
            if (i % 10 == 0) {
                builder.addField("", fieldTitle.toString(), false);
                addPage(builder.build());
                builder = new EmbedBuilder(CommandUtil.BASE_EMBED);
                fieldTitle = new StringBuilder();
            }
            index = i;
        }
        if (index % 10 != 0) {
            builder.addField("", fieldTitle.toString(), false);
            addPage(builder.build());
        }

        printFirstPage(e);
    }
}
