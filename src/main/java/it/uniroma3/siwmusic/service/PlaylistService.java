package it.uniroma3.siwmusic.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siwmusic.model.Playlist;
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

}
