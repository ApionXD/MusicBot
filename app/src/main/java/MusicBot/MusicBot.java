package MusicBot;

import MusicBot.settings.SettingsManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j

public class MusicBot {
    public static Bot musicBot;
    public static void main(String[] args) {
        musicBot = new Bot();
    }
}
