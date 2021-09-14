package MusicBot;

import MusicBot.audio.MusicUtil;
import MusicBot.command.CommandUtil;
import MusicBot.command.HelloWorld;
import MusicBot.command.Play;
import MusicBot.listener.MessageListener;
import MusicBot.properties.PropManager;
import MusicBot.settings.SettingsManager;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;
import java.util.Properties;

@Slf4j @Getter
public class Bot {
    private SettingsManager settingsManager;
    private CommandUtil commandUtil;
    private MusicUtil musicUtil;

    private final Properties botProperties;
    private final String botToken;
    private final boolean setLight;
    JDA jda;

    public Bot() {
        botProperties = PropManager.loadBotPropertyFile();
        botToken = botProperties.getProperty("token");
        setLight = Boolean.getBoolean(botProperties.getProperty("slowMode"));

        try {
            if (setLight) {
                log.info("slowMode flag set in properties, launching in slow mode");
                jda = JDABuilder.createLight(botToken).build();
            }
            else {
                jda = JDABuilder.createDefault(botToken).build();
            }
        }
        catch (LoginException e) {
            log.error("Token not valid! Please check your bot.properties file!");
            System.exit(1);
        }
        try {
            jda.awaitReady();
            log.info("JDA is ready!");
        }
        catch (InterruptedException e) {
            log.error(e.toString());
            log.debug("Error waiting for JDA to be ready.");
        }

    }
    public void addUtil(){
        jda.addEventListener(new MessageListener());
        settingsManager = new SettingsManager();
        commandUtil = new CommandUtil();
        musicUtil = new MusicUtil();
        commandUtil.addCommands(new HelloWorld());
        commandUtil.addCommands(new Play());
    }
}
