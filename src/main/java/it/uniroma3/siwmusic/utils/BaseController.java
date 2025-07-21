package it.uniroma3.siwmusic.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.uniroma3.siwmusic.model.Messages;
import it.uniroma3.siwmusic.model.User;
import it.uniroma3.siwmusic.security.SiwUserDetails;

public class BaseController {
    
    public User getUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof SiwUserDetails)
            return ((SiwUserDetails) principal).getUser();
        return null;
    }

    public String redirectWithError(
        String title,
        String message,
        RedirectAttributes redirectAttributes, 
        String redirectUrl
    ) {         
        redirectAttributes.addFlashAttribute(
            "message",
            Messages.error(title, message)
        );

        return redirectUrl;
    }

    public String redirectWithSuccess(
        String title,
        String message,
        RedirectAttributes redirectAttributes,
        String redirectUrl
    ) {
        redirectAttributes.addFlashAttribute(
            "message",
            Messages.success(title, message)
        );
        return redirectUrl;
    }

    public String redirectWithWarning(
        String title,
        String message,
        RedirectAttributes redirectAttributes,
        String redirectUrl
    ) {
        redirectAttributes.addFlashAttribute(
            "message",
            Messages.warning(title, message)
        );
        return redirectUrl;
    }
    

    public void userLogout() {
        SecurityContextHolder.clearContext();
    }

}
