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
    public static final String SHORT_HELP_DESC = "Sets the prefix to invoke the bot";
    public static final String LONG_HELP_DESC = "Sets the prefix to invoke the bot";
    public SetPrefix() {
        this.setCommandName(NAME);
        this.addValidArgNum(1);
        this.setShortHelpDesc(SHORT_HELP_DESC);
        this.setLongHelpDesc(LONG_HELP_DESC);
    }
    @Override
    public void executeCommand(CommandEvent e) {
        final TextChannel origChannel = e.getOrigEvent().getChannel();
        final String newPrefix = e.getWords().get(1);
        final String guildID = e.getOrigEvent().getGuild().getId();
        final Settings s = MusicBot.musicBot.getSettingsManager().getSettingsFromGuildID(e.getOrigEvent().getGuild().getId());
        log.info("Changing prefix for guild " + guildID + " to " + newPrefix);
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
