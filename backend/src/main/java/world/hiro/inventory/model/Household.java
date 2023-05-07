package world.hiro.inventory.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import java.util.Date;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

@Entity // This tells Hibernate to make a table out of this class
@Table(name = "households")
@Getter @Setter @NoArgsConstructor
public class Household {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @NotBlank
  @Column(name = "inviteCode")
  private String inviteCode;

  @NotBlank
  @Column(name = "name")
  private String name;

  public Household(String inviteCode, String name) {
    this.inviteCode = inviteCode;
    this.name = name;
  }
}