package world.hiro.inventory.controller;

import java.util.Date;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import world.hiro.inventory.model.User;
import world.hiro.inventory.repository.UserRepository;
import world.hiro.inventory.utilities.ResponseMessages;
import world.hiro.inventory.utilities.UserIdentityUtil;

@RestController
@RequestMapping("/user")
public class UserController {
  @Autowired UserRepository userRepository;

  @Autowired UserIdentityUtil userIdentityUtil;

  @Autowired PasswordEncoder encoder;

  // Read by Id
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> getUserById() {
    Optional<User> optionUser = userRepository.findById(userIdentityUtil.GetLoggedUserId());
    try {
      User user = optionUser.get();
      return ResponseEntity.ok(user);
    } catch (NoSuchElementException e) {
      return new ResponseEntity<>(ResponseMessages.failedToFindUserById, HttpStatus.NOT_FOUND);
    }
  }

  // Update by Id
  @PutMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE,
      value = "/id")
  public ResponseEntity<Object> updateUserById(@RequestBody String request) {
    JsonParser parser = JsonParserFactory.getJsonParser();
    Map<String, Object> req = parser.parseMap(request);
    Optional<User> optionUser = userRepository.findById(userIdentityUtil.GetLoggedUserId());
    try {
      User user = optionUser.get();
      if (req.containsKey("email")) {
        user.setEmail((String) req.get("email"));
      }
      if (req.containsKey("password")) {
        user.setPassword(encoder.encode((String) req.get("password")));
      }
      user.setLastUpdated(new Date());
      userRepository.save(user);
      return ResponseEntity.ok(user);
    } catch (NoSuchElementException e) {
      return new ResponseEntity<>(ResponseMessages.failedToUpdateUserById, HttpStatus.NOT_FOUND);
    }
  }

  // Delete by Id
  @DeleteMapping(
      produces = MediaType.APPLICATION_JSON_VALUE,
      value = "/id")
  public ResponseEntity<Object> deleteUserById() {
    try {
      userRepository.deleteById(userIdentityUtil.GetLoggedUserId());
      return ResponseEntity.ok(ResponseMessages.succesfullyDeletedUserById);
    } catch (Exception e) {
      return new ResponseEntity<>(ResponseMessages.failedToDeleteUserById, HttpStatus.NOT_FOUND);
    }
  }
}
