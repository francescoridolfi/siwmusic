package it.uniroma3.siwmusic.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.uniroma3.siwmusic.model.Playlist;

@Repository
public interface PlaylistRepository extends CrudRepository<Playlist, Long> {

    public Iterable<Playlist> findByUserId(Long userId);

}