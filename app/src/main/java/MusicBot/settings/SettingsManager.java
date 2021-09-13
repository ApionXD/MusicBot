package MusicBot.settings;

import MusicBot.MusicBot;
import MusicBot.file.FileManager;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
                        log.info(Files.readString(filePath));
                        return FileManager.GSON_INSTANCE.fromJson(reader, Settings.class);
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
}
