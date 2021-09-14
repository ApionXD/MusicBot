package MusicBot.command.audio;

import MusicBot.MusicBot;
import MusicBot.audio.MusicUtil;
import MusicBot.command.base.Command;
import MusicBot.command.base.CommandEvent;
import MusicBot.command.base.CommandUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;

public class Part extends Command {
    private static final String NAME = "part";
    public Part() {
        this.setCommandName(NAME);
    }
    @Override
    public void executeCommand(CommandEvent e) {
        Guild g = e.getOrigEvent().getGuild();
        MusicUtil musicUtil = MusicBot.musicBot.getMusicUtil();
        String guildID = g.getId();
        g.getAudioManager().closeAudioConnection();
        EmbedBuilder builder = new EmbedBuilder(CommandUtil.BASE_EMBED).addField("Disconnected Music Bot from all channels!", "", false);
        e.getOrigEvent().getChannel().sendMessage(builder.build()).queue();
        musicUtil.getSchedulerFromID(guildID).clearAllTracks();
        musicUtil.getPlayerFromID(guildID).stopTrack();
    }
}
