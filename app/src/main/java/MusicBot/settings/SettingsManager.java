package MusicBot.settings;

import MusicBot.MusicBot;
import MusicBot.file.FileManager;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import net.dv8tion.jda.api.entities.Guild;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.List;

public class SettingsManager {
    private static Cache<String, Settings> settingMap;
    public static void initAllSettings() {
        settingMap = CacheBuilder.newBuilder()
                .maximumSize(100)
                .build(new CacheLoader<String, Settings>() {
                    @Override
                    public Settings load(String key) throws Exception {
                        String fileName = "settings/" + MusicBot.musicBot.getJda().getGuildById(key).getName() + ".json";
                        Path filePath = FileManager.FS.getPath(fileName);
                        FileReader reader = new FileReader(fileName);
                        return FileManager.GSON_INSTANCE.fromJson(reader, Settings.class);
                    }
                });

        List<Guild> guildList = MusicBot.musicBot.getJda().getGuilds();
        guildList.forEach(g -> FileManager.forceReadFile("settings\\" + g.getName() + ".json"));
    }

}
