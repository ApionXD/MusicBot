package MusicBot.settings;

import lombok.Setter;

@Setter
public class Settings {
    private String prefix;
    private String guildID;

    public Settings() {
        prefix = "~";
    }
}
