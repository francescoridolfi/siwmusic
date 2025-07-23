package it.uniroma3.siwmusic.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siwmusic.model.Playlist;
import it.uniroma3.siwmusic.model.Song;
import it.uniroma3.siwmusic.model.User;
import it.uniroma3.siwmusic.repository.PlaylistRepository;

@Service
public class PlaylistService {

    @Autowired
    private PlaylistRepository playlistRepository;

    public Optional<Playlist> findById(Long id) {
        return playlistRepository.findById(id);
    }

    public Iterable<Playlist> findByUserId(Long userId) {
        return playlistRepository.findByUserId(userId);
    }

    public Playlist save(Playlist playlist) {
        return playlistRepository.save(playlist);
    }

    public void delete(Playlist playlist) {
        playlistRepository.delete(playlist);
    }

    public Iterable<Playlist> findAll() {
        return playlistRepository.findAll();
    }
    
    public void createPlaylistForUser(User user) {
        Playlist playlist = new Playlist();
        playlist.setUser(user);
        playlistRepository.save(playlist);
    }

    public Playlist getOrCreateUserPlaylist(User user) {
        return playlistRepository.findByUser(user).orElseGet(() -> {
            Playlist p = new Playlist();
            p.setUser(user);
            return playlistRepository.save(p);
        });
    }

    @Transactional
    public void removeSongFromAllPlaylists(Song song) {
        ArrayList<Playlist> playlists = (ArrayList<Playlist>) playlistRepository.findAll();
        for (Playlist playlist : playlists) {
            if (playlist.getSongs().remove(song)) {
                playlistRepository.save(playlist);
            }
        }
    }

}