package world.hiro.inventory.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import java.util.Date;

import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

@Entity // This tells Hibernate to make a table out of this class
@Table(name = "inventoryItems")
@Getter @Setter @NoArgsConstructor
public class InventoryItem {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @NotBlank
  @Column(name = "householdId")
  private Long householdId;

  @NotBlank
  @Column(name = "name")
  private String name;

  @NotBlank
  @Column(name = "category")
  private String category;

  @Column(name = "documentation", nullable = true)
  private String documentation;

  @NotBlank
  @Column(name = "storageLocation")
  private String storageLocation;

  @NotBlank
  @Column(name = "storageRoom")
  private String storageRoom;

  @NotBlank
  @Column(name = "quantity")
  private int quantity;

  @Column(name = "expiration", nullable = true)
  private Date expiration;

  public InventoryItem(
    Long householdId,
    String name,
    String category,
    String documentation,
    String storageLocation,
    String storageRoom,
    int quantity,
    Date expiration
  ) {
    this.householdId = householdId;
    this.name = name;
    this.category = category;
    this.documentation = documentation;
    this.storageLocation = storageLocation;
    this.storageRoom = storageRoom;
    this.quantity = quantity;
    this.expiration = expiration;
  }
}