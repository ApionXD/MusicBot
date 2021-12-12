package MusicBot.command.base.reaction.paged;

import MusicBot.command.base.reaction.ReactionCommand;
import MusicBot.command.base.reaction.ReactionEvent;
import com.google.common.collect.Lists;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.List;

public abstract class PaginatedCommand extends ReactionCommand {
    protected static String LEFT_ARROW = "U+2b05";
    protected static String RIGHT_ARROW = "U+27a1";
    @Getter
    private List<MessageEmbed> pages;
    int curPage = 1;
    public PaginatedCommand() {
        pages = Lists.newArrayList();
    }
    public void addPage(MessageEmbed page) {
        pages.add(page);
    }

    @Override
    public void handleReactionEvent(ReactionEvent e) {
        String reaction = e.getReaction().getReactionEmote().getAsCodepoints();
        if (reaction.equals(LEFT_ARROW) && curPage != 1) {
            curPage--;
        }
        if (reaction.equals(RIGHT_ARROW) && curPage < pages.size()) {
            curPage++;
        }
        Message message = e.getOrigEvent().retrieveMessage().complete();
        message.clearReactions().complete();
        message.editMessage(pages.get(curPage - 1)).complete();
        if (curPage != 0) {
            message.addReaction(LEFT_ARROW).queue();
        }
        if (curPage != pages.size()) {
            message.addReaction(RIGHT_ARROW).queue();
        }
    }
}
