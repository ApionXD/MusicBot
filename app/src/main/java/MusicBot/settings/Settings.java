package MusicBot.settings;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Settings {
    private String prefix;
    private String guildID;

    public Settings() {
        prefix = "~";
    }
}
