package it.uniroma3.siwmusic.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class Messages {
    public static final String SUCCESS = "success";
    public static final String ERROR = "error";
    public static final String WARNING = "warning";
    public static final String INFO = "info";

    private String level;
    private String title;
    private String message;

    public Messages(String level, String title, String message) {
        this.level = level;
        this.title = title;
        this.message = message;
    }


    public static Messages success(String title, String message) {
        return new Messages(SUCCESS, title, message);
    }

    public static Messages error(String title, String message) {
        return new Messages(ERROR, title, message);
    }

    public static Messages warning(String title, String message) {
        return new Messages(WARNING, title, message);
    }

    public static Messages info(String title, String message) {
        return new Messages(INFO, title, message);
    }

}