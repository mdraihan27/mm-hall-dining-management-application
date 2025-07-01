package io.github.mdraihan27.mmh.dining.controllers.dining_token;

import io.github.mdraihan27.mmh.dining.entities.dining_token.MealInfoEntity;
import io.github.mdraihan27.mmh.dining.repositories.MealInfoRepository;
import io.github.mdraihan27.mmh.dining.utilities.CreateResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("api/v1/")
public class MealInfoController {

    @Autowired
    private CreateResponseUtil createResponseUtil;

    @Autowired
    private MealInfoRepository mealInfoRepository;

    @PostMapping("admin/meal-info/set")
    public ResponseEntity setMealInfo(@RequestBody MealInfoEntity mealInfo) {
        try{
            if (mealInfo.getMealInfoId() == null || mealInfo.getMealInfoId().isEmpty() ||
                mealInfo.getLunchMealType() == null || mealInfo.getLunchMealType().isEmpty() ||
                mealInfo.getDinnerMealType() == null || mealInfo.getDinnerMealType().isEmpty() ) {
                return ResponseEntity
                        .badRequest()
                        .body(createResponseUtil.createResponseBody(false, "Incomplete information"));
            }

            MealInfoEntity savedMealInfo = mealInfoRepository.save(mealInfo);
            if (savedMealInfo==null){
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(createResponseUtil.createResponseBody(false, "Failed to save meal info"));
            }
            return ResponseEntity.ok().body(createResponseUtil.createResponseBody(true, "Saved meal info"));

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity
                    .internalServerError()
                    .body(createResponseUtil.createResponseBody(false, "An error occurred while setting meal info"));
        }
    }

    @GetMapping("public/meal-info/get")
    public ResponseEntity getMealInfo() {
        try{

            MealInfoEntity mealInfo = mealInfoRepository.findById("mealInfo").orElse(null);

            if(mealInfo == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(createResponseUtil.createResponseBody(false, "Meal info not found"));
            }

            Map<String, Object> mealInfoMap = Map.of(
                    "lunchMealType", mealInfo.getLunchMealType(),
                    "dinnerMealType", mealInfo.getDinnerMealType(),
                    "dinnerMealPrice", mealInfo.getDinnerMealPrice(),
                    "lunchMealPrice", mealInfo.getLunchMealPrice()
            );

            return ResponseEntity.ok()
                    .body(createResponseUtil.createResponseBody(true, "Meal info retrieved successfully", "mealInfo", mealInfoMap));

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity
                    .internalServerError()
                    .body(createResponseUtil.createResponseBody(false, "An error occurred while getting meal info"));
        }
    }
}
