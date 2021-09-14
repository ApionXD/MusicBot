package MusicBot.command.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Command {
    private String commandName;

    public Command() {
    }
    public abstract void executeCommand(CommandEvent e);

}
