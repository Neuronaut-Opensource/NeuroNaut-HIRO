package world.hiro.inventory.controller;

import world.hiro.inventory.utilities.ResponseMessages;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {
  @GetMapping("/")
  public ResponseEntity<String> ping() {
    return ResponseEntity.ok(ResponseMessages.systemIsWorkingMessage);
  }
}
