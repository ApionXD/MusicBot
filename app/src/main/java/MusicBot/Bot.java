package MusicBot;

import MusicBot.listener.ReadyListener;
import MusicBot.properties.PropManager;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;
import java.util.Properties;

@Slf4j @Getter
public class Bot {
    final Properties botProperties;
    final String botToken;
    final boolean setLight;
    JDA jda;
    public Bot() {
        botProperties = PropManager.loadBotPropertyFile();
        botToken = botProperties.getProperty("token");
        setLight = Boolean.getBoolean(botProperties.getProperty("slowMode"));

        try {
            if (setLight) {
                jda = JDABuilder.createLight(botToken).build();
            }
            else {
                jda = JDABuilder.createDefault(botToken).build();
            }
        } catch (LoginException e) {
            log.error("Token not valid! Please check your bot.properties file!");
            System.exit(1);
        }
        jda.addEventListener(new ReadyListener());
    }
}
