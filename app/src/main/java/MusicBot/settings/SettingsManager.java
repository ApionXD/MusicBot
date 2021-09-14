package MusicBot.settings;

import MusicBot.MusicBot;
import MusicBot.file.FileManager;
import ch.qos.logback.core.util.FileUtil;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Getter
@Slf4j
public class SettingsManager {
    private LoadingCache<String, Settings> settingMap;


    public SettingsManager() {
        initSettings();
        genCache();
        log.debug("Finished initializing SettingsManager!");
    }
    private void genCache() {
        log.debug("Generating cache!");
        settingMap = CacheBuilder.newBuilder()
                .maximumSize(100)
                .build(new CacheLoader<String, Settings>() {
                    @Override
                    public Settings load(String key) throws Exception {
                        log.warn("Guild " + key + " settings is not in cache! Attempting to fetch it.");
                        String fileName = "settings\\" + MusicBot.musicBot.getJda().getGuildById(key).getName() + ".json";
                        Path filePath = FileManager.forceReadFile(fileName);
                        FileReader reader = new FileReader(fileName);
                        Settings result = FileManager.GSON_INSTANCE.fromJson(reader, Settings.class);
                        return result;
                    }
                });
    }
    //Loops through all guilds, generates missing settings files.
    private void initSettings() {
        log.debug(String.valueOf(MusicBot.musicBot == null));
        List<Guild> guildList = MusicBot.musicBot.getJda().getGuilds();
        log.info("Found " + guildList.size() + " guilds");
        guildList.forEach(g -> {
            Path settingFile = FileManager.forceReadFile("settings\\" + g.getName() + ".json");
            log.info("Found setting file at " + settingFile);
            FileWriter settingsWriter = null;
            try {
                if (Files.readString(settingFile).length() == 0) {
                    try {
                        settingsWriter = new FileWriter(settingFile.toFile());
                        log.warn("Settings file for " + g.getName() + " is empty, generating a default config.");
                        log.warn("Settings file will be at: " + settingFile);
                        log.warn(Files.readString(settingFile));
                        Settings defaultSettings = new Settings();
                        log.debug("Generating complex default settings.");
                        genDefaultComplexSettings(defaultSettings, g.getId());
                        String outputJson = FileManager.GSON_INSTANCE.toJson(defaultSettings);
                        settingsWriter.write(outputJson);
                        settingsWriter.close();
                    }
                    catch (IOException e) {
                        log.error(e.toString());
                    }
                }
            }
            catch (IOException e) {
                log.error(e.toString());
            }
        });
    }
    public Settings getSettingsFromGuildID(String id) {
        try {
            return settingMap.get(id);
        }
        catch (ExecutionException e) {
            log.error(e.toString());
        }
        return null;
    }
    private void genDefaultComplexSettings(Settings s, String guildID) {
        final String channelID = MusicBot.musicBot.getJda().getGuildById(guildID).getDefaultChannel().getId();
        log.debug("Default command channel is: " + channelID);
        s.setCommandChannelID(channelID);
    }
    public void saveSettingsForGuild(String guildID) {
        Settings s;
        StringBuilder fileName = new StringBuilder("settings\\");

        try {
            fileName.append(MusicBot.musicBot.getJda().getGuildById(guildID).getName());
            fileName.append(".json");
            Path file = FileManager.openFile(fileName.toString());

            s = settingMap.get(guildID);
            log.info("Writing new settings to " + fileName.toString());
            FileWriter writer = new FileWriter(file.toFile());
            FileManager.GSON_INSTANCE.toJson(s, writer);
            writer.close();
            //log.info("Old settings file: " +  Files.readString(file));
        }
        catch (ExecutionException | IOException exception) {
            log.error(exception.getMessage());
        }
    }
}
