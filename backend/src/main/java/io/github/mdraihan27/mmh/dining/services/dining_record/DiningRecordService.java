package io.github.mdraihan27.mmh.dining.services.dining_record;

import io.github.mdraihan27.mmh.dining.entities.dining_record.DiningRecordEntity;
import io.github.mdraihan27.mmh.dining.entities.dining_token.DiningTokenEntity;
import io.github.mdraihan27.mmh.dining.repositories.DiningRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class DiningRecordService {

    @Autowired
    private DiningRecordRepository diningRecordRepository;

    public void incrementTokenCount(){
        DiningRecordEntity diningRecordEntity = diningRecordRepository.findById(getCurrentDate()).orElse(null);
        if(diningRecordEntity == null){

            diningRecordEntity.setDiningRecordDay(getCurrentDate());

            DiningRecordEntity previousDayRecord = diningRecordRepository.findById(getPreviousDate()).orElse(null);

            long previousDayBalance=0;
            if(previousDayRecord != null){
                previousDayBalance = previousDayRecord.getDiningBalance();
            }

            diningRecordEntity.setDiningBalance(previousDayBalance);
            diningRecordEntity.setTokensSold(0);
            diningRecordEntity.setTotalExpenses(0);
        }

       //code mising
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
