package world.hiro.inventory.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

@Entity // This tells Hibernate to make a table out of this class
@Table(name = "inventoryItems",
  uniqueConstraints = {@UniqueConstraint(columnNames = "name")})
@Getter @Setter @NoArgsConstructor
public class InventoryItem {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "name")
  private String name;

  @Column(name = "category")
  private String category;

  @Column(name = "documentation")
  private String documentation;

  @Column(name = "storageLocation")
  private String storageLocation;

  @Column(name = "storageRoom")
  private String storageRoom;

  @Column(name = "quantity")
  private int quantity;

  public InventoryItem(String name, String category, String documentation, String storageLocation, String storageRoom, int quantity) {
    this.name = name;
    this.category = category;
    this.documentation = documentation;
    this.storageLocation = storageLocation;
    this.storageRoom = storageRoom;
    this.quantity = quantity;
  }
}