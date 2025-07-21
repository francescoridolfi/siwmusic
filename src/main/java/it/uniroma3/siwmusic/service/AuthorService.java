package it.uniroma3.siwmusic.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siwmusic.model.Author;
import it.uniroma3.siwmusic.repository.AuthorRepository;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    public Optional<Author> findById(Long id) {
        return authorRepository.findById(id);
    }

    public Author save(Author author) {
        return authorRepository.save(author);
    }

    public void delete(Author author) {
        authorRepository.delete(author);
    }

    public Iterable<Author> findAll() {
        return authorRepository.findAll();
    }

    public boolean existsById(Long id) {
        return authorRepository.existsById(id);
    }
    
}
