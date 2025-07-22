package it.uniroma3.siwmusic.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.uniroma3.siwmusic.model.Author;

@Repository
public interface AuthorRepository extends CrudRepository<Author, Long> {

    

}