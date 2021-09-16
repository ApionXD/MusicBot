package MusicBot.command.base.reaction;

import MusicBot.MusicBot;
import lombok.Getter;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

public class ReactionEvent {
    private final ReactionCommand reactionCommandCalled;
    @Getter
    private final GuildMessageReactionAddEvent origEvent;
    @Getter
    private final MessageReaction reaction;
    public ReactionEvent(GuildMessageReactionAddEvent e) {
        reactionCommandCalled = MusicBot.musicBot.getReactionUtil().getReactionCommandFromID(e.getMessageId());
        reaction = e.getReaction();
        origEvent = e;
    }
    public void runReactionCommand() {
        reactionCommandCalled.handleReactionEvent(this);
    }
}
