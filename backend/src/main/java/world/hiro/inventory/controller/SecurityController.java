package world.hiro.inventory.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.context.SecurityContextHolder;

import world.hiro.inventory.security.services.UserDetailsImpl;

@RestController
public class SecurityController {
    @RequestMapping(value = "/username", method = RequestMethod.GET)
    @ResponseBody
    public Long currentUserName() {
        UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getId();
    }
}