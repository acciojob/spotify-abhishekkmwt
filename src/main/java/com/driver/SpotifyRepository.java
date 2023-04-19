package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class SpotifyRepository {
    public HashMap<Artist, List<Album>> artistAlbumMap;
    public HashMap<Album, List<Song>> albumSongMap;
    public HashMap<Playlist, List<Song>> playlistSongMap;
    public HashMap<Playlist, List<User>> playlistListenerMap;
    public HashMap<User, Playlist> creatorPlaylistMap;
    public HashMap<User, List<Playlist>> userPlaylistMap;
    public HashMap<Song, List<User>> songLikeMap;

    public List<User> users;
    public List<Song> songs;
    public List<Playlist> playlists;
    public List<Album> albums;
    public List<Artist> artists;

    public SpotifyRepository() {
        //To avoid hitting apis multiple times, initialize all the hashmaps here with some dummy data
        artistAlbumMap = new HashMap<>();
        albumSongMap = new HashMap<>();
        playlistSongMap = new HashMap<>();
        playlistListenerMap = new HashMap<>();
        creatorPlaylistMap = new HashMap<>();
        userPlaylistMap = new HashMap<>();
        songLikeMap = new HashMap<>();

        users = new ArrayList<>();
        songs = new ArrayList<>();
        playlists = new ArrayList<>();
        albums = new ArrayList<>();
        artists = new ArrayList<>();
    }

    public User createUser(String name, String mobile) {
        User user = new User(name, mobile);
        users.add(user);
        return user;
    }

    public Artist createArtist(String name) {
        Artist artist = new Artist(name);
        artists.add(artist);
        return artist;
    }

    public Album createAlbum(String title, String artistName) {
        Album album = new Album(title);
        Artist artist = new Artist(artistName);
        if (artistAlbumMap.containsKey(artist)) {
            if (artistAlbumMap.get(artist) != null && !artistAlbumMap.get(artist).contains(album)) {
                List<Album> albums1= artistAlbumMap.get(artist);
                albums1.add(album);
                artistAlbumMap.put(artist,albums1);
            } else if (artistAlbumMap.get(artist) == null) {
                List<Album> albums1 = new ArrayList<>();
                albums1.add(album);
                artistAlbumMap.put(artist, albums1);
            }
        } else {
            List<Album> albums1 = new ArrayList<>();
            albums1.add(album);
            artistAlbumMap.put(artist, albums1);
        }
        if (!albums.contains(album)) {
            albums.add(album);
        }
        if (!artists.contains(artist)) {
            artists.add(artist);
        }
        return album;
    }

    public Song createSong(String title, String albumName, int length) throws Exception {
        Song song = new Song(title, length);
        Album album = new Album(albumName);
        if (albumSongMap.containsKey(album)) {
            if (albumSongMap.get(album) != null && !albumSongMap.get(album).contains(song)) {
                List<Song> songs1= albumSongMap.get(song);
                songs1.add(song);
                albumSongMap.put(album,songs1);
            } else if (albumSongMap.get(album) == null) {
                List<Song> songs1 = new ArrayList<>();
                songs1.add(song);
                albumSongMap.put(album, songs1);
            }
        } else {
            throw new Exception("Album does not exist");
        }
        if (!songs.contains(song)) {
            songs.add(song);
        }

        return song;
    }

    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {
        Playlist playlist = new Playlist(title);
        if (playlistSongMap.get(playlist) != null && playlistSongMap.containsKey(playlist)) {
            List<Song> songs1=playlistSongMap.get(playlist);
            for (Song song : songs) {
                if (song.getLength() == length) {
                    songs1.add(song);
                }
            }
            playlistSongMap.put(playlist,songs1);
        }
        else {
            List<Song> songs1 = new ArrayList<>();
            for (Song song : songs) {
                if (song.getLength() == length) {
                    songs1.add(song);
                }
            }
            playlistSongMap.put(playlist, songs1);
        }

        if (!playlists.contains(playlist)) {
            playlists.add(playlist);
        }

        User user = null;
        for (User user1 : users) {
            if (user1.getMobile().equals(mobile)) {
                user = user1;
            }
        }

        if (user == null) {
            throw new Exception("User does not exist");
        }

        //putting values in creatorPlaylistMap HashMap
        if (creatorPlaylistMap.containsKey(user) && creatorPlaylistMap.get(user) != playlist) {
            creatorPlaylistMap.put(user, playlist);
        } else if (!creatorPlaylistMap.containsKey(user)) {
            creatorPlaylistMap.put(user, playlist);
        }

        //putting values in playlistListenerMap HashMap
        List<User> Users=playlistListenerMap.get(playlist);
        if(playlistListenerMap.containsKey(playlist) && playlistListenerMap.get(playlist)!=null){
            if(!playlistListenerMap.get(playlist).contains(user)){
                Users.add(user);
            }
            playlistListenerMap.put(playlist,Users);
        }
        else{
            List<User> users1=new ArrayList<>();
            users1.add(user);
            playlistListenerMap.put(playlist,users1);
        }

        List<Playlist> playlistList =userPlaylistMap.get(user);
        if(userPlaylistMap.containsKey(user) && userPlaylistMap.get(user)!=null){
            if(!userPlaylistMap.get(user).contains(playlist)){
                playlistList.add(playlist);
            }
            userPlaylistMap.put(user,playlistList);
        }
        else{
            List<Playlist> playlists1=new ArrayList<>();
            playlistList.add(playlist);
            userPlaylistMap.put(user,playlists1);
        }

        if(!playlists.contains(playlist)){
            playlists.add(playlist);
        }
        return playlist;
    }

    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {
        Playlist playlist = new Playlist(title);
        List<Song> songs1=new ArrayList<>();
        for (Song song : songs) {
            for (String songTitle : songTitles) {
                if (song.getTitle().equals(songTitle)) {
                    {
                        if(playlistSongMap.containsKey(playlist)&& playlistSongMap.get(playlist)!=null)
                        {
                            if(playlistSongMap.get(playlist).contains(song)){
                                continue;
                            }
                            else{
                                playlistSongMap.get(playlist).add(song);
                            }
                        }
                        else{
                            songs1.add(song);
                            playlistSongMap.put(playlist,songs1);
                        }
                    }
                }
            }
        }
        User user = null;
        for (User user1 : users) {
            if (user1.getMobile().equals(mobile)) {
                user = user1;
            }
        }

        if (user == null) {
            throw new Exception("User does not exist");
        }

        //putting values in creatorPlaylistMap HashMap
        if (creatorPlaylistMap.containsKey(user) && creatorPlaylistMap.get(user) != playlist) {
            creatorPlaylistMap.put(user, playlist);
        } else if (!creatorPlaylistMap.containsKey(user)) {
            creatorPlaylistMap.put(user, playlist);
        }

        //putting values in playlistListenerMap HashMap
        List<User> Users=playlistListenerMap.get(playlist);
        if(playlistListenerMap.containsKey(playlist) && playlistListenerMap.get(playlist)!=null){
            if(!playlistListenerMap.get(playlist).contains(user)){
                Users.add(user);
            }
            playlistListenerMap.put(playlist,Users);
        }
        else{
            List<User> users1=new ArrayList<>();
            users1.add(user);
            playlistListenerMap.put(playlist,users1);
        }

        List<Playlist> playlistList =userPlaylistMap.get(user);
        if(userPlaylistMap.containsKey(user) && userPlaylistMap.get(user)!=null){
            if(!userPlaylistMap.get(user).contains(playlist)){
                playlistList.add(playlist);
            }
            userPlaylistMap.put(user,playlistList);
        }
        else{
            List<Playlist> playlists1=new ArrayList<>();
            playlistList.add(playlist);
            userPlaylistMap.put(user,playlists1);
        }

        if(!playlists.contains(playlist)){
            playlists.add(playlist);
        }

        return playlist;
    }


    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {
        User user = null;
        for (User user1 : users) {
            if (user1.getMobile().equals(mobile)) {
                user = user1;
            }
        }
        if (user == null) {
            throw new Exception("User does not exist");
        }

        Playlist playlist=null;
        for(Playlist playlist1 :playlists){
            if(playlist1.getTitle().equals(playlistTitle)){
                playlist=playlist1;
            }
        }

        if (playlist == null) {
            throw new Exception("Playlist does not exist");
        }

        //putting values in creatorPlaylistMap HashMap
        if (creatorPlaylistMap.containsKey(user) && creatorPlaylistMap.get(user) != playlist) {
            creatorPlaylistMap.put(user, playlist);
        } else if (!creatorPlaylistMap.containsKey(user)) {
            creatorPlaylistMap.put(user, playlist);
        }

        //putting values in playlistListenerMap HashMap
        List<User> Users=playlistListenerMap.get(playlist);
        if(playlistListenerMap.containsKey(playlist) && playlistListenerMap.get(playlist)!=null){
            if(!playlistListenerMap.get(playlist).contains(user)){
                Users.add(user);
            }
            playlistListenerMap.put(playlist,Users);
        }
        else{
            List<User> users1=new ArrayList<>();
            users1.add(user);
            playlistListenerMap.put(playlist,users1);
        }

        List<Playlist> playlistList =userPlaylistMap.get(user);
        if(userPlaylistMap.containsKey(user) && userPlaylistMap.get(user)!=null){
            if(!userPlaylistMap.get(user).contains(playlist)){
                playlistList.add(playlist);
            }
            userPlaylistMap.put(user,playlistList);
        }
        else{
            List<Playlist> playlists1=new ArrayList<>();
            playlistList.add(playlist);
            userPlaylistMap.put(user,playlists1);
        }

        if(!playlists.contains(playlist)){
            playlists.add(playlist);
        }
        return playlist;
    }

    public Song likeSong(String mobile, String songTitle) throws Exception {
        User user = null;
        for (User user1 : users) {
            if (user1.getMobile().equals(mobile)) {
                user = user1;
            }
        }
        if (user == null) {
            throw new Exception("User does not exist");
        }

        Song song=null;
        for(Song song1 :songs){
            if(song1.getTitle().equals(songTitle)){
                song=song1;
            }
        }

        if (song == null) {
            throw new Exception("Song does not exist");
        }

        List<User> users1=songLikeMap.get(song);
        if(songLikeMap.containsKey(song) && users1!=null){
            if(!users1.contains(user)){
                users1.add(user);
            }
            songLikeMap.put(song,users1);
        }
        else{
            List<User> list=new ArrayList<>();
            list.add(user);
            songLikeMap.put(song,list);
        }

        for(Artist artist : artists){
            for(Album album : artistAlbumMap.get(artist)){
                for(Song song1 : albumSongMap.get(album)){
                    if(song1.getTitle().equals(songTitle)){
                        artist.setLikes(artist.getLikes()+1);
                    }
                }
            }
        }
        return song;
    }


    public String mostPopularArtist() {
        int max=0;
        String name="";
        for(Artist artist : artists){
            if(artist.getLikes()>max){
                max= artist.getLikes();
                name=artist.getName();
            }
        }
        return name;
    }

    public String mostPopularSong() {
        String name="";
        for(Map.Entry<Song,List<User>> entry : songLikeMap.entrySet()){
            int count =0;

            if(entry.getValue().size() > count){
                count= entry.getValue().size();
                name=entry.getKey().getTitle();
            }
        }
        return name;
    }
}
