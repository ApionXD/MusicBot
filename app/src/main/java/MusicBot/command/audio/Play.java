package MusicBot.command.audio;

import MusicBot.MusicBot;
import MusicBot.audio.MusicUtil;
import MusicBot.command.base.Command;
import MusicBot.command.base.CommandEvent;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.managers.AudioManager;

@Slf4j
public class Play extends Command {
    public static final String NAME = "play";
    public Play() {
        this.setCommandName(NAME);
    }
    @Override
    public void executeCommand(CommandEvent e) {
        log.debug("Getting musicutil");
        MusicUtil musicUtil = MusicBot.musicBot.getMusicUtil();
        String guildID = e.getOrigEvent().getGuild().getId();
        //Might have concurrency issues, but that shouldn't be a problem.
        AudioManager m = e.getOrigEvent().getGuild().getAudioManager();
        log.debug("Setting sound handler.");
        m.setSendingHandler(musicUtil.getSendHandler(guildID));
        log.debug("Joining channel.");
        m.openAudioConnection(e.getOrigEvent().getGuild().getVoiceChannels().get(0));
        log.debug("Song loading...");
        musicUtil.queueSong(guildID, e.getWords().get(1), e);
    }

}
