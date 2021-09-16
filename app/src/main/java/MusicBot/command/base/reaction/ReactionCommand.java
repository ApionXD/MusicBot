package MusicBot.command.base.reaction;

import MusicBot.command.base.Command;

public abstract class ReactionCommand extends Command {
    public abstract void handleReactionEvent(ReactionEvent e);
}
