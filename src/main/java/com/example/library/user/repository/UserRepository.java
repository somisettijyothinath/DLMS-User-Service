package com.example.library.user.repository;

import com.example.library.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    // Email operations (uses automatic UNIQUE index on email)
    Optional<UserEntity> findByEmail(String email);
    boolean existsByEmail(String email);

    // Role-based queries (fast even without explicit indexes due to low cardinality)
    List<UserEntity> findByRole(UserEntity.Role role);
    List<UserEntity> findByRoleAndActive(UserEntity.Role role, boolean active);

    // Student-specific queries
    List<UserEntity> findByStudentId(String studentId);
    List<UserEntity> findByStudentIdAndActive(String studentId, boolean active);

    // Active users
    List<UserEntity> findByActive(boolean active);
    
    // Custom queries for librarian dashboard
    @Query("SELECT u FROM UserEntity u WHERE u.role = :role AND u.active = true ORDER BY u.name")
    List<UserEntity> findActiveUsersByRole(@Param("role") UserEntity.Role role);

    @Query("SELECT COUNT(u) FROM UserEntity u WHERE u.role = :role")
    Long countByRole(@Param("role")UserEntity.Role role);
    
    // Search by name (LIKE query for partial matches)
    @Query("SELECT u FROM UserEntity u WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%')) AND u.active = true")
    List<UserEntity> searchActiveUsersByName(@Param("name") String name);

    // Count operations for dashboard
    @Query("SELECT COUNT(u) FROM UserEntity u WHERE u.role = :role AND u.active = true")
    long countActiveUsersByRole(@Param("role") UserEntity.Role role);

    // Login validation query
    @Query("SELECT u FROM UserEntity u WHERE u.email = :email")
    Optional<UserEntity> findActiveUserByEmail(@Param("email") String email);
}
