package io.github.mdraihan27.mmh.dining.repositories;


import io.github.mdraihan27.mmh.dining.entities.user.UserVerificationEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserVerificationRepository extends MongoRepository<UserVerificationEntity, String> {

}
