package it.uniroma3.siwmusic.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.uniroma3.siwmusic.model.Song;

@Repository
public interface SongRepository extends CrudRepository<Song, Long> {

    public Iterable<Song> findByAuthorsId(Long authorsId);
    public Iterable<Song> findByAlbumName(String albumName);
    public Iterable<Song> findByGenre(String genre);


}