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
    public Part() {
        this.setCommandName(NAME);
        this.addValidArgNum(0);
    }
    @Override
    public void executeCommand(CommandEvent e) {
        TextChannel origChannel = e.getOrigEvent().getChannel();
        if (!super.hasValidArgs(e.getWords().size() - 1)){
            EmbedBuilder builder = new EmbedBuilder(CommandUtil.BASE_EMBED).addField("You have entered the wrong amount of arguments for " + NAME,"", false);
            origChannel.sendMessage(builder.build()).queue();
            return;
        }
        Guild g = e.getOrigEvent().getGuild();
        MusicUtil musicUtil = MusicBot.musicBot.getMusicUtil();
        String guildID = g.getId();
        g.getAudioManager().closeAudioConnection();
        EmbedBuilder builder = new EmbedBuilder(CommandUtil.BASE_EMBED).addField("Disconnected Music Bot from all channels!", "", false);
        origChannel.sendMessage(builder.build()).queue();
        musicUtil.getSchedulerFromID(guildID).clearAllTracks();
        musicUtil.getPlayerFromID(guildID).stopTrack();
    }
}