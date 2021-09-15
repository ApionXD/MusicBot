package MusicBot.command;

import MusicBot.MusicBot;
import MusicBot.command.base.Command;
import MusicBot.command.base.CommandEvent;
import MusicBot.command.base.CommandUtil;
import MusicBot.settings.Settings;
import MusicBot.settings.SettingsManager;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

@Slf4j
public class SetPrefix extends Command {
    private static final String NAME = "setprefix";
    public SetPrefix() {
        this.setCommandName(NAME);
        this.addValidArgNum(1);
    }
    @Override
    public void executeCommand(CommandEvent e) {
        TextChannel origChannel = e.getOrigEvent().getChannel();
        if (!super.hasValidArgs(e.getWords().size() - 1)){
            EmbedBuilder builder = new EmbedBuilder(CommandUtil.BASE_EMBED).addField("You have entered the wrong amount of arguments for " + NAME,"", false);
            origChannel.sendMessage(builder.build()).queue();
            return;
        }
        String newPrefix = e.getWords().get(1);
        String guildID = e.getOrigEvent().getGuild().getId();
        log.info("Changing prefix for guild " + guildID + " to " + newPrefix);
        Settings s = MusicBot.musicBot.getSettingsManager().getSettingsFromGuildID(e.getOrigEvent().getGuild().getId());
        s.setPrefix(newPrefix);
        MusicBot.musicBot.getSettingsManager().saveSettingsForGuild(guildID);
        boolean success = s.getPrefix().equals(newPrefix);
        EmbedBuilder builder = new EmbedBuilder(CommandUtil.BASE_EMBED);
        if (success) {
            builder.addField("Successfully set prefix to " + newPrefix, "", false);
        }
        else {
            builder.addField("Failed to set prefix!", "", false);
        }
        origChannel.sendMessage(builder.build()).queue();
    }
}
