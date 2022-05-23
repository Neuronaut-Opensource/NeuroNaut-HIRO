package world.hiro.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import world.hiro.inventory.model.InventoryItem;

public interface InventoryRepository extends JpaRepository<InventoryItem, Long> {
  InventoryItem findById(Integer id);
}
