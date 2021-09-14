package MusicBot.audio;

import MusicBot.audio.JDASendHandler;
import MusicBot.command.CommandEvent;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Slf4j
public class MusicUtil {
    //Memory overhead here might be unnecessary, maybe just make it a map.
    private LoadingCache<String, AudioPlayerManager> managers;
    //Might eventually need to be a list of audio players
    private LoadingCache<String, AudioPlayer> players;
    public MusicUtil() {
        managers = CacheBuilder.newBuilder().maximumSize(20).build(new CacheLoader<String, AudioPlayerManager>() {
            @Override
            public AudioPlayerManager load(String key) throws Exception {
                DefaultAudioPlayerManager newManager = new DefaultAudioPlayerManager();
                AudioSourceManagers.registerRemoteSources(newManager);
                return newManager;
            }
        });
        players = CacheBuilder.newBuilder().maximumSize(20).build(new CacheLoader<String, AudioPlayer>() {
            @Override
            public AudioPlayer load(String key) throws Exception {
                return managers.get(key).createPlayer();
            }
        });
    }
    public JDASendHandler getSendHandler(String guildID) {
        try {
            return new JDASendHandler(players.get(guildID));
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
    //Returns either a success or failure message.
    public LoadResult playSong(String guildID, String link, CommandEvent e) {
        try {
            AudioPlayerManager mgr = managers.get(guildID);
            AudioPlayer player = players.get(guildID);
            LoadResult result = new LoadResult(player, e);
            mgr.loadItem(link, result);
            return result;
        } catch (ExecutionException exception) {
            exception.printStackTrace();
        }
        return null;
    }
}

