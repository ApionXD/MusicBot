package MusicBot.command;

import MusicBot.command.base.Command;
import MusicBot.command.base.CommandEvent;
import MusicBot.command.base.CommandUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

public class HelloWorld extends Command {
    private static final String NAME = "hello";
    public HelloWorld() {
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

        MessageEmbed resultEmbed = new EmbedBuilder(CommandUtil.BASE_EMBED).addField("", "Hello, world!", false).build();
        origChannel.sendMessage(resultEmbed).queue();
    }

}
