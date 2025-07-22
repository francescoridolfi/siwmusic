package it.uniroma3.siwmusic.service;

import java.util.List;
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

    public List<Song> findAll() {
        return (List<Song>) songRepository.findAll(); 
    }

    public List<Song> findByAuthorId(Long authorId) {
        return (List<Song>)  songRepository.findByAuthorsId(authorId);
    }

    public List<Song> findByAlbumName(String albumName) {
        return (List<Song>)  songRepository.findByAlbumName(albumName);
    }
    
    public Iterable<Song> findByGenre(String genre) {
        return songRepository.findByGenre(genre);
    }   

}