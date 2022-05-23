package world.hiro.inventory.objects.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class JwtResponse {
  private String token;
  private String type = "Bearer";
  private Long id;
  private String email;

  public JwtResponse(String accessToken, Long id, String email) {
    this.token = accessToken;
    this.id = id;
    this.email = email;
  }
}
