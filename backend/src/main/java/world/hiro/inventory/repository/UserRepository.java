package world.hiro.inventory.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import world.hiro.inventory.model.User;

@Component
public interface UserRepository extends JpaRepository<User, Long> {
  User findByEmail(String email);
  List<User> findAllByHouseholdId(Long householdId);
  boolean existsByEmail(String email);
}
