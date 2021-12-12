package MusicBot.command.audio;

import MusicBot.MusicBot;
import MusicBot.audio.MusicUtil;
import MusicBot.command.base.Command;
import MusicBot.command.base.CommandEvent;
import net.dv8tion.jda.api.entities.TextChannel;

public class Skip extends Command {
    public static final String NAME = "skip";
    public Skip() {
        this.setCommandName(NAME);
        this.addValidArgNum(0);
    }
    @Override
    public void executeCommand(CommandEvent e) {
        super.executeCommand(e);
        MusicUtil musicUtil = MusicBot.musicBot.getMusicUtil();
        String guildID = e.getOrigEvent().getGuild().getId();
        musicUtil.skipSong(guildID);
    }
}
