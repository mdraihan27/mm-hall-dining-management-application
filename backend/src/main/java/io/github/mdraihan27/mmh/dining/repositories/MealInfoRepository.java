package io.github.mdraihan27.mmh.dining.repositories;

import io.github.mdraihan27.mmh.dining.entities.dining_token.MealInfoEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MealInfoRepository extends MongoRepository<MealInfoEntity, String> {

}
