package MusicBot.command;

import MusicBot.MusicBot;
import MusicBot.command.base.Command;
import MusicBot.command.base.CommandEvent;
import MusicBot.command.base.CommandUtil;
import MusicBot.command.base.reaction.paged.PaginatedCommand;
import MusicBot.settings.Settings;
import MusicBot.settings.SettingsManager;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.*;
@Slf4j
public class Help extends PaginatedCommand {
    private static final String NAME = "help";
    public static final String SHORT_HELP_DESC = "Lists all commands, or help for a specific command";
    public static final String LONG_HELP_DESC = "Lists all commands, or help for a specific command";
    public Help() {
        this.setCommandName(NAME);
        this.addValidArgNum(0);
        this.addValidArgNum(1);
        this.setShortHelpDesc(SHORT_HELP_DESC);
        this.setLongHelpDesc(LONG_HELP_DESC);
    }

    @Override
    public void executeCommand(CommandEvent e) {
        super.executeCommand(e);
        sendGenericHelp(e);
    }
    public void sendGenericHelp(CommandEvent e) {
        HashSet<Command> commands = MusicBot.musicBot.getCommandUtil().getCommands();
        ArrayList<MessageEmbed.Field> allfields = Lists.newArrayList();
        EmbedBuilder page = new EmbedBuilder(CommandUtil.BASE_EMBED);
        allfields.addAll(getFields(MusicBot.musicBot.getCommandUtil(), e));
        allfields.addAll(getFields(MusicBot.musicBot.getReactionUtil(), e));
        int index = 1;
        for (int i = 0; i < allfields.size(); i++) {
            page.addField(allfields.get(i));
            if (i % 10 == 0 && i != 0) {
                addPage(page.build());
                page = new EmbedBuilder(CommandUtil.BASE_EMBED);
            }
            index = i;
        }
        if (index % 10 != 0) {
            addPage(page.build());
            page = new EmbedBuilder(CommandUtil.BASE_EMBED);
        }
        printFirstPage(e);
    }
    private ArrayList<MessageEmbed.Field> getFields(CommandUtil utility, CommandEvent e) {
        HashSet<Command> commands = utility.getCommands();
        Iterator<Command> setItr = commands.iterator();
        ArrayList<MessageEmbed.Field> fields = Lists.newArrayList();
        Settings s = MusicBot.musicBot.getSettingsManager().getSettingsFromGuildID(e.getOrigEvent().getGuild().getId());
        int i = 0;
        while (i < commands.size() && setItr.hasNext()) {
            Command c = setItr.next();
            final String name = s.getPrefix() + c.getCommandName();
            MessageEmbed.Field field = new MessageEmbed.Field(name, c.getShortHelpDesc(), false);
            log.debug(field.toString());
            fields.add(field);
            i++;
        }
        return fields;
    }
}
