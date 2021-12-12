package MusicBot.command;

import MusicBot.MusicBot;
import MusicBot.command.base.Command;
import MusicBot.command.base.CommandEvent;
import MusicBot.command.base.CommandUtil;
import MusicBot.command.base.reaction.paged.PaginatedCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

import java.util.*;

public class Help extends PaginatedCommand {
    private static final String NAME = "help";

    public Help() {
        this.setCommandName(NAME);
        this.addValidArgNum(0);
        this.addValidArgNum(1);
    }

    @Override
    public void executeCommand(CommandEvent e) {
        super.executeCommand(e);
        HashSet<Command> commands = MusicBot.musicBot.getCommandUtil().getCommands();
        int i = 1;
        EmbedBuilder builder = new EmbedBuilder(CommandUtil.BASE_EMBED);
        StringBuilder fieldTitle = new StringBuilder();
        Iterator<Command> setItr = commands.iterator();
        while (i < commands.size() && setItr.hasNext()) {
            fieldTitle.append(setItr.next().getCommandName());
            fieldTitle.append('\n');
            if (i % 10 == 0) {
                builder.addField(fieldTitle.toString(), "", false);
                addPage(builder.build());
                fieldTitle = new StringBuilder();
                builder = new EmbedBuilder(CommandUtil.BASE_EMBED);
            }
        }
        if (i % 10 != 0) {
            builder.addField("", fieldTitle.toString(), false);
            addPage(builder.build());
        }
        printFirstPage(e);
    }
}
