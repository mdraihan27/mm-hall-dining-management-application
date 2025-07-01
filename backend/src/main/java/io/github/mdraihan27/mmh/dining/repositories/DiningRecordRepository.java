package io.github.mdraihan27.mmh.dining.repositories;

import io.github.mdraihan27.mmh.dining.entities.dining_record.DiningRecordEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DiningRecordRepository extends MongoRepository<DiningRecordEntity, String> {

}
