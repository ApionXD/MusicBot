package MusicBot.listener;

import MusicBot.MusicBot;
import MusicBot.settings.Settings;
import MusicBot.settings.SettingsManager;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class ReadyListener extends ListenerAdapter {
    public ReadyListener() {

    }

    @SneakyThrows
    @Override
    public void onReady(@NotNull ReadyEvent event) {
        super.onReady(event);
        SettingsManager settingsMgr = new SettingsManager();
        Guild defGuild =MusicBot.musicBot.getJda().getGuilds().get(0);
        defGuild.getDefaultChannel().sendMessage("The current prefix is: " + settingsMgr.getSettingMap().get(defGuild.getId()).getPrefix()).queue();
    }
}
