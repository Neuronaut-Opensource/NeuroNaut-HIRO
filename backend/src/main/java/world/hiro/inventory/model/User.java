package world.hiro.inventory.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity // This tells Hibernate to make a table out of this class
@Table(
    name = "users",
    uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
@Getter @Setter @NoArgsConstructor
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long userId;

  @NotBlank
  @Column(name = "householdId")
  private Long householdId;

  @NotBlank
  @Size(max = 50)
  @Email
  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @NotBlank
  @Size(max = 120)
  @Column(name = "password")
  private String password;

  @NotBlank
  @Column(name = "creationTime")
  private Date creationTime;

  @NotBlank
  @Column(name = "lastUpdated")
  private Date lastUpdated;

  public User(
    Long householdId,
    String email,
    String password,
    Date creationTime,
    Date lastUpdated
  ) {
    this.householdId = householdId;
    this.email = email;
    this.password = password;
    this.creationTime = creationTime;
    this.lastUpdated = lastUpdated;
  }
}
