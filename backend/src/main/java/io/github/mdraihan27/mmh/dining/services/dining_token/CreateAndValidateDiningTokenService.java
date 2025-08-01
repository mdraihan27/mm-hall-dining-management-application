package io.github.mdraihan27.mmh.dining.services.dining_token;

import io.github.mdraihan27.mmh.dining.entities.dining_record.DiningRecordEntity;
import io.github.mdraihan27.mmh.dining.entities.dining_token.DiningTokenEntity;
import io.github.mdraihan27.mmh.dining.entities.user.UserEntity;
import io.github.mdraihan27.mmh.dining.repositories.DiningRecordRepository;
import io.github.mdraihan27.mmh.dining.repositories.DiningTokenRepository;
import io.github.mdraihan27.mmh.dining.repositories.MealInfoRepository;
import io.github.mdraihan27.mmh.dining.repositories.UserRepository;
import io.github.mdraihan27.mmh.dining.services.dining_record.DiningRecordService;
import io.github.mdraihan27.mmh.dining.utilities.CreateResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

@Service
public class CreateAndValidateDiningTokenService {

    @Autowired
    private DiningTokenRepository tokenRepository;

    @Autowired
    private CreateResponseUtil createResponseUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MealInfoRepository mealInfoRepository;

    @Autowired
    private DiningRecordRepository diningRecordRepository;

    @Autowired
    private DiningRecordService diningRecordService;

    @Transactional
    public ResponseEntity createNewToken(UserEntity tokenOwner, DiningTokenEntity diningToken) {
        try {
            diningToken.setTokenId(UUID.randomUUID().toString().replace("-", "").substring(0, 8));
            diningToken.setTokenOwnerEmail(tokenOwner.getEmail());
            diningToken.setTokenGenerationTIme(Instant.now().toEpochMilli());

            if(diningToken.getMealTime().equals("lunch")){
                diningToken.setMealPrice(mealInfoRepository.findById("mealInfo").get().getLunchMealPrice());
            }else if(diningToken.getMealTime().equals("dinner")){
                diningToken.setMealPrice(mealInfoRepository.findById("mealInfo").get().getDinnerMealPrice());
            }



            long tokenExpirationTime;
            if (diningToken.getMealTime().equals("lunch")) {
                tokenExpirationTime = getNextDaySpecificMomentTimeStamp(15, 00);
            } else if (diningToken.getMealTime().equals("dinner")) {
                tokenExpirationTime = getNextDaySpecificMomentTimeStamp(21, 45);
            } else {
                return ResponseEntity
                        .badRequest()
                        .body(createResponseUtil.createResponseBody(false, "Meal type is invalid or empty"));
            }

            diningToken.setTokenExpirationTime(tokenExpirationTime);

            if(diningToken.getMealPrice() > tokenOwner.getBalance()) {
                return ResponseEntity
                        .badRequest()
                        .body(createResponseUtil.createResponseBody(false, "User does not have enough balance to buy this token"));
            }else{
                tokenOwner.setBalance(tokenOwner.getBalance() - diningToken.getMealPrice());
            }

            DiningTokenEntity savedToken = tokenRepository.save(diningToken);

            if (savedToken != null) {
                tokenOwner.getUserTokensId().add(savedToken.getTokenId());
                userRepository.save(tokenOwner);
                diningRecordService.addTokenToDiningRecords(savedToken);
                return ResponseEntity.ok().body(createResponseUtil.createResponseBody(true, "Token generated successfully"));
            } else {
                return ResponseEntity.internalServerError().body(createResponseUtil.createResponseBody(false, "Failed to create new token"));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }



    public ResponseEntity validateAndGetDiningToken(String tokenId) {
        try {
            DiningTokenEntity token = tokenRepository.findById(tokenId).orElse(null);
            if (token == null) {
                return ResponseEntity.badRequest().body(createResponseUtil.createResponseBody(false, "Token not found"));
            } else if (!token.getMealTime().equals("lunch") && !token.getMealTime().equals("dinner")) {
                return ResponseEntity.badRequest().body(createResponseUtil.createResponseBody(false, "Meal time is invalid or empty"));
            }

            return ResponseEntity
                    .ok()
                    .body(createResponseUtil.createResponseBody(true, "Token validated successfully", "diningTokenInfo", createResponseUtil.createDiningTokenInfoMap(token)));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    public long getNextDaySpecificMomentTimeStamp(int hour, int minute) {
        try {

            Instant now = Instant.now();

            LocalDate todayUTC = now.atZone(ZoneOffset.UTC).toLocalDate();

            LocalDate tomorrowUTC = todayUTC.plusDays(1);

            LocalDateTime tomorrow3PMUTC = LocalDateTime.of(tomorrowUTC, LocalTime.of(hour, minute));

            Instant result = tomorrow3PMUTC.toInstant(ZoneOffset.UTC);

            long epochMillis = result.toEpochMilli();

            return epochMillis;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
