package io.github.mdraihan27.mmh.dining.services.dining_record;

import io.github.mdraihan27.mmh.dining.entities.dining_record.DiningRecordEntity;
import io.github.mdraihan27.mmh.dining.entities.dining_token.DiningTokenEntity;
import io.github.mdraihan27.mmh.dining.repositories.DiningRecordRepository;
import io.github.mdraihan27.mmh.dining.repositories.MealInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class DiningRecordService {

    @Autowired
    private DiningRecordRepository diningRecordRepository;

    @Autowired
    MealInfoRepository mealInfoRepository;

    public ResponseEntity addTokenToDiningRecords(DiningTokenEntity diningToken){
        try{

            Optional<DiningRecordEntity> diningRecord = diningRecordRepository.findById(getCurrentDate());
            if(diningRecord.isPresent()){
                diningRecord.get().setTokensSold(diningRecord.get().getTokensSold() + 1);
                diningRecord.get().setTotalSales(diningRecord.get().getTotalSales()+diningToken.getMealPrice());
                diningRecordRepository.save(diningRecord.get());
            }else{
                DiningRecordEntity newDiningRecord = new DiningRecordEntity(DateTimeFormatter.ofPattern("dd-MM-yyyy")
                        .format(LocalDateTime.now()), 0, 0, 0);

                newDiningRecord.setTokensSold(1);
                newDiningRecord.setTotalSales(diningToken.getMealPrice());
                diningRecordRepository.save(newDiningRecord);
            }
            return ResponseEntity.ok().build();
        }catch (Exception e){
            throw new RuntimeException(e);
        }

    }


    public String getCurrentDate() {
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return now.format(formatter);
    }

    public String getPreviousDate() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return yesterday.format(formatter);
    }
}
