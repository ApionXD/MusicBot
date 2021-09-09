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

    @SneakyThrows
    public static void createDefaultProperties(Path propFile) {
        Properties defaultProperties = new BotProperties();
        defaultProperties.store(new FileWriter(propFile.toFile()), "");
    }
    //There isn't actually a way for IOExceptions to be thrown here, they are handled by forceReadFile()
    @SneakyThrows
    public static Properties loadPropertyFile() {
        Path propFile = FileManager.forceReadFile(PROP_FILE_PATH);
        FileReader propReader =  new FileReader(propFile.toFile());
        if (!propReader.ready()) {
            log.warn("Properties file is empty!");
            PropManager.createDefaultProperties(propFile);
        }
        Properties properties = new Properties();
        properties.load(propReader);
        return properties;
    }
}
