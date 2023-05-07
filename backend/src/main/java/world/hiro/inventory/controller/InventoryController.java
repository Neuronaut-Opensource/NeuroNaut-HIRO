package world.hiro.inventory.controller;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Date;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;

import world.hiro.inventory.model.InventoryItem;
import world.hiro.inventory.model.User;
import world.hiro.inventory.repository.InventoryRepository;
import world.hiro.inventory.repository.UserRepository;
import world.hiro.inventory.utilities.ResponseMessages;
import world.hiro.inventory.security.services.UserDetailsImpl;

@RestController
@RequestMapping("/inventory")
public class InventoryController {
  
  @Autowired InventoryRepository inventoryRepository;
  @Autowired UserRepository userRepository;

  // Create
  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> createInventoryRecord(@RequestBody String request) {
    // parse request
    JsonParser parser = JsonParserFactory.getJsonParser();
    Map<String, Object> req = parser.parseMap(request);
    // get logged in user
    UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Long userId = user.getId();
    Optional<User> optionUser = userRepository.findById(userId);
    try {
      InventoryItem item;
      String expirationString = (String) req.get("expiration");
      item =
          new InventoryItem(
              optionUser.get().getHouseholdId(),
              (String) req.get("name"),
              (String) req.get("category"),
              (String) req.get("documentation"),
              (String) req.get("storageLocation"),
              (String) req.get("storageRoom"),
              (int) req.get("quantity"),
              new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(expirationString));
      return ResponseEntity.ok(inventoryRepository.save(item));
    } catch (Exception e) {
      return new ResponseEntity<>(
          ResponseMessages.failedToCreateInventoryItemBadRequest, HttpStatus.BAD_REQUEST);
    }
  }

  // Read all
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> getAllInventoryItems(
    @RequestParam( value = "pageNumber", defaultValue = "0") int pageNumber,
    @RequestParam( value = "pageSize", defaultValue = "10") int pageSize
  ) {
    // get logged in user
    UserDetailsImpl userImpl = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Long userId = userImpl.getId();
    Optional<User> optionUser = userRepository.findById(userId);
    try {
      // get all inventory for the given household
      User user = optionUser.get();
      Pageable paging = PageRequest.of(pageNumber, pageSize);
      return ResponseEntity.ok(inventoryRepository.findAllByHouseholdId(user.getHouseholdId(), paging));
    } catch (NoSuchElementException e) {
      return new ResponseEntity<>(ResponseMessages.failedToFindAllInventoryItems, HttpStatus.NOT_FOUND);
    }
  }

  // Read by Id
  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE,
      value = "/id")
  public ResponseEntity<Object> getInventoryItemById(@RequestBody String request) {
    // parse the request
    JsonParser parser = JsonParserFactory.getJsonParser();
    Map<String, Object> req = parser.parseMap(request);
    // get logged in user
    UserDetailsImpl userImpl = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Long userId = userImpl.getId();
    Optional<User> optionUser = userRepository.findById(userId);
    try {
      // get the inventory item by id if it belongs to the given household
      User user = optionUser.get();
      InventoryItem inventoryItem = inventoryRepository.findByIdAndHouseholdId(Long.valueOf((int) req.get("id")), user.getHouseholdId());
      return ResponseEntity.ok(inventoryItem);
    } catch (NoSuchElementException e) {
      return new ResponseEntity<>(ResponseMessages.failedToFindInventoryItemById, HttpStatus.NOT_FOUND);
    }
  }

  // Read by name
  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE,
      value = "/name")
  public ResponseEntity<Object> getInventoryItemByName(@RequestBody String request) {
    // parse the request
    JsonParser parser = JsonParserFactory.getJsonParser();
    Map<String, Object> req = parser.parseMap(request);
    // get logged in user
    UserDetailsImpl userImpl = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Long userId = userImpl.getId();
    Optional<User> optionUser = userRepository.findById(userId);
    try {
      // build pageable
      Pageable paging = PageRequest.of((int) req.get("pageNumber"), (int) req.get("pageSize"));
      // get by name containing and householdId
      User user = optionUser.get();
      Page<InventoryItem> pageInventoryItem = inventoryRepository.findByNameContainingAndHouseholdId((String) req.get("name"), user.getHouseholdId(), paging);
      if (pageInventoryItem.getTotalElements() > 0) {
        return ResponseEntity.ok(pageInventoryItem);
      } else {
        return new ResponseEntity<>(ResponseMessages.failedToFindInventoryItemByName, HttpStatus.NOT_FOUND);
      }
    } catch (NoSuchElementException e) {
      return new ResponseEntity<>(ResponseMessages.failedToFindInventoryItemByName, HttpStatus.NOT_FOUND);
    }
  }

  // Read by category
  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE,
      value = "/category")
  public ResponseEntity<Object> getInventoryItemByCategory(@RequestBody String request) {
    // parse the request
    JsonParser parser = JsonParserFactory.getJsonParser();
    Map<String, Object> req = parser.parseMap(request);
    // get logged in user
    UserDetailsImpl userImpl = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Long userId = userImpl.getId();
    Optional<User> optionUser = userRepository.findById(userId);
    try {
      // build pageable
      Pageable paging = PageRequest.of((int) req.get("pageNumber"), (int) req.get("pageSize"));
      // get by category containing and householdId
      User user = optionUser.get();
      Page<InventoryItem> pageInventoryItem = inventoryRepository.findByCategoryContainingAndHouseholdId((String) req.get("category"), user.getHouseholdId(), paging);
      if (pageInventoryItem.getTotalElements() > 0) {
        return ResponseEntity.ok(pageInventoryItem);
      } else {
        return new ResponseEntity<>(ResponseMessages.failedToFindInventoryItemByCategory, HttpStatus.NOT_FOUND);
      }
    } catch (NoSuchElementException e) {
      return new ResponseEntity<>(ResponseMessages.failedToFindInventoryItemByName, HttpStatus.NOT_FOUND);
    }
  }

  // Read by location
  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE,
      value = "/location")
  public ResponseEntity<Object> getInventoryItemByLocation(@RequestBody String request) {
    // parse the request
    JsonParser parser = JsonParserFactory.getJsonParser();
    Map<String, Object> req = parser.parseMap(request);
    // get logged in user
    UserDetailsImpl userImpl = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Long userId = userImpl.getId();
    Optional<User> optionUser = userRepository.findById(userId);
    try {
      // build pageable
      Pageable paging = PageRequest.of((int) req.get("pageNumber"), (int) req.get("pageSize"));
      // get by storage location containing and householdId
      User user = optionUser.get();
      Page<InventoryItem> pageInventoryItem = inventoryRepository.findByStorageLocationContainingAndHouseholdId((String) req.get("location"), user.getHouseholdId(), paging);
      if (pageInventoryItem.getTotalElements() > 0) {
        return ResponseEntity.ok(pageInventoryItem);
      } else {
        return new ResponseEntity<>(ResponseMessages.failedToFindInventoryItemByLocation, HttpStatus.NOT_FOUND);
      }
    } catch (NoSuchElementException e) {
      return new ResponseEntity<>(ResponseMessages.failedToFindInventoryItemByName, HttpStatus.NOT_FOUND);
    }
  }  

  // Read by room
  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE,
      value = "/room")
  public ResponseEntity<Object> getInventoryItemByRoom(@RequestBody String request) {
    // parse the request
    JsonParser parser = JsonParserFactory.getJsonParser();
    Map<String, Object> req = parser.parseMap(request);
    // get logged in user
    UserDetailsImpl userImpl = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Long userId = userImpl.getId();
    Optional<User> optionUser = userRepository.findById(userId);
    try {
      // build pageable
      Pageable paging = PageRequest.of((int) req.get("pageNumber"), (int) req.get("pageSize"));
      // get by storage room containing and householdId
      User user = optionUser.get();
      Page<InventoryItem> pageInventoryItem = inventoryRepository.findByStorageRoomContainingAndHouseholdId((String) req.get("room"), user.getHouseholdId(), paging);
      if (pageInventoryItem.getTotalElements() > 0) {
        return ResponseEntity.ok(pageInventoryItem);
      } else {
        return new ResponseEntity<>(ResponseMessages.failedToFindInventoryItemByRoom, HttpStatus.NOT_FOUND);
      }
    } catch (NoSuchElementException e) {
      return new ResponseEntity<>(ResponseMessages.failedToFindInventoryItemByName, HttpStatus.NOT_FOUND);
    }
  }

  // Read by expiration between dates
  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE,
      value = "/expired")
  public ResponseEntity<Object> getInventoryItemByExpiredBetween(@RequestBody String request) {
    // parse the request
    JsonParser parser = JsonParserFactory.getJsonParser();
    Map<String, Object> req = parser.parseMap(request);
    // get logged in user
    UserDetailsImpl userImpl = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Long userId = userImpl.getId();
    Optional<User> optionUser = userRepository.findById(userId);
    // build pageable
    Pageable paging = PageRequest.of((int) req.get("pageNumber"), (int) req.get("pageSize"));
    // get start & end dates
    String startDate = (String) req.get("startDate");
    String endDate = (String) req.get("endDate");
    try {
      // get expired items between date range and householdId
      User user = optionUser.get();
      Page<InventoryItem> pageInventoryItem = inventoryRepository.findByExpirationBetweenAndHouseholdId(
        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startDate),
        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endDate),
        user.getHouseholdId(),
        paging
      );
      if (pageInventoryItem.getTotalElements() > 0) {
        return ResponseEntity.ok(pageInventoryItem);
      } else {
        throw new Exception(ResponseMessages.failedToFindInventoryItemByExpirationRange);
      }
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  // Read all expired inventory
  @GetMapping(
    produces = MediaType.APPLICATION_JSON_VALUE,
    value = "/expired"  
  )
  public ResponseEntity<Object> getAllExpiredInventoryItems(
    @RequestParam( value = "pageNumber", defaultValue = "0") int pageNumber,
    @RequestParam( value = "pageSize", defaultValue = "10") int pageSize
  ) {
    // get logged in user
    UserDetailsImpl userImpl = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Long userId = userImpl.getId();
    Optional<User> optionUser = userRepository.findById(userId);
    // build pageable
    Pageable paging = PageRequest.of(pageNumber, pageSize);
    try {
      // get expired items within a householdId
      User user = optionUser.get();
      Page<InventoryItem> pageInventoryItem = inventoryRepository.findByExpirationBeforeAndHouseholdId(new Date(), user.getHouseholdId(), paging);
      return ResponseEntity.ok(pageInventoryItem);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  // Update by Id
  @PutMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE,
      value = "/id")
  public ResponseEntity<Object> updateInventoryItemById(@RequestBody String request) {
    // parse teh request
    JsonParser parser = JsonParserFactory.getJsonParser();
    Map<String, Object> req = parser.parseMap(request);
    // get logged in user
    UserDetailsImpl userImpl = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Long userId = userImpl.getId();
    Optional<User> optionUser = userRepository.findById(userId);
    try {
      // get the item to update
      User user = optionUser.get();
      InventoryItem inventoryItem = inventoryRepository.findByIdAndHouseholdId(Long.valueOf((int) req.get("id")), user.getHouseholdId());
      if (req.containsKey("name")) {
        inventoryItem.setName((String) req.get("name"));
      }
      if (req.containsKey("category")) {
        inventoryItem.setCategory((String) req.get("category"));
      }
      if (req.containsKey("documentation")) {
        inventoryItem.setDocumentation((String) req.get("documentation"));
      }
      if (req.containsKey("storageLocation")) {
        inventoryItem.setStorageLocation((String) req.get("storageLocation"));
      }
      if (req.containsKey("storageRoom")) {
        inventoryItem.setStorageRoom((String) req.get("storageRoom"));
      }
      if (req.containsKey("quantity")) {
        inventoryItem.setQuantity((int) req.get("quantity"));
      }
      inventoryRepository.save(inventoryItem);
      return ResponseEntity.ok(inventoryItem);
    } catch (NoSuchElementException e) {
      return new ResponseEntity<>(ResponseMessages.failedToFindInventoryItemById, HttpStatus.NOT_FOUND);
    }
  }

  // Delete by Id
  @DeleteMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE,
      value = "/id")
  public ResponseEntity<Object> deleteInventoryItemById(@RequestBody String request) {
    JsonParser parser = JsonParserFactory.getJsonParser();
    Map<String, Object> req = parser.parseMap(request);
    try {
      inventoryRepository.deleteById(Long.valueOf((int) req.get("id")));
      return ResponseEntity.ok(ResponseMessages.succesfullyDeletedInventoryItemById);
    } catch (Exception e) {
      return new ResponseEntity<>(ResponseMessages.failedToFindInventoryItemById, HttpStatus.NOT_FOUND);
    }
  }
}
