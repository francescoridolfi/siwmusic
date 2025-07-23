package it.uniroma3.siwmusic.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
import it.uniroma3.siwmusic.utils.BaseController;

@Controller
public class HomeController extends BaseController {

    @Autowired
    private SongService songService;

    @Autowired
    private PlaylistService playlistService;

    @GetMapping("/")
    public String home(Model model) {
        List<Song> songs = songService.findAll();

        for (Song s : songs) {
            s.getAuthors().size();
        }
        model.addAttribute("songs", songs);

        Map<Long, Boolean> inPlaylistMap = new HashMap<>();
        User user = getUser();

        if (user != null) {
            Playlist playlist = playlistService.getOrCreateUserPlaylist(user);
            ArrayList<Song> playlistSongs = new ArrayList<>(playlist.getSongs());
            for (Song s : songs) {
                inPlaylistMap.put(s.getId(), playlistSongs.contains(s));
                
            }
        }

        model.addAttribute("inPlaylistMap", inPlaylistMap);
        return "home";
    }

}
