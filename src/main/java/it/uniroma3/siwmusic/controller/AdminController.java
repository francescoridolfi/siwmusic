package it.uniroma3.siwmusic.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siwmusic.model.Author;
import it.uniroma3.siwmusic.model.Song;
import it.uniroma3.siwmusic.service.AuthorService;
import it.uniroma3.siwmusic.service.SongService;

@Controller
@RequestMapping("/admin")
public class AdminController {
	@Autowired
    private SongService songService;
    @Autowired
    private AuthorService authorService;

    public AdminController(SongService songService) {
        this.songService = songService;
    }

    @Value("${siwbooks.upload.dir}")
    private String uploadDir;

    @GetMapping("/add-song")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String showAddSongForm(Model model) {
        model.addAttribute("song", new Song());
        model.addAttribute("authors", authorService.findAll());
        return "admin/add-song";
    }

    @PostMapping("/add-song")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String addSong(@ModelAttribute("song") Song song,
                          @RequestParam("audioFile") MultipartFile audioFile,
                          @RequestParam("coverImage") MultipartFile coverImage,
                          @RequestParam("authors") List<Long> authorIds) throws IOException {

        // Salva file audio
        String audioFileName = System.currentTimeMillis() + "_" + audioFile.getOriginalFilename();
        Path audioPath = Paths.get(uploadDir + "/audio/" + audioFileName);
        Files.createDirectories(audioPath.getParent());
        Files.write(audioPath, audioFile.getBytes());
        song.setFilePath("audio/" + audioFileName);

        // Salva immagine copertina
        String imageFileName = System.currentTimeMillis() + "_" + coverImage.getOriginalFilename();
        Path imagePath = Paths.get(uploadDir + "/images/" + imageFileName);
        Files.createDirectories(imagePath.getParent());
        Files.write(imagePath, coverImage.getBytes());
        song.setCoverImagePath("images/" + imageFileName);
        
        Iterable<Author> iterableAuthors = authorService.findAllById(authorIds);
        Set<Author> selectedAuthors = new HashSet<>();
        iterableAuthors.forEach(selectedAuthors::add);
        song.setAuthors(selectedAuthors);



        songService.save(song);
        return "redirect:/";
    }
    
    @GetMapping("/add-author")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String showAddAuthorForm(Model model) {
        model.addAttribute("author", new Author());
        return "admin/add-author";
    }

    @PostMapping("/add-author")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String addAuthor(@ModelAttribute Author author) {
        authorService.save(author);
        return "redirect:/admin/add-song";
    }

}
