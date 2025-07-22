package it.uniroma3.siwmusic.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siwmusic.model.Song;
import it.uniroma3.siwmusic.repository.SongRepository;

@Service
public class SongService {

    @Autowired
    private SongRepository songRepository;
   
    public Optional<Song> findById(Long id) {
        return songRepository.findById(id);
    }

    public Song save(Song song) {
        return songRepository.save(song);
    }

    public void delete(Song song) {
        songRepository.delete(song);
    }

    public Iterable<Song> findAll() {
        return songRepository.findAll(); 
    }

    public Iterable<Song> findByAuthorId(Long authorId) {
        return songRepository.findByAuthorsId(authorId);
    }

    public Iterable<Song> findByAlbumName(String albumName) {
        return songRepository.findByAlbumName(albumName);
    }
    
    public Iterable<Song> findByGenre(String genre) {
        return songRepository.findByGenre(genre);
    }   

}