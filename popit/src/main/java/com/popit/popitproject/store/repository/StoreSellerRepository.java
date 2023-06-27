package com.popit.popitproject.store.repository;

import com.popit.popitproject.store.entity.StoreEntity;
import com.popit.popitproject.store.model.StoreType;
import com.popit.popitproject.user.entity.UserEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreSellerRepository extends JpaRepository<StoreEntity, Long> {
    Optional<StoreEntity> findByUser(UserEntity user);
    Optional<StoreEntity> findByStoreName(String storeName);
    boolean existsByStoreName(String storeName);
}