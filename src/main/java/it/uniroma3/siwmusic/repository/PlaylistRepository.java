package it.uniroma3.siwmusic.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.uniroma3.siwmusic.model.Playlist;
import it.uniroma3.siwmusic.model.User;

@Repository
public interface PlaylistRepository extends CrudRepository<Playlist, Long> {

    public Iterable<Playlist> findByUserId(Long userId);

    Optional<Playlist> findByUser(User user);

}