package world.hiro.inventory.utilities;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.security.core.context.SecurityContextHolder;

import world.hiro.inventory.model.User;
import world.hiro.inventory.repository.UserRepository;
import world.hiro.inventory.security.services.UserDetailsImpl;

@Component
public class UserIdentityUtil {

    @Autowired UserRepository userRepository;

    public Long GetHouseholdId() throws NoSuchElementException {
        // get logged in user
        UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = user.getId();
        Optional<User> optionUser = userRepository.findById(userId);
        return optionUser.get().getHouseholdId();
    }

    public Long GetLoggedUserId() throws NoSuchElementException {
        // get logged in user
        UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getId();
    }
}