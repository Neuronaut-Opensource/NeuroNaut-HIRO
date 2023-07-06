package world.hiro.inventory.controller;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import world.hiro.inventory.model.Household;
import world.hiro.inventory.repository.HouseholdRepository;
import world.hiro.inventory.utilities.ResponseMessages;
import world.hiro.inventory.utilities.UserIdentityUtil;

@RestController
@RequestMapping("/household")
public class HouseholdController {
    @Autowired HouseholdRepository householdRepository;

    @Autowired UserIdentityUtil userIdentityUtil;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getMyHouseholdInfo() {
        Optional<Household> optionalHousehold = householdRepository.findById(userIdentityUtil.GetHouseholdId());
        try {
            Household household = optionalHousehold.get();
            return ResponseEntity.ok(household);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(ResponseMessages.failedToGetHouseholdInfo, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> updateHouseholdName(@RequestBody String request) {
        JsonParser parser = JsonParserFactory.getJsonParser();
        Map<String, Object> req = parser.parseMap(request);
        Optional<Household> optionalHousehold = householdRepository.findById(userIdentityUtil.GetHouseholdId());
        try {
            Household household = optionalHousehold.get();
            if (req.containsKey("name")) {
                household.setName((String) req.get("name"));
            }
            householdRepository.save(household);
            return ResponseEntity.ok(household);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(ResponseMessages.failedToGetHouseholdInfo, HttpStatus.NOT_FOUND);
        }
    }
}
