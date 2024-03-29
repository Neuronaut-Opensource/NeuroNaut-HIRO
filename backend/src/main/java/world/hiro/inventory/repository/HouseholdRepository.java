package world.hiro.inventory.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import world.hiro.inventory.model.Household;

@Component
public interface HouseholdRepository extends JpaRepository<Household, Long> {
  Optional<Household> findById(Long id);
  Household findByName(String name);
  List<Household> findByInviteCode(String inviteCode);
}
