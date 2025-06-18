package io.github.mdraihan27.mmh.dining.repositories;

import io.github.mdraihan27.mmh.dining.entities.dining_token.DiningTokenEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DiningTokenRepository extends MongoRepository<DiningTokenEntity, String> {

}
