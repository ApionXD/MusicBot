package MusicBot.command.base;

import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.HashSet;

@Getter
@Setter
public abstract class Command {
    private String commandName;
    private HashSet<Integer> validArgNums;

    public Command() {
        validArgNums = Sets.newHashSet();
    }
    public abstract void executeCommand(CommandEvent e);
    public boolean hasValidArgs(int numArgs) {
        if (validArgNums.size() == 0) {
            return true;
        }
        else {
            return validArgNums.contains(numArgs);
        }
    }
    public void addValidArgNum(int i) {
        validArgNums.add(i);
    }

}
