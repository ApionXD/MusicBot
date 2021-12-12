package MusicBot.command.audio;

import MusicBot.MusicBot;
import MusicBot.audio.MusicUtil;
import MusicBot.command.base.Command;
import MusicBot.command.base.CommandEvent;
import MusicBot.command.base.CommandUtil;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.managers.AudioManager;

@Slf4j
public class Play extends Command {
    public static final String NAME = "play";
    public static final String SHORT_HELP_DESC = "Plays a song/playlist";
    public static final String LONG_HELP_DESC = "Plays a song/playlist, no voting needed";
    public Play() {
        this.setCommandName(NAME);
        this.addValidArgNum(1);
        this.setShortHelpDesc(SHORT_HELP_DESC);
        this.setLongHelpDesc(LONG_HELP_DESC);
    }
    @Override
    public void executeCommand(CommandEvent e) {

        TextChannel origChannel = e.getOrigEvent().getChannel();
        log.debug("Getting musicutil");
        MusicUtil musicUtil = MusicBot.musicBot.getMusicUtil();
        String guildID = e.getOrigEvent().getGuild().getId();
        log.debug("Song loading...");
        String identifier = "";
        for (int i = 1; i < e.getWords().size(); i++) {
            String s = e.getWords().get(i);
            identifier += s + " ";
        }
        if (!identifier.contains("http://") && !identifier.contains("https://")) {
            identifier = "ytsearch:" + identifier;
        }
        musicUtil.queueSong(guildID, identifier, e);
    }

}
