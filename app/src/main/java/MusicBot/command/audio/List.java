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
    public List() {
        this.setCommandName(NAME);
        this.addValidArgNum(0);
    }

    @Override
    public void executeCommand(CommandEvent e) {
        super.executeCommand(e);
        ArrayList<AudioTrack> tracks = MusicBot.musicBot.getMusicUtil().getSchedulerFromID(e.getOrigEvent().getGuild().getId()).getTracks();
        EmbedBuilder builder = new EmbedBuilder(CommandUtil.BASE_EMBED).setTitle("Music Queue:");
        StringBuilder fieldTitle = new StringBuilder();
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
        }
        builder.addField("", fieldTitle.toString(), false);
        addPage(builder.build());
        Message message = e.getOrigEvent().getChannel().sendMessage(getPages().get(0)).complete();
        if (getPages().size() > 1) {
            message.addReaction(RIGHT_ARROW).queue();
        }
        String messageID = message.getId();
        MusicBot.musicBot.getReactionUtil().addReactionMessage(messageID, this);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                MusicBot.musicBot.getReactionUtil().removeReactionMessage(messageID);
            }
        };
        Timer timer = new Timer(true);
        timer.schedule(task, 30000);
    }
}
