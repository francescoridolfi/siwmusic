package it.uniroma3.siwmusic.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @NoArgsConstructor
public class Song {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private String genre;
    private String albumName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;

    private String filePath;
    private String coverImagePath;
    
    @ManyToMany
    @JoinTable(
    	    name = "song_author",
    	    joinColumns = @JoinColumn(name = "song_id"),
    	    inverseJoinColumns = @JoinColumn(name = "author_id")
    	)
    private List<Author> authors = new ArrayList<>();

}