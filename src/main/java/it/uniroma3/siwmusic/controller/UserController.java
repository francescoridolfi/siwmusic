package it.uniroma3.siwmusic.controller;


import java.security.Principal;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import it.uniroma3.siwmusic.model.Playlist;
import it.uniroma3.siwmusic.model.Song;
import it.uniroma3.siwmusic.model.User;
import it.uniroma3.siwmusic.service.PlaylistService;
import it.uniroma3.siwmusic.service.SongService;
import it.uniroma3.siwmusic.service.UserService;


@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	UserService userService;
	@Autowired
	PlaylistService playlistService;
	@Autowired
	SongService songService;

	@PostMapping("/playlist/toggle")
	public String toggleSongInPlaylist(@RequestParam("songId") Long songId, Principal principal,
			@RequestHeader("referer") String referer) {
	    Optional<User> userOpt = userService.findByUsername(principal.getName());
	    Optional<Song> songOpt = songService.findById(songId);

	    if (userOpt.isPresent() && songOpt.isPresent()) {
	        User user = userOpt.get();
	        Song song = songOpt.get();

	        Playlist playlist = playlistService.getOrCreateUserPlaylist(user);

	        if (playlist.getSongs().contains(song)) {
	            playlist.getSongs().remove(song);
	        } else {
	            playlist.getSongs().add(song);
	        }

	        playlistService.save(playlist);
	    }

	    return "redirect:" + referer;
	}

	@GetMapping("/playlist")
	@PreAuthorize("isAuthenticated()")
	public String viewPlaylist(Model model, Principal principal) {
	    Optional<User> userOpt = userService.findByUsername(principal.getName());
	    if (userOpt.isPresent()) {
	        User user = userOpt.get();
	        Playlist playlist = playlistService.getOrCreateUserPlaylist(user);
	        Set<Song> songs = playlist.getSongs();
	        model.addAttribute("songs", songs);
	    } else {
	        model.addAttribute("songs", Collections.emptyList());
	    }
	    return "user/my-playlist";
	}

}

