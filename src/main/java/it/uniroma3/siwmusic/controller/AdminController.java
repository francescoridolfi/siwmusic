package it.uniroma3.siwmusic.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.uniroma3.siwmusic.model.Author;
import it.uniroma3.siwmusic.model.Song;
import it.uniroma3.siwmusic.model.User;
import it.uniroma3.siwmusic.model.enums.Role;
import it.uniroma3.siwmusic.service.AuthorService;
import it.uniroma3.siwmusic.service.PlaylistService;
import it.uniroma3.siwmusic.service.SongService;
import it.uniroma3.siwmusic.utils.BaseController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/admin")
public class AdminController extends BaseController {
	@Autowired
    private SongService songService;
    @Autowired
    private AuthorService authorService;
    @Autowired
    private PlaylistService playlistService;


    @Value("${siwbooks.upload.dir}")
    private String uploadDir;

    @GetMapping("/add-song")
    public String showAddSongForm(Model model) {
        User user = getUser();
        if(user == null || !user.getRole().equals(Role.ROLE_ADMIN))
            return "redirect:/";

        model.addAttribute("song", new Song());
        model.addAttribute("formActionUrl", "/admin/add-song");
        model.addAttribute("isEditing", false);
        model.addAttribute("authors", authorService.findAll());
        return "admin/add-song";
    }

    @GetMapping("/edit-song/{id}")
    public String showEditSongForm(
        @PathVariable Long id,
        RedirectAttributes redirectAttributes,
        Model model
    ) {
        User user = getUser();
        if(user == null || !user.getRole().equals(Role.ROLE_ADMIN))
            return "redirect:/";

        Song song = songService.findById(id).orElse(null);

        if(song == null)
            return redirectWithError("Error", "Song not found", redirectAttributes, "redirect:/");

        model.addAttribute("song", song);
        model.addAttribute("formActionUrl", "/admin/edit-song/" + id);
        model.addAttribute("isEditing", true);
        model.addAttribute("authors", authorService.findAll());
        return "admin/add-song";
    }

    @PostMapping("/add-song")
    public String addSong(
        @ModelAttribute("song") Song song,
        @RequestParam("audioFile") MultipartFile audioFile,
        @RequestParam("coverImage") MultipartFile coverImage,
        @RequestParam("authors") List<Long> authorIds,
        RedirectAttributes redirectAttributes
    ) throws IOException {

        User user = getUser();
        if(user == null || !user.getRole().equals(Role.ROLE_ADMIN))
            return "redirect:/";

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
        List<Author> selectedAuthors = new ArrayList<>();
        iterableAuthors.forEach(selectedAuthors::add);
        song.setAuthors(selectedAuthors);



        songService.save(song);
        return redirectWithSuccess("Success", "Song created successfully", redirectAttributes, "redirect:/");
    }

    @PostMapping("/edit-song/{id}")
    public String editSong(
        @PathVariable Long id,
        @ModelAttribute("song") Song songForm,
        @RequestParam("audioFile") MultipartFile audioFile,
        @RequestParam("coverImage") MultipartFile coverImage,
        @RequestParam("authors") List<Long> authorIds,
        RedirectAttributes redirectAttributes
    ) throws IOException {
        User user = getUser();
        if(user == null || !user.getRole().equals(Role.ROLE_ADMIN))
            return "redirect:/";


        Song song = songService.findById(id).orElse(null);

        if(song == null)
            return redirectWithError("Error", "Song not found", redirectAttributes, "redirect:/");

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
        List<Author> selectedAuthors = new ArrayList<>();
        iterableAuthors.forEach(selectedAuthors::add);
        song.setAuthors(selectedAuthors);

        song.setTitle(songForm.getTitle());
        song.setGenre(songForm.getGenre());
        song.setAlbumName(songForm.getAlbumName());
        song.setReleaseDate(songForm.getReleaseDate());
    
        songService.save(song);
        return redirectWithSuccess("Success", "Song edited successfully", redirectAttributes, "redirect:/admin/edit-song/" + id);
    }
    
    @GetMapping("/add-author")
    public String showAddAuthorForm(Model model) {
        User user = getUser();
        if(user == null || !user.getRole().equals(Role.ROLE_ADMIN))
            return "redirect:/";


        model.addAttribute("author", new Author());
        model.addAttribute("isEditing", false);
        model.addAttribute("formActionUrl", "/admin/add-author");
        return "admin/add-author";
    }

    @PostMapping("/add-author")
    public String addAuthor(
        @ModelAttribute Author author,
        @RequestHeader String referer,
        RedirectAttributes redirectAttributes
    ) {
        User user = getUser();
        if(user == null || !user.getRole().equals(Role.ROLE_ADMIN))
            return "redirect:/";

            
        authorService.save(author);

        return redirectWithSuccess("Success", "Author created successfully", redirectAttributes, "redirect:" + referer);
    }

    @GetMapping("/edit-author/{id}")
    public String showEditAuthorForm(
        @PathVariable Long id,
        RedirectAttributes redirectAttributes,
        Model model
    ) {
        User user = getUser();
        if(user == null || !user.getRole().equals(Role.ROLE_ADMIN))
            return "redirect:/";

        Author author = authorService.findById(id).orElse(null);
        if(author == null)
            return redirectWithError("Error", "Author not found", redirectAttributes, "redirect:/");

        model.addAttribute("author", author);
        model.addAttribute("isEditing", true);
        model.addAttribute("formActionUrl", "/admin/edit-author/" + id);
        return "admin/add-author";
    }

    @PostMapping("/edit-author/{id}")
    public String addEditAuthor(
        @PathVariable Long id,
        RedirectAttributes redirectAttributes,
        @ModelAttribute Author authorForm
    ) {
        User user = getUser();
        if(user == null || !user.getRole().equals(Role.ROLE_ADMIN))
            return "redirect:/";

        Author author = authorService.findById(id).orElse(null);
        if(author == null)
            return redirectWithError("Error", "Author not found", redirectAttributes, "redirect:/");

        
        author.setName(authorForm.getName());

        authorService.save(author);
        return redirectWithSuccess("Success", "Author updated successfully", redirectAttributes, "redirect:/admin/edit-song/" + id);
    }

    @GetMapping("/delete-song/{id}")
    public String deleteSong(
        @PathVariable Long id,
        RedirectAttributes redirectAttributes
    ) {
        
        User user = getUser();

        if(user == null || !user.getRole().equals(Role.ROLE_ADMIN))
            return "redirect:/";

        Song song = songService.findById(id).orElse(null);
        if(song == null)
            return redirectWithError("Error", "Song not found", redirectAttributes, "redirect:/");

        playlistService.removeSongFromAllPlaylists(song);
        songService.delete(song);

        return redirectWithSuccess("Success", "Song deleted successfully", redirectAttributes, "redirect:/");
    }

    @GetMapping("/delete-author/{id}")
    public String deleteAuthor(
        @PathVariable Long id,
        RedirectAttributes redirectAttributes
    ) {
        
        User user = getUser();

        if(user == null || !user.getRole().equals(Role.ROLE_ADMIN))
            return "redirect:/";

        Author author = authorService.findById(id).orElse(null);
        if(author == null)
            return redirectWithError("Error", "Author not found", redirectAttributes, "redirect:/");

        songService.removeAllSongFromAuthor(author);
        authorService.delete(author);

        return redirectWithSuccess("Success", "Author deleted successfully", redirectAttributes, "redirect:/");
    }
    

}
