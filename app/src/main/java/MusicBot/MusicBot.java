package MusicBot;

import MusicBot.properties.PropManager;
import lombok.extern.slf4j.Slf4j;


import java.io.IOException;
import java.util.Properties;
@Slf4j

public class MusicBot {
    public static void main(String[] args) {
        final Properties botProperties = PropManager.loadPropertyFile();

    }
}
