package MusicBot.settings;

import MusicBot.MusicBot;
import MusicBot.file.FileManager;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

@Getter
@Slf4j
public class SettingsManager {

    private LoadingCache<String, Settings> settingMap;


    public SettingsManager() {
        genCache();
        initSettings();
    }
    private void genCache() {
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
        List<Guild> guildList = MusicBot.musicBot.getJda().getGuilds();
        guildList.forEach(g -> {
            Path settingFile = FileManager.forceReadFile("settings\\" + g.getName() + ".json");
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

}
