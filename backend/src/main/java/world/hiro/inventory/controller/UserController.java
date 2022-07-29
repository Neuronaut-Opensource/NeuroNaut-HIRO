package world.hiro.inventory.controller;

import world.hiro.inventory.model.User;
import world.hiro.inventory.repository.UserRepository;
import world.hiro.inventory.utilities.ResponseMessages;

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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
  @Autowired UserRepository userRepo;

  // Read by Id
  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE,
      value = "/id")
  public ResponseEntity<Object> getUserById(@RequestBody String request) {
    JsonParser parser = JsonParserFactory.getJsonParser();
    Map<String, Object> req = parser.parseMap(request);
    Optional<User> optionUser = userRepo.findById(Long.valueOf((int) req.get("id")));
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
    Optional<User> optionUser = userRepo.findById(Long.valueOf((int) req.get("id")));
    try {
      User user = optionUser.get();
      if (req.containsKey("email")) {
        user.setEmail((String) req.get("email"));
      }
      if (req.containsKey("password")) {
        user.setPassword((String) req.get("password"));
      }
      user.setLastUpdated(new Date());
      userRepo.save(user);
      return ResponseEntity.ok(user);
    } catch (NoSuchElementException e) {
      return new ResponseEntity<>(ResponseMessages.failedToFindUserById, HttpStatus.NOT_FOUND);
    }
  }

  // Delete by Id
  @DeleteMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE,
      value = "/id")
  public ResponseEntity<Object> deleteUserById(@RequestBody String request) {
    JsonParser parser = JsonParserFactory.getJsonParser();
    Map<String, Object> req = parser.parseMap(request);
    try {
      userRepo.deleteById(Long.valueOf((int) req.get("id")));
      return ResponseEntity.ok(ResponseMessages.succesfullyDeletedUserById);
    } catch (Exception e) {
      return new ResponseEntity<>(ResponseMessages.failedToFindUserById, HttpStatus.NOT_FOUND);
    }
  }
}
