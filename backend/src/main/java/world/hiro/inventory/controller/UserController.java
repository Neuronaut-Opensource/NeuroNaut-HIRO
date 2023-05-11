package world.hiro.inventory.controller;

import java.util.Date;
import java.util.List;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import world.hiro.inventory.model.User;
import world.hiro.inventory.repository.HouseholdRepository;
import world.hiro.inventory.repository.InventoryRepository;
import world.hiro.inventory.repository.UserRepository;
import world.hiro.inventory.utilities.ResponseMessages;
import world.hiro.inventory.utilities.UserIdentityUtil;

@RestController
@RequestMapping("/user")
public class UserController {
  @Autowired UserRepository userRepository;
  @Autowired HouseholdRepository householdRepository;
  @Autowired InventoryRepository inventoryRepository;

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
  @Transactional
  public ResponseEntity<Object> deleteUserById() {
    try {
      // get user info
      Optional<User> optionUser = userRepository.findById(userIdentityUtil.GetLoggedUserId());
      User user = optionUser.get();
      // get all users in the household
      List<User> usersInHousehold = userRepository.findAllByHouseholdId(user.getHouseholdId());
      // See if this is the last user in the household
      if (usersInHousehold.size() == 1) {
        // delete all inventory items that belong to this household from the DB
        inventoryRepository.deleteByHouseholdId(user.getHouseholdId());
        // delete the household from the DB
        householdRepository.deleteById(user.getHouseholdId());
      }
      // delete the user from the DB
      userRepository.deleteById(user.getUserId());
      return ResponseEntity.ok(ResponseMessages.succesfullyDeletedUserById);
    } catch (Exception e) {
      return new ResponseEntity<>(ResponseMessages.failedToDeleteUserById, HttpStatus.BAD_REQUEST);
    }
  }
}
