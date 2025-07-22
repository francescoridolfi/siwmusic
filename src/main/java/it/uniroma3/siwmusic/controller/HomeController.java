package it.uniroma3.siwmusic.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import it.uniroma3.siwmusic.model.Playlist;
import it.uniroma3.siwmusic.model.Song;
import it.uniroma3.siwmusic.model.User;
import it.uniroma3.siwmusic.service.PlaylistService;
import it.uniroma3.siwmusic.service.SongService;
import it.uniroma3.siwmusic.service.UserService;

@Controller
public class HomeController {

    @Autowired
    private SongService songService;
    @Autowired
    private UserService userService;
    @Autowired
    private PlaylistService playlistService;

    @GetMapping("/")
    public String home(Model model, Principal principal) {
    	List<Song> songs = new ArrayList<>();
    	songService.findAll().forEach(songs::add);
        model.addAttribute("songs", songs);

        if (principal != null) {
        	Optional<User> userOpt = userService.findByUsername(principal.getName());
        	if (userOpt.isPresent()) {
        	    User user = userOpt.get();
        	    Playlist playlist = playlistService.getOrCreateUserPlaylist(user);
        	    Set<Long> songIdsInPlaylist = playlist.getSongs().stream().map(Song::getId).collect(Collectors.toSet());

        	    Map<Long, Boolean> inPlaylistMap = new HashMap<>();
        	    for (Song s : songs) {
        	        inPlaylistMap.put(s.getId(), songIdsInPlaylist.contains(s.getId()));
        	    }

        	    model.addAttribute("inPlaylistMap", inPlaylistMap);
        	}

        }

        return "home";
    }

}
