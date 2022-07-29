package world.hiro.inventory.controller;

import world.hiro.inventory.model.User;
import world.hiro.inventory.objects.requests.LoginRequest;
import world.hiro.inventory.objects.requests.SignupRequest;
import world.hiro.inventory.objects.responses.JwtResponse;
import world.hiro.inventory.objects.responses.MessageResponse;
import world.hiro.inventory.repository.UserRepository;
import world.hiro.inventory.security.jwt.JwtUtils;
import world.hiro.inventory.security.services.UserDetailsImpl;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
  @Autowired AuthenticationManager authenticationManager;

  @Autowired UserRepository userRepository;

  @Autowired PasswordEncoder encoder;

  @Autowired JwtUtils jwtUtils;

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
          .body(new MessageResponse("Error: Email is already in use!"));
    }

    // Create new user's account
    User user = new User(signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()), new Date(), new Date());

    userRepository.save(user);

    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }
}
