package MusicBot.listener;

import MusicBot.settings.SettingsManager;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class ReadyListener extends ListenerAdapter {
    public ReadyListener() {

    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        super.onReady(event);
        SettingsManager.initAllSettings();
    }
}
