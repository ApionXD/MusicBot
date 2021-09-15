package MusicBot.properties;

import MusicBot.file.FileManager;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Path;
import java.util.Properties;

@Slf4j
public class PropManager {
    public static final String PROP_FILE_PATH = "bot.properties";

    //Reads/creates a new properties files and checks that it has all values that are needed.
    @SneakyThrows
    public static BotProperties loadBotPropertyFile() {
        Path propFile = FileManager.forceReadFile(PROP_FILE_PATH);
        FileReader propReader =  new FileReader(propFile.toFile());
        BotProperties properties = new BotProperties();
        properties.load(propReader);
        properties.updateProperties();
        saveProperties(properties, propFile);
        return properties;
    }
    //There isn't actually a way for IOExceptions to be thrown here, they are handled when all property files are read
    //No real reason to use sneakythrows here though, this could be easily wrapped in a try/catch
    @SneakyThrows
    public static void saveProperties(Properties properties, Path propFile) {
        properties.store(new FileWriter(propFile.toFile()), "");
    }
}
