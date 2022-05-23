package world.hiro.inventory.controller;

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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import world.hiro.inventory.model.InventoryItem;
import world.hiro.inventory.repository.InventoryRepository;
import world.hiro.inventory.utilities.ResponseMessages;

@RestController
@RequestMapping("/inventory")
public class InventoryController {
  @Autowired InventoryRepository inventoryRepo;

  // Create
  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> createInventoryRecord(@RequestBody String request) {
    JsonParser parser = JsonParserFactory.getJsonParser();
    Map<String, Object> req = parser.parseMap(request);
    try {
      InventoryItem item =
          new InventoryItem(
              (String) req.get("name"),
              (String) req.get("category"),
              (String) req.get("documentation"),
              (String) req.get("storageLocation"),
              (String) req.get("storageRoom"),
              (int) req.get("quantity"));
      return ResponseEntity.ok(inventoryRepo.save(item));
    } catch (Exception e) {
      return new ResponseEntity<>(
          ResponseMessages.failedToCreateInventoryItemBadRequest, HttpStatus.BAD_REQUEST);
    }
  }

  // Read all
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public List<InventoryItem> getAllInventoryItems() {
    return inventoryRepo.findAll();
  }

  // Read by Id
  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE,
      value = "/id")
  public ResponseEntity<Object> getInventoryItemById(@RequestBody String request) {
    JsonParser parser = JsonParserFactory.getJsonParser();
    Map<String, Object> req = parser.parseMap(request);
    Optional<InventoryItem> optionInventoryItem = inventoryRepo.findById(Long.valueOf((int) req.get("id")));
    try {
      InventoryItem inventoryItem = optionInventoryItem.get();
      return ResponseEntity.ok(inventoryItem);
    } catch (NoSuchElementException e) {
      return new ResponseEntity<>(ResponseMessages.failedToFindInventoryItemById, HttpStatus.NOT_FOUND);
    }
  }

  // Update by Id
  @PutMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE,
      value = "/id")
  public ResponseEntity<Object> updateInventoryItemById(@RequestBody String request) {
    JsonParser parser = JsonParserFactory.getJsonParser();
    Map<String, Object> req = parser.parseMap(request);
    Optional<InventoryItem> optionInventoryItem = inventoryRepo.findById(Long.valueOf((int) req.get("id")));
    try {
      InventoryItem inventoryItem = optionInventoryItem.get();
      if (req.containsKey("name")) {
        inventoryItem.setName((String) req.get("name"));
      }
      if (req.containsKey("unit")) {
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
      inventoryRepo.save(inventoryItem);
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
      inventoryRepo.deleteById(Long.valueOf((int) req.get("id")));
      return ResponseEntity.ok(ResponseMessages.succesfullyDeletedInventoryItemById);
    } catch (Exception e) {
      return new ResponseEntity<>(ResponseMessages.failedToFindInventoryItemById, HttpStatus.NOT_FOUND);
    }
  }
}
