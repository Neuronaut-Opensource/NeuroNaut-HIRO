package world.hiro.inventory.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Date;

import world.hiro.inventory.model.InventoryItem;

public interface InventoryRepository extends PagingAndSortingRepository<InventoryItem, Long> {
  Page<InventoryItem> findAllByHouseholdId(Long householdId, Pageable pageable);
  InventoryItem findByIdAndHouseholdId(Long id, Long householdId);
  Page<InventoryItem> findByNameContainingAndHouseholdId(String name, Long householdId, Pageable pageable);
  Page<InventoryItem> findByCategoryContainingAndHouseholdId(String category, Long householdId, Pageable pageable);
  Page<InventoryItem> findByStorageLocationContainingAndHouseholdId(String storageLocation, Long householdId, Pageable pageable);
  Page<InventoryItem> findByStorageRoomContainingAndHouseholdId(String storageRoom, Long householdId, Pageable pageable);
  Page<InventoryItem> findByExpirationBetweenAndHouseholdId(Date testStartDate, Date testEndDate, Long householdId, Pageable pageable);
  Page<InventoryItem> findByExpirationBeforeAndHouseholdId(Date testDate, Long householdId, Pageable pageable);
}
