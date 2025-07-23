package it.uniroma3.siwmusic.controller;


import java.security.Principal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.uniroma3.siwmusic.model.Playlist;
import it.uniroma3.siwmusic.model.Song;
import it.uniroma3.siwmusic.model.User;
import it.uniroma3.siwmusic.service.PlaylistService;
import it.uniroma3.siwmusic.service.SongService;
import it.uniroma3.siwmusic.service.UserService;
import it.uniroma3.siwmusic.utils.BaseController;


@Controller
@RequestMapping("/user")
public class UserController extends BaseController {
	
	@Autowired
	UserService userService;
	@Autowired
	PlaylistService playlistService;
	@Autowired
	SongService songService;

	@PostMapping("/playlist/toggle")
	public String toggleSongInPlaylist(
        @RequestParam("songId") Long songId,
        Principal principal,
		@RequestHeader("referer") String referer,
        RedirectAttributes redirectAttributes
    ) {
        referer = "redirect:" + referer;

        User user = getUser();
        if(user == null) {
            return redirectWithError(
                "Error",
                "You must login in order to manage your playlist",
                redirectAttributes,
                referer
            );
        }
        
        
	    Song song = songService.findById(songId).orElse(null);

        if(song == null) {
            return redirectWithError(
                "Error",
                "Song not found",
                redirectAttributes,
                referer
            );
        }

        Playlist playlist = playlistService.getOrCreateUserPlaylist(user);

        if (playlist.getSongs().contains(song)) {
            playlist.getSongs().remove(song);
        } else {
            playlist.getSongs().add(song);
        }

        playlistService.save(playlist);

	    return referer;
	}

	@GetMapping("/playlist")
	public String viewPlaylist(Model model) {

        User user = getUser();

        if(user == null)
            return "redirect:/";

        Playlist playlist = playlistService.getOrCreateUserPlaylist(user);
        ArrayList<Song> songs = new ArrayList<>(playlist.getSongs());
        model.addAttribute("songs", songs);
	    return "user/my-playlist";
	}

}

