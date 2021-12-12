package MusicBot.command.audio;

import MusicBot.MusicBot;
import MusicBot.audio.MusicUtil;
import MusicBot.command.base.Command;
import MusicBot.command.base.CommandEvent;
import MusicBot.command.base.CommandUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

public class Part extends Command {
    private static final String NAME = "part";
    public static final String SHORT_HELP_DESC = "Disconnects MusicBot from all connected channels";
    public static final String LONG_HELP_DESC = "Disconnects MusicBot from all connected channels, and clears the queue";
    public Part() {
        this.setCommandName(NAME);
        this.addValidArgNum(0);
        this.setShortHelpDesc(SHORT_HELP_DESC);
        this.setLongHelpDesc(LONG_HELP_DESC);
    }
    @Override
    public void executeCommand(CommandEvent e) {
        super.executeCommand(e);
        final TextChannel origChannel = e.getOrigEvent().getChannel();
        final Guild g = e.getOrigEvent().getGuild();
        final MusicUtil musicUtil = MusicBot.musicBot.getMusicUtil();
        final String guildID = g.getId();
        g.getAudioManager().closeAudioConnection();
        EmbedBuilder builder = new EmbedBuilder(CommandUtil.BASE_EMBED).addField("Disconnected Music Bot from all channels!", "", false);
        origChannel.sendMessage(builder.build()).queue();
        musicUtil.getSchedulerFromID(guildID).clearAllTracks();
        musicUtil.getPlayerFromID(guildID).stopTrack();
    }
}
