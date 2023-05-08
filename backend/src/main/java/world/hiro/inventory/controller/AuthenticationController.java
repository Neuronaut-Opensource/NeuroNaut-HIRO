package world.hiro.inventory.controller;

import java.util.Date;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import world.hiro.inventory.model.User;
import world.hiro.inventory.model.Household;
import world.hiro.inventory.objects.requests.LoginRequest;
import world.hiro.inventory.objects.requests.SignupRequest;
import world.hiro.inventory.objects.responses.JwtResponse;
import world.hiro.inventory.utilities.ResponseMessages;
import world.hiro.inventory.utilities.InviteCodeGenerator;
import world.hiro.inventory.repository.UserRepository;
import world.hiro.inventory.repository.HouseholdRepository;
import world.hiro.inventory.security.jwt.JwtUtils;
import world.hiro.inventory.security.services.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
  @Autowired AuthenticationManager authenticationManager;

  @Autowired UserRepository userRepository;
  @Autowired HouseholdRepository householdRepository;

  @Autowired PasswordEncoder encoder;

  @Autowired JwtUtils jwtUtils;
  @Autowired InviteCodeGenerator inviteCodeGenerator;

  @PostMapping("/login")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtils.generateJwtToken(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    return ResponseEntity.ok(
        new JwtResponse(jwt, userDetails.getId(), userDetails.getEmail()));
  }

  @PostMapping("/enroll")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity.badRequest()
          .body(ResponseMessages.emailAlreadyInUse);
    }

    // Create a new Household for the new user
    String inviteCode = inviteCodeGenerator.createInviteCode();
    Household household = new Household(inviteCode, signUpRequest.getHouseholdName());

    household = householdRepository.save(household);

    // Create new user's account
    User user = new User(household.getId(), signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()), new Date(), new Date());

    userRepository.save(user);

    return ResponseEntity.ok(ResponseMessages.enrolled(inviteCode));
  }

  @PostMapping("/enrollInvite")
  public ResponseEntity<?> registerUserByInvite(@Valid @RequestBody SignupRequest signUpRequest) {

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity.badRequest()
          .body(ResponseMessages.emailAlreadyInUse);
    }

    // find household that invited user
    List<Household> listHousehold = householdRepository.findByInviteCode(signUpRequest.getInviteCode());

    // if household can be found by invite code enroll, else throw bad request error
    if (listHousehold.size() > 0) {
      // Create new user's account
      User user = new User(listHousehold.get(0).getId(), signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()), new Date(), new Date());

      userRepository.save(user);

      return ResponseEntity.ok(ResponseMessages.enrolled(signUpRequest.getInviteCode()));
    } else {
      return ResponseEntity.badRequest().body(ResponseMessages.invalidInviteCode);
    }
  }
}
