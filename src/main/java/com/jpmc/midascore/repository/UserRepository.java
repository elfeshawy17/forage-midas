package com.jpmc.midascore.repository;

import com.jpmc.midascore.entity.UserRecord;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserRecord, Long> {
    UserRecord findById(long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT u FROM UserRecord u WHERE u.id = :id")
    Optional<UserRecord> findByIdForUpdate(@Param("id") Long id);
}
