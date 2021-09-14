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
    //All of these caches map a guildID to their corresponding objects
    //Memory overhead here might be unnecessary, maybe just make it a map.
    private LoadingCache<String, AudioPlayerManager> managers;
    //Might eventually need to be a list of audio players
    //These caches limit a guild to having only one player/scheduler each
    //To get around this, we need to make the String map to a list of AudioPlayers/TrackSchedulers, then we need a way to map a player to a scheduler

    private LoadingCache<String, AudioPlayer> players;
    private LoadingCache<String, TrackScheduler> schedulers;
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
        schedulers = CacheBuilder.newBuilder().maximumSize(20).build(new CacheLoader<String, TrackScheduler>() {
            @Override
            public TrackScheduler load(String key) throws Exception {
                TrackScheduler scheduler = new TrackScheduler(key);
                players.get(key).addListener(scheduler);
                return scheduler;
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
    public void queueSong(String guildID, String link, CommandEvent e) {
        try {
            AudioPlayerManager mgr = managers.get(guildID);
            AudioPlayer player = players.get(guildID);
            TrackScheduler scheduler = schedulers.get(guildID);
            LoadResult result = new LoadResult(e);
            mgr.loadItem(link, result);
        }
        catch (ExecutionException exception) {
            exception.printStackTrace();
        }
    }
    public AudioPlayerManager getManagerFromID(String id){
        try {
            return managers.get(id);
        } catch (ExecutionException exception) {
            exception.printStackTrace();
        }
        return null;
    }
    public AudioPlayer getPlayerFromID(String id){
        try {
            return players.get(id);
        } catch (ExecutionException exception) {
            exception.printStackTrace();
        }
        return null;
    }
    public TrackScheduler getSchedulerFromID(String s) {
        try {
            return schedulers.get(s);
        } catch (ExecutionException exception) {
            exception.printStackTrace();
        }
        return null;
    }

}

