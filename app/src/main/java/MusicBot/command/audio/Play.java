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
    public Play() {
        this.setCommandName(NAME);
        this.addValidArgNum(1);
    }
    @Override
    public void executeCommand(CommandEvent e) {
        super.executeCommand(e);
        TextChannel origChannel = e.getOrigEvent().getChannel();
        log.debug("Getting musicutil");
        MusicUtil musicUtil = MusicBot.musicBot.getMusicUtil();
        String guildID = e.getOrigEvent().getGuild().getId();
        log.debug("Song loading...");
        musicUtil.queueSong(guildID, e.getWords().get(1), e);
    }

}
