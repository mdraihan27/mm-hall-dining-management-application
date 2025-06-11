package io.github.mdraihan27.mmh.dining.repositories;


import io.github.mdraihan27.mmh.dining.entities.user.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<UserEntity, String> {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findById(String uid);

}