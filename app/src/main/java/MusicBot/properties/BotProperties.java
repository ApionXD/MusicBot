package MusicBot.properties;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Properties;
@Slf4j
public class BotProperties extends Properties {
    private final HashMap<String, String> propertyDefaults;
    public BotProperties() {
        super();
        propertyDefaults = Maps.newHashMap();
        propertyDefaults.put("token", "");
        propertyDefaults.put("slowMode", "false");
    }
    public void updateProperties() {
        this.propertyDefaults.keySet().forEach(s -> {
            if (!this.containsKey(s)) {
                log.warn("Adding " + s + " to bot.properties");
                this.put(s, propertyDefaults.get(s));
            }
        });
    }


}
