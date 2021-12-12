package MusicBot.command;

import MusicBot.MusicBot;
import MusicBot.command.base.CommandEvent;
import MusicBot.command.base.CommandUtil;
import MusicBot.command.base.reaction.ReactionCommand;
import MusicBot.command.base.reaction.ReactionEvent;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

@Slf4j
public class HelloWorldReaction extends ReactionCommand {
    private static String NAME = "helloreact";
    public HelloWorldReaction() {
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
        MessageEmbed resultEmbed = new EmbedBuilder(CommandUtil.BASE_EMBED)
                .addField("", "Hello, world!", false)
                .addField("", "React to this message and I will print the name of the reaction!", false)
                .build();
        origChannel.sendMessage(resultEmbed).queue(message -> {
            MusicBot.musicBot.getReactionUtil().addReactionMessage(message.getId(), this);
            log.debug(message.getId() + " added to listened messages");
        });
        ;
    }

    @Override
    public void handleReactionEvent(ReactionEvent e) {
        e.getOrigEvent().getChannel().sendMessage(e.getReaction().getReactionEmote().getAsCodepoints()).queue();
    }
}
